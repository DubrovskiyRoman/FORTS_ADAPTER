package kz.roma.adapter_forts.service;


import kz.roma.adapter_forts.config.DateConfig;
import kz.roma.adapter_forts.config.RabbitConfigBackOffice;
import kz.roma.adapter_forts.dao.statistics.StatisticsDao;
import kz.roma.adapter_forts.dto.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.micexrts.cgate.*;
import ru.micexrts.cgate.messages.Message;
import ru.micexrts.cgate.messages.StreamDataMessage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class OrderSubscription implements ISubscriber {
    private static Logger logger = LoggerFactory.getLogger(OrderSubscription.class);
    private StatisticsDao statisticsDao;
    private List<Long> orderRepIdList = new ArrayList<>();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DateConfig dateConfig;

    @Autowired
    public OrderSubscription(StatisticsDao statisticsDao) {
        this.statisticsDao = statisticsDao;
        this.orderRepIdList = statisticsDao.findAllOrdersRowId();
    }

    @Override
    public int onMessage(Connection connection, Listener listener, Message message) {
        int messageType = message.getType();
        switch (messageType) {
            case MessageType.MSG_OPEN:
                logger.debug("Ready to receive orders messages from FORTS");
                break;
            case MessageType.MSG_STREAM_DATA:
                StreamDataMessage streamDataMessage = (StreamDataMessage) message;
                List<HashMap<String, Object>> orderList = new ArrayList<HashMap<String, Object>>();
                HashMap<String, Object> orderMap = new HashMap<String, Object>();

                if (streamDataMessage.getMsgName().equalsIgnoreCase("orders_log")) {
                    try {
                        orderMap.put("replID", streamDataMessage.getField("replID").get());
                        orderMap.put("public_order_id", streamDataMessage.getField("public_order_id").get());
                        orderMap.put("public_amount", streamDataMessage.getField("public_amount").get());
                        orderMap.put("price", streamDataMessage.getField("price").get());
                        orderMap.put("dir", streamDataMessage.getField("dir").get());
                        orderMap.put("xstatus", streamDataMessage.getField("xstatus").get());
                        orderMap.put("moment", streamDataMessage.getField("moment").get());
                        orderMap.put("public_action", streamDataMessage.getField("public_action").get());
                        orderMap.put("client_code", streamDataMessage.getField("client_code").get());
                        orderMap.put("login_from", streamDataMessage.getField("login_from").get());
                        orderMap.put("broker_from_rts", streamDataMessage.getField("broker_from_rts").get());
                        orderMap.put("date_exp", streamDataMessage.getField("date_exp").get());
                        orderMap.put("isin_id", streamDataMessage.getField("isin_id").get());
                        orderList.add(orderMap);
                        logger.debug("Instrument Message received from FORTS: " + orderList.toString());
                    } catch (CGateException e) {
                        logger.error("Can't receive message from FORTS " + e);
                    }
                    checkOrderRowUnique(orderList);
                    break;
                }
        }
        return ErrorCode.OK;
    }

    public void checkOrderRowUnique(List<HashMap<String, Object>> orderList) {
        if (orderRepIdList.isEmpty()) {
            logger.debug("Table statistics is empty");
            parseToOrder(orderList);
            Stream orderStreamRowId = orderList.stream().unordered().parallel().map((order) -> order.get("replID"));
            orderRepIdList.addAll((List<Long>) orderStreamRowId.collect(Collectors.toCollection(ArrayList::new)));

        }
        if (!orderRepIdList.isEmpty()) {
            Stream compareOrderReplId = orderList.stream().unordered().parallel().map((order) -> order.get("replID"));
            boolean anymatch = compareOrderReplId.anyMatch(orderRepIdList::contains);
            if (anymatch) {
                orderList.stream().unordered().parallel().forEach((order) -> System.out.println("We have order № " + order.get("public_order_id")
                        + " replID " + order.get("replID") + " in cache"));
            } else if (anymatch != true) {
                parseToOrder(orderList);
                Stream orderRowId = orderList.stream().unordered().parallel().map((order) -> order.get("replID"));
                orderRepIdList.addAll((List<Long>) orderRowId.collect(Collectors.toCollection(ArrayList::new)));
            }
        }
    }

    public void parseToOrder(List<HashMap<String, Object>> orderList) {
        orderList.stream().unordered().parallel().forEach((order) -> {
            Orders orders = applicationContext.getBean(Orders.class);
            orders.setOrderId(String.valueOf(order.get("public_order_id")));
            orders.setOrderAmount((Long) order.get("public_amount"));
            orders.setOrderPrice((BigDecimal) order.get("price"));
            orders.setRowId((Long) order.get("replID"));
            orders.setOrderDir((Byte) order.get("dir"));
            orders.setExtendedStatus((Long) order.get("xstatus"));
            orders.setOrderChangeTime((Date) order.get("moment"));
            orders.setOrderStatus((Byte) order.get("public_action"));
            orders.setOrderClientCode((String) order.get("client_code"));
            orders.setTraderCode((String) order.get("login_from"));
            orders.setBrokerCode((String) order.get("broker_from_rts"));
            orders.setOrderExpDate((Date) order.get("date_exp"));
            orders.setOrderIsin((Integer) order.get("isin_id"));
            orders.setDownloadDate(dateConfig.getDate());
            statisticsDao.saveStatistics(orders.getRowId(), "orders");
            logger.debug("Order's rowId is saved in db: " + orders.getRowId());
            rabbitTemplate.convertAndSend(RabbitConfigBackOffice.fortsBackOfficeExch, RabbitConfigBackOffice.ordersRoutingKEY, orders);
            System.out.println(orders.toString());
            logger.debug("Instrument is send to rabbit: " + orders.toString());
        });
    }
}



