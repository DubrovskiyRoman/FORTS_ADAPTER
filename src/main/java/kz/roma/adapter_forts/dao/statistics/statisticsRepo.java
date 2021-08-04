package kz.roma.adapter_forts.dao.statistics;

import kz.roma.adapter_forts.domain_model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface statisticsRepo extends JpaRepository<Statistics, Long> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO statistics (downloaded, object_row_id, message_id, gate_way_id) " +
            "VALUES            (:downloaded, :object_row_id, :message_id, :gate_way_id)", nativeQuery = true)
    void saveInstrStat(@Param("downloaded") LocalDateTime saveTime, @Param("object_row_id") long instr_row_id,
                       @Param("message_id") long message_id, @Param("gate_way_id") long gate_way_id);

    @Query(value = "SELECT st.object_row_id FROM statistics st WHERE st.message_id=:message_id", nativeQuery = true)
    List<Long> findAllInstrRowId(@Param("message_id") long message_id);


}
