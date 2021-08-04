package kz.roma.adapter_forts.dao.messageTypeDao;

import kz.roma.adapter_forts.domain_model.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageTypeRepo extends JpaRepository<MessageType, Long> {
    @Query(value = "select mt.id from message_type mt where mt.message_name =:name", nativeQuery = true)
    long findMessageTypeIdByName(@Param("name") String name);
}
