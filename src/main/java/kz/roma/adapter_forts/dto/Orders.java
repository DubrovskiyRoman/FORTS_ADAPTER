package kz.roma.adapter_forts.dto;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
@Scope("prototype")
public class Orders {
    private long id;
    private String orderId; // Идентификационный номер заявки (для айсбергов - номер видимой части айсберга)
    private long orderAmount; // Количество контрактов в операции (для айсбергов - количество контрактов в операции по видимой части айсберга)
    private BigDecimal orderPrice; // Цена
    private long rowId;  // В документации replId
    private Byte orderDir; // Направление заявки 1 Buy 2 Sell
    private Long extendedStatus; // Расширенный статус заявки, значения нужно смотреть в документации
    private Date orderChangeTime; // Время изменения состояния заявки
    private Byte orderStatus; // Действие с заявкой (для айсбергов - действие с видимой частью айсберга)
    private String orderClientCode; // Код клиента
    private String traderCode; //  Логин пользователя, поставившего заявку
    private String brokerCode; // Код брокера
    private Date orderExpDate; // Дата истечения заявки
    private Integer orderIsin; // ISIN инструмента
    private Date downloadDate;

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }

    public Integer getOrderIsin() {
        return orderIsin;
    }

    public void setOrderIsin(Integer orderIsin) {
        this.orderIsin = orderIsin;
    }

    public Date getOrderExpDate() {
        return orderExpDate;
    }

    public void setOrderExpDate(Date orderExpDate) {
        this.orderExpDate = orderExpDate;
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public String getTraderCode() {
        return traderCode;
    }

    public void setTraderCode(String traderCode) {
        this.traderCode = traderCode;
    }

    public String getOrderClientCode() {
        return orderClientCode;
    }

    public void setOrderClientCode(String orderClientCode) {
        this.orderClientCode = orderClientCode;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderChangeTime() {
        return orderChangeTime;
    }

    public void setOrderChangeTime(Date orderChangeTime) {
        this.orderChangeTime = orderChangeTime;
    }

    public Long getExtendedStatus() {
        return extendedStatus;
    }

    public void setExtendedStatus(Long extendedStatus) {
        this.extendedStatus = extendedStatus;
    }

    public Byte getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(Byte orderDir) {
        this.orderDir = orderDir;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }


    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", orderAmount=" + orderAmount +
                ", orderPrice=" + orderPrice +
                ", rowId=" + rowId +
                ", orderDir=" + orderDir +
                ", extendedStatus=" + extendedStatus +
                ", orderChangeTime=" + orderChangeTime +
                ", orderStatus=" + orderStatus +
                ", orderClientCode='" + orderClientCode + '\'' +
                ", traderCode='" + traderCode + '\'' +
                ", brokerCode='" + brokerCode + '\'' +
                ", orderExpDate=" + orderExpDate +
                ", orderIsin=" + orderIsin +
                ", downloadDate=" + downloadDate +
                '}';
    }
}
