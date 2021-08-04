package kz.roma.adapter_forts.dao.gatewayDao;

import kz.roma.adapter_forts.domain_model.GateWay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GateWayRepo extends JpaRepository <GateWay, Long> {
    @Query(value = "SELECT gw.id FROM gate_way AS gw WHERE gw.gate_way_name =:gw.gate_way_name", nativeQuery = true)
    Long findGateWayIdByName(@Param("gw.gate_way_name") String gwName);
}
