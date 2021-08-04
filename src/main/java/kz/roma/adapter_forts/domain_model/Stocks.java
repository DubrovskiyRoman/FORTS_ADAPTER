package kz.roma.adapter_forts.domain_model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Stocks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String  shortName;
    private String country;

    @OneToMany (mappedBy = "stocks", cascade = CascadeType.ALL, orphanRemoval = true)
    Collection<GateWay> gateWays = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Collection<GateWay> getGateWays() {
        return gateWays;
    }

    public void setGateWays(Collection<GateWay> gateWays) {
        this.gateWays = gateWays;
    }

    @Override
    public String toString() {
        return "Stocks{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", country='" + country + '\'' +
                ", gateWays=" + gateWays +
                '}';
    }
}
