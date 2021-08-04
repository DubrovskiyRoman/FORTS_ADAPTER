package kz.roma.adapter_forts.service;

import kz.roma.adapter_forts.config.DateConfig;
import kz.roma.adapter_forts.dao.statistics.StatisticsDao;
import kz.roma.adapter_forts.dto.Instruments;
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
public class InstrumentsSubscription implements ISubscriber {
    private static Logger logger = LoggerFactory.getLogger(InstrumentsSubscription.class);
    private StatisticsDao StatisticsDao;
    private List<Long> instrRepIdList = new ArrayList<>();

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DateConfig dateConfig;

    @Autowired
    public InstrumentsSubscription(StatisticsDao StatisticsDao) {
        this.StatisticsDao = StatisticsDao;
        this.instrRepIdList = StatisticsDao.findAllInstrRowId();
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
                if (streamDataMessage.getMsgName().equalsIgnoreCase("fut_instruments")) {
                    List<HashMap<String, Object>> instrList = new ArrayList<HashMap<String, Object>>();
                    HashMap<String, Object> instrMap = new HashMap<String, Object>();
                    try {
                        instrMap.put("replID", streamDataMessage.getField("replID").get());
                        instrMap.put("base_contract_code", streamDataMessage.getField("base_contract_code").get());
                        instrMap.put("isin", streamDataMessage.getField("isin").get());
                        instrMap.put("isin_id", streamDataMessage.getField("isin_id").get());
                        instrMap.put("roundto", streamDataMessage.getField("roundto").get());
                        instrMap.put("min_step", streamDataMessage.getField("min_step").get());
                        instrMap.put("lot_volume", streamDataMessage.getField("lot_volume").get());
                        instrMap.put("last_trade_date", streamDataMessage.getField("last_trade_date").get());
                        instrMap.put("d_exp_start", streamDataMessage.getField("d_exp_start").get());
                        instrMap.put("is_percent", streamDataMessage.getField("is_percent").get());
                        instrMap.put("percent_rate", streamDataMessage.getField("percent_rate").get());
                        instrMap.put("pctyield_coeff", streamDataMessage.getField("pctyield_coeff").get());
                        instrMap.put("pctyield_total", streamDataMessage.getField("pctyield_total").get());
                        instrList.add(instrMap);
                        logger.debug("Instrument Message received from FORTS: " + instrList.toString());
                    } catch (CGateException e) {
                        logger.error("Can't receive message from FORTS " + e);
                    }
                    checkInstrumentRowUnique(instrList);
                    break;
                }
        }
        return ErrorCode.OK;
    }

    public void checkInstrumentRowUnique(List<HashMap<String, Object>> instrList) {
        if (instrRepIdList.isEmpty()) {
            logger.debug("Table statistics is empty");
            parseToInstr(instrList);
            Stream instrStreamRowId = instrList.stream().unordered().parallel().map((instr) -> instr.get("replID"));
            instrRepIdList.addAll((List<Long>) instrStreamRowId.collect(Collectors.toCollection(ArrayList::new)));
        }
        if (!instrRepIdList.isEmpty()) {
            Stream compareInstrReplId = instrList.stream().unordered().parallel().map((instr) -> instr.get("replID"));
            boolean anymatch = compareInstrReplId.anyMatch(instrRepIdList::contains);

            if (anymatch) {
                instrList.stream().unordered().parallel().forEach((instrRepId) -> System.out.println("We have instrument: "
                        + instrRepId.get("isin") + " replID " + instrRepId.get("replID") + " in cache"));
            } else if (anymatch != true) {
                parseToInstr(instrList);
                Stream instrRowId = instrList.stream().unordered().parallel().map((instr) -> instr.get("replID"));
                instrRepIdList.addAll((List<Long>) instrRowId.collect(Collectors.toCollection(ArrayList::new)));
            }
        }
    }

    public void parseToInstr(List<HashMap<String, Object>> instrList) {
        instrList.stream().unordered().parallel().forEach((instr) -> {
            Instruments instruments = applicationContext.getBean(Instruments.class);
            instruments.setBaseCode((String) instr.get("base_contract_code"));
            instruments.setInstrCode((String) instr.get("isin"));
            instruments.setRowId((Long) instr.get("replID"));
            instruments.setInstrId((Integer) instr.get("isin_id"));
            instruments.setRoundTo((Integer) instr.get("roundto"));
            instruments.setMinPriceStep((BigDecimal) instr.get("min_step"));
            instruments.setLotVolume((Integer) instr.get("lot_volume"));
            instruments.setLastTradeDate((Date) instr.get("last_trade_date"));
            instruments.setInstrExecDate((Date) instr.get("d_exp_start"));
            instruments.setIsFutures((Byte) instr.get("is_percent"));
            instruments.setPercentRate((BigDecimal) instr.get("percent_rate"));
            instruments.setCoeff((BigDecimal) instr.get("pctyield_coeff"));
            instruments.setCoeffTotal((BigDecimal) instr.get("pctyield_total"));
            instruments.setDownloadDate(dateConfig.getDate());
            StatisticsDao.saveStatistics(instruments.getRowId(), "instruments");
            logger.debug("Instrument's rowId is saved in db: " + instruments.getRowId());
            rabbitTemplate.convertAndSend(RabbitConfigBackOffice.fortsBackOfficeExch, RabbitConfigBackOffice.instrRoutingKEY, instruments);
            logger.debug("Instrument is send to rabbit: " + instruments.toString());
        });
    }
}
























