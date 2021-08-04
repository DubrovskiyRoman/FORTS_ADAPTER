package kz.roma.adapter_forts.domain_model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class MessageType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String messageCode;
    private String messageName;

    @OneToMany(mappedBy = "messageType", cascade = CascadeType.ALL, orphanRemoval = true)
    Collection<Statistics> statistics = new ArrayList<>();

    public Collection<Statistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(Collection<Statistics> statistics) {
        this.statistics = statistics;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "id=" + id +
                ", messageCode='" + messageCode + '\'' +
                ", messageName='" + messageName + '\'' +
                ", statistics=" + statistics +
                '}';
    }
}

