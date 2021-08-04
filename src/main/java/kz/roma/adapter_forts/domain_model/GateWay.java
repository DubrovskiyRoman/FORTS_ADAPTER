package kz.roma.adapter_forts.domain_model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class GateWay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gateWayName;
    private String version;

    @ManyToOne
    @JoinColumn(name = "STOCK_ID")
    Stocks stocks;

    @OneToMany(mappedBy = "gateWay", cascade = CascadeType.ALL, orphanRemoval = true)
    Collection<Statistics> statistics = new ArrayList<>();

    public String getGateWayName() {
        return gateWayName;
    }

    public void setGateWayName(String gateWayName) {
        this.gateWayName = gateWayName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Stocks getStocks() {
        return stocks;
    }

    public void setStocks(Stocks stocks) {
        this.stocks = stocks;
    }

    @Override
    public String toString() {
        return "GateWay{" +
                "id=" + id +
                ", gateWayName='" + gateWayName + '\'' +
                ", version='" + version + '\'' +
                ", stocks=" + stocks +
                '}';
    }
}
