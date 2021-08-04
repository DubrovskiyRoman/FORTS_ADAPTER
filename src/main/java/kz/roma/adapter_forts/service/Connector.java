package kz.roma.adapter_forts.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.micexrts.cgate.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service("ConnectorForts")
public class Connector {
    private static Logger logger = LoggerFactory.getLogger(Connector.class);
    private static volatile boolean exitFlag = false;
    private static volatile boolean cleanedUp = false;
    private Connection connection;
    private Listener listenerInstrument, listenerOrders, listenerDeals, listenerUsdCourse, listenerTrades;
    private List<Listener> listeners = new ArrayList<>();

    @Autowired
    private InstrumentsSubscription instrumentsSubscription;
    @Autowired
    private OrderSubscription orderSubscription;
    @Autowired
    private DealSubscription dealSubscription;
    @Autowired
    private UsdOnlineSubscription usdOnlineSubscription;
    @Autowired
    TradesSubscription tradesSubscription;

    @PostConstruct
    public void run() throws CGateException {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                exitFlag = true;
                while (!cleanedUp) ;
            }
        });

        try {
            CGate.open("ini=jrepl.ini;key=11111111");  //Initialize work environment
            connection = new Connection("p2tcp://127.0.0.1:4001;app_name=trader_office"); //Connect to local router
            logger.debug("Try to established connection with: " + connection.toString());
            String[] lsnStr = new String[5]; // The list of necessary subscriptions
            lsnStr[0] = "p2repl://FORTS_TRADE_REPL;tables=orders_log";
            lsnStr[1] = "p2repl://FORTS_REFDATA_REPL;tables=fut_instruments";
            lsnStr[2] = "p2repl://FORTS_TRADE_REPL;tables=user_deal";
            lsnStr[3] = "p2repl://FORTS_REFDATA_REPL;tables=usd_online";
            lsnStr[4] = "p2repl://FORTS_DEALS_REPL;tables=deal";
            logger.debug("The list of subscriptions: " + lsnStr.toString());

            // Create object listener for subscript on instruments, orders, deals and etc.

            listenerInstrument = new Listener(connection, lsnStr[1], instrumentsSubscription);
            listenerOrders = new Listener(connection, lsnStr[0], orderSubscription);
            listenerDeals = new Listener(connection, lsnStr[2], dealSubscription);
            listenerUsdCourse = new Listener(connection, lsnStr[3], usdOnlineSubscription);
            listenerTrades = new Listener(connection, lsnStr[4], tradesSubscription);
            listeners.add(listenerInstrument);
            listeners.add(listenerOrders);
            listeners.add(listenerDeals);
            listeners.add(listenerUsdCourse);
            listeners.add(listenerTrades);
            logger.debug("The list of listeners: " + listeners.toString());

            while (!exitFlag) {
                int state = connection.getState();
                switch (state) {
                    case State.ERROR:
                        connection.close();
                        logger.debug("Connection is closed");
                        break;
                    case State.CLOSED:
                        try {
                            connection.open("");
                            logger.debug("Connection is opened");
                        } catch (CGateException ec) {
                            logger.error("Failed opening connection: " + ec);
                        }
                        break;
                    case State.ACTIVE:
                        int result = connection.process(1);
                        if (result != ErrorCode.OK && result != ErrorCode.TIMEOUT) {
                            logger.error("Warning: connection state request failed: 0x%X", result);
                        }
                        listeners.stream().unordered().parallel().forEach((listener) -> {
                            try {
                                int listenerState = listener.getState();
                                switch (listenerState) {
                                    case State.ERROR:
                                        listener.close();
                                        logger.error("listener is closed" + listener.toString());
                                        break;
                                    case State.CLOSED:
                                        listener.open("");
                                        logger.error("listener is opened" + listener.toString());
                                        break;
                                }
                            } catch (CGateException el) {
                                logger.error("Failed worked with listener" + el);
                            }
                        });
                        break;
                }
            }
        } catch (CGateException cgex) {
            logger.error("Exception: " + cgex);

        } finally {
            listeners.stream().unordered().parallel().filter(listener -> listener != null).forEach(listener -> {
                try {
                    listener.close();
                    listener.dispose();
                } catch (CGateException e) {
                    e.printStackTrace();
                }
            });
        }
        if (connection != null) {
            try {
                connection.close();
                connection.dispose();
            } catch (CGateException cgex) {
            }
        }
        CGate.close();
        logger.debug("Work environment is closed");
        cleanedUp = true;
    }
}




