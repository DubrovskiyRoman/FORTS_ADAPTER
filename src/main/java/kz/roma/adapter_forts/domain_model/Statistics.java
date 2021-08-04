package kz.roma.adapter_forts.domain_model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    MessageType messageType;

    @ManyToOne
    @JoinColumn(name = "GATE_WAY_ID")
    GateWay gateWay;

    long objectRowId;
    LocalDateTime downloaded;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public long getObjectRowId() {
        return objectRowId;
    }

    public void setObjectRowId(long objectRowId) {
        this.objectRowId = objectRowId;
    }


    public LocalDateTime getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(LocalDateTime downloaded) {
        this.downloaded = downloaded;
    }

    @Override
    public String toString() {
        return "InstrumentsStatistics{" +
                "id=" + id +
                ", messageType=" + messageType +
                ", instrRowId=" + objectRowId +
                ", downloaded=" + downloaded +
                '}';
    }
}
