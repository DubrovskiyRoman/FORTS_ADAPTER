package kz.roma.adapter_forts.service;

import kz.roma.adapter_forts.config.RabbitConfigMarketData;
import kz.roma.adapter_forts.dao.statistics.StatisticsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.micexrts.cgate.*;
import ru.micexrts.cgate.messages.Message;
import ru.micexrts.cgate.messages.StreamDataMessage;

import java.util.HashMap;

@Service
public class UsdOnlineSubscription implements ISubscriber {
    private static Logger logger = LoggerFactory.getLogger(UsdOnlineSubscription.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StatisticsDao statisticsDao;

    @Override
    public int onMessage(Connection connection, Listener listener, Message message) {
        int messageType = message.getType();
        switch (messageType) {
            case MessageType.MSG_OPEN:
                System.out.println("Begin to receive usd_ONLINE messages!");
                break;
            case MessageType.MSG_STREAM_DATA:
                StreamDataMessage streamDataMessage = (StreamDataMessage) message;
                HashMap<String, Object> usdQuotesMap = new HashMap<String, Object>();
                if (streamDataMessage.getMsgName().equalsIgnoreCase("usd_online")) {
                    logger.info("Ready to receive USD Rates");
                    try {
                        usdQuotesMap.put("replID", streamDataMessage.getField("replID").get());
                        usdQuotesMap.put("replRev", streamDataMessage.getField("replRev").get());
                        usdQuotesMap.put("replAct", streamDataMessage.getField("replAct").get());
                        usdQuotesMap.put("valut_id", streamDataMessage.getField("id").get());
                        usdQuotesMap.put("rate", streamDataMessage.getField("rate").get());
                        usdQuotesMap.put("moment", streamDataMessage.getField("moment").get());
                        logger.debug(usdQuotesMap.toString());
                        statisticsDao.saveStatistics((Long)usdQuotesMap.get("replRev"), "usd_online");
                        rabbitTemplate.convertAndSend(RabbitConfigMarketData.fortsMarketDataExch, RabbitConfigMarketData.marketDataRoutingKEY, usdQuotesMap);
                    } catch (CGateException e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
        return ErrorCode.OK;
    }
}
