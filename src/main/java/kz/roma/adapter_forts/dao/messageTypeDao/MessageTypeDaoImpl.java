package kz.roma.adapter_forts.dao.messageTypeDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class MessageTypeDaoImpl implements MessageTypeDao {
    @Autowired
    MessageTypeRepo messageTypeRepo;

    @Override
    @Transactional(readOnly = true)
    public long findMessageIdByName(String name) {
        return messageTypeRepo.findMessageTypeIdByName(name);
    }

}
