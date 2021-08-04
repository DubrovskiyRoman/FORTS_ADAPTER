package kz.roma.adapter_forts.dto;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
@Scope("prototype")
public class Deals {
    private long id;
    private long dealNum; // Номер сделки
    private long orderIdBuy; // Номер заявки покупателя
    private long orderIdSell; // Номер заявки продавца
    private long rowId; // В документации replId
    private Integer dealIsin; // ISIN инструмента
    private Long dealQnt; // Количество инструмента в сделках
    private BigDecimal dealPrice; // Цена сделки
    private Date dealsDate; // Время заключения сделки
    private Byte noSystemDeal; // Признак внесистемной сделки, 1 - сделка внесистемная, 0 - системная (покаупка/продажа)
    private Long buyerDealStatus; // Статус сделки со стороны покупателя
    private Long sellerDealStatus; // Статус сделки со стороны продавца
    private String buyerCode; // Код покупателя
    private String sellerCode; // Код продавца
    private String buyerCompanyCode; // Код РТС фирмы покупателя
    private String sellerCompanyCode; // Код РТС фирмы продавца

    public String getBuyerCompanyCode() {
        return buyerCompanyCode;
    }

    public void setBuyerCompanyCode(String buyerCompanyCode) {
        this.buyerCompanyCode = buyerCompanyCode;
    }

    public String getSellerCompanyCode() {
        return sellerCompanyCode;
    }

    public void setSellerCompanyCode(String sellerCompanyCode) {
        this.sellerCompanyCode = sellerCompanyCode;
    }

    public String getSellerCode() {
        return sellerCode;
    }

    public void setSellerCode(String sellerCode) {
        this.sellerCode = sellerCode;
    }

    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode;
    }

    public Long getBuyerDealStatus() {
        return buyerDealStatus;
    }

    public void setBuyerDealStatus(Long buyerDealStatus) {
        this.buyerDealStatus = buyerDealStatus;
    }

    public Long getSellerDealStatus() {
        return sellerDealStatus;
    }

    public void setSellerDealStatus(Long sellerDealStatus) {
        this.sellerDealStatus = sellerDealStatus;
    }

    public Byte getNoSystemDeal() {
        return noSystemDeal;
    }

    public void setNoSystemDeal(Byte noSystemDeal) {
        this.noSystemDeal = noSystemDeal;
    }

    public Date getDealsDate() {
        return dealsDate;
    }

    public void setDealsDate(Date dealsDate) {
        this.dealsDate = dealsDate;
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public Long getDealQnt() {
        return dealQnt;
    }

    public void setDealQnt(Long dealQnt) {
        this.dealQnt = dealQnt;
    }

    public Integer getDealIsin() {
        return dealIsin;
    }

    public void setDealIsin(Integer dealIsin) {
        this.dealIsin = dealIsin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDealNum() {
        return dealNum;
    }

    public void setDealNum(long dealNum) {
        this.dealNum = dealNum;
    }

    public long getOrderIdBuy() {
        return orderIdBuy;
    }

    public void setOrderIdBuy(long orderIdBuy) {
        this.orderIdBuy = orderIdBuy;
    }

    public long getOrderIdSell() {
        return orderIdSell;
    }

    public void setOrderIdSell(long orderIdSell) {
        this.orderIdSell = orderIdSell;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    @Override
    public String toString() {
        return "Deals{" +
                "id=" + id +
                ", dealNum=" + dealNum +
                ", orderIdBuy=" + orderIdBuy +
                ", orderIdSell=" + orderIdSell +
                ", rowId=" + rowId +
                ", dealIsin=" + dealIsin +
                ", dealQnt=" + dealQnt +
                ", dealPrice=" + dealPrice +
                ", dealsDate=" + dealsDate +
                ", noSystemDeal=" + noSystemDeal +
                ", buyerDealStatus=" + buyerDealStatus +
                ", sellerDealStatus=" + sellerDealStatus +
                ", buyerCode='" + buyerCode + '\'' +
                ", sellerCode='" + sellerCode + '\'' +
                ", buyerCompanyCode='" + buyerCompanyCode + '\'' +
                ", sellerCompanyCode='" + sellerCompanyCode + '\'' +
                '}';
    }
}
