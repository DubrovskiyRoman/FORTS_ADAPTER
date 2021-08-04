package kz.roma.adapter_forts.dao.statistics;

import kz.roma.adapter_forts.dao.gatewayDao.GateWayDao;
import kz.roma.adapter_forts.dao.messageTypeDao.MessageTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Lazy(false)
@Service
public class StatisticsDaoImpl implements StatisticsDao {
    @Autowired
    statisticsRepo statisticsRepo;

    @Autowired
    MessageTypeDao messageTypeDao;

    @Autowired
    GateWayDao gateWayDao;

    @PostConstruct
    @Override
    public List<Long> findAllInstrRowId() {
        return statisticsRepo.findAllInstrRowId(messageTypeDao.findMessageIdByName("instruments"));
    }

    @Override
    public List<Long> findAllOrdersRowId() {
        return statisticsRepo.findAllInstrRowId(messageTypeDao.findMessageIdByName("orders"));
    }

    @Override
    public List<Long> findAllDealsRowId() {
        return statisticsRepo.findAllInstrRowId(messageTypeDao.findMessageIdByName("deals"));
    }

    @Override
    public void saveStatistics(Long rowId, String messageName) {

        statisticsRepo.saveInstrStat(LocalDateTime.now(), rowId, messageTypeDao.findMessageIdByName(messageName),
                gateWayDao.findGateWayByIdByName("SPECTRA Plaza-2"));

    }


}
