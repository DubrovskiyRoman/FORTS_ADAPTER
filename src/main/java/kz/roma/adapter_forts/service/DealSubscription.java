package kz.roma.adapter_forts.service;

import kz.roma.adapter_forts.dao.statistics.StatisticsDao;
import kz.roma.adapter_forts.dto.Deals;
import kz.roma.adapter_forts.config.RabbitConfigBackOffice;
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
public class DealSubscription implements ISubscriber {
    private static Logger logger = LoggerFactory.getLogger(DealSubscription.class);
    private StatisticsDao statisticsDao;
    private List<Long> dealRepIdList = new ArrayList<>();

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    public DealSubscription(StatisticsDao statisticsDao) {
        this.statisticsDao = statisticsDao;
        this.dealRepIdList = statisticsDao.findAllDealsRowId();
    }

    @Override
    public int onMessage(Connection connection, Listener listener, Message message) {
        int messageType = message.getType();
        switch (messageType) {
            case MessageType.MSG_OPEN:
                logger.debug("Ready to receive instruments messages from FORTS");
                break;
            case MessageType.MSG_STREAM_DATA:
                StreamDataMessage streamDataMessage = (StreamDataMessage) message;
                if (streamDataMessage.getMsgName().equalsIgnoreCase("user_deal")) {
                    List<HashMap<String, Object>> dealList = new ArrayList<>();
                    HashMap<String, Object> dealMap = new HashMap<>();
                    try {
                        dealMap.put("replID", streamDataMessage.getField("replID").get());
                        dealMap.put("id_deal", streamDataMessage.getField("id_deal").get());
                        dealMap.put("private_order_id_buy", streamDataMessage.getField("private_order_id_buy").get());
                        dealMap.put("private_order_id_sell", streamDataMessage.getField("private_order_id_sell").get());
                        dealMap.put("replID", streamDataMessage.getField("replID").get());
                        dealMap.put("isin_id", streamDataMessage.getField("isin_id").get());
                        dealMap.put("xamount", streamDataMessage.getField("xamount").get());
                        dealMap.put("price", streamDataMessage.getField("price").get());
                        dealMap.put("moment", streamDataMessage.getField("moment").get());
                        dealMap.put("nosystem", streamDataMessage.getField("nosystem").get());
                        dealMap.put("xstatus_buy", streamDataMessage.getField("xstatus_buy").get());
                        dealMap.put("xstatus_sell", streamDataMessage.getField("xstatus_sell").get());
                        dealMap.put("code_buy", streamDataMessage.getField("code_buy").get());
                        dealMap.put("code_sell", streamDataMessage.getField("code_sell").get());
                        dealMap.put("code_rts_buy", streamDataMessage.getField("code_rts_buy").get());
                        dealMap.put("code_rts_sell", streamDataMessage.getField("code_rts_sell").get());
                        dealList.add(dealMap);
                        logger.debug("Instrument Message received from FORTS: " + dealList.toString());
                    } catch (CGateException e) {
                        logger.error("Can't receive message from FORTS " + e);
                    }
                    checkDealRowUnique(dealList);
                }
        }
        return ErrorCode.OK;
    }

    public void checkDealRowUnique(List<HashMap<String, Object>> dealList) {
        if (dealRepIdList.isEmpty()) {
            System.out.println("Table Deals is Empty");
            parseToDeal(dealList);
            Stream orderStreamRowId = dealList.stream().unordered().parallel().map((deal) -> deal.get("replID"));
            dealRepIdList.addAll((List<Long>) orderStreamRowId.collect(Collectors.toCollection(ArrayList::new)));
        }
        if (!dealRepIdList.isEmpty()) {
            Stream compareDealReplId = dealList.stream().unordered().parallel().map((deal) -> deal.get("replID"));
            boolean anymatch = compareDealReplId.anyMatch(dealRepIdList::contains);
            if (anymatch) {
                dealList.stream().unordered().parallel().forEach((deal) -> System.out.println("We have deal № " + deal.get("id_deal")
                        + " replID " + deal.get("replID") + " in cache"));

            } else if (anymatch != true) {
                parseToDeal(dealList);
                Stream dealRowId = dealList.stream().unordered().parallel().map((deal) -> deal.get("replID"));
                dealRepIdList.addAll((List<Long>) dealRowId.collect(Collectors.toCollection(ArrayList::new)));
            }
        }

    }

    public void parseToDeal(List<HashMap<String, Object>> dealList) {
        dealList.stream().unordered().parallel().forEach((deal) -> {
            Deals deals = applicationContext.getBean(Deals.class);
            deals.setDealNum((Long) deal.get("id_deal"));
            deals.setOrderIdBuy((Long) deal.get("private_order_id_buy"));
            deals.setOrderIdSell((Long) deal.get("private_order_id_sell"));
            deals.setRowId((Long) deal.get("replID"));
            deals.setDealIsin((Integer) deal.get("isin_id"));
            deals.setDealQnt((Long) deal.get("xamount"));
            deals.setDealPrice((BigDecimal) deal.get("price"));
            deals.setDealsDate((Date) deal.get("moment"));
            deals.setNoSystemDeal((Byte) deal.get("nosystem"));
            deals.setBuyerDealStatus((Long) deal.get("xstatus_buy"));
            deals.setSellerDealStatus((Long) deal.get("xstatus_sell"));
            deals.setBuyerCode((String) deal.get("code_buy"));
            deals.setSellerCode((String) deal.get("code_sell"));
            deals.setBuyerCompanyCode((String) deal.get("code_rts_buy"));
            deals.setSellerCompanyCode((String) deal.get("code_rts_sell"));
            statisticsDao.saveStatistics(deals.getRowId(), "deals");
            logger.debug("Deal's rowId " +  deals.getRowId() + " is saved in db:" );
            rabbitTemplate.convertAndSend(RabbitConfigBackOffice.fortsBackOfficeExch, RabbitConfigBackOffice.dealsRoutingKEY, deals);
            logger.debug("Deals is send to rabbit: " + deals.toString());
        });

    }
}



