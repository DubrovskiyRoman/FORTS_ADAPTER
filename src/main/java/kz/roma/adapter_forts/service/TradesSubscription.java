package kz.roma.adapter_forts.service;


import kz.roma.adapter_forts.config.RabbitConfigMarketData;
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
public class TradesSubscription implements ISubscriber {
    private static Logger logger = LoggerFactory.getLogger(DealSubscription.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public int onMessage(Connection connection, Listener listener, Message message) {
        int messageType = message.getType();
        switch (messageType) {
            case MessageType.MSG_OPEN:
                logger.debug("Ready to receive Trades messages from FORTS");
                break;
            case MessageType.MSG_STREAM_DATA:
                StreamDataMessage streamDataMessage = (StreamDataMessage) message;
                if (streamDataMessage.getMsgName().equalsIgnoreCase("deal")) {
                    HashMap<String, Object> tradesMap = new HashMap<>();
                    try {
                        tradesMap.put("replId", streamDataMessage.getField("replID").get());
                        tradesMap.put("id_deal", streamDataMessage.getField("id_deal").get());
                        tradesMap.put("isin_id", streamDataMessage.getField("isin_id").get());
                        tradesMap.put("xamount", streamDataMessage.getField("xamount").get());
                        tradesMap.put("price", streamDataMessage.getField("price").get());
                        logger.debug("Send trades message to rabbit: " + tradesMap.toString());
                        rabbitTemplate.convertAndSend(RabbitConfigMarketData.fortsMarketDataExch, RabbitConfigMarketData.tradesRoutingKEY, tradesMap);
                    } catch (CGateException e) {
                        e.printStackTrace();
                    }
                }
        }
        return ErrorCode.OK;
    }
}
