package kz.roma.adapter_forts.dao.gatewayDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateWayDaoImpl implements GateWayDao {
    @Autowired
    GateWayRepo gateWayRepo;

    @Override
    public long findGateWayByIdByName(String name) {
        return gateWayRepo.findGateWayIdByName(name);
    }
}
