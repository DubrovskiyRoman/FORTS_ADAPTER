package kz.roma.adapter_forts.dao.statistics;

import java.util.List;

public interface StatisticsDao {
    void saveStatistics(Long rowId, String messageName);

    List<Long> findAllInstrRowId();

    List<Long> findAllOrdersRowId();

    List<Long> findAllDealsRowId();


}
