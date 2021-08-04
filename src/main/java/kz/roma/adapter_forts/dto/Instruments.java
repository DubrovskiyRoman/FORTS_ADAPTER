package kz.roma.adapter_forts.dto;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
@Scope("prototype")
public class Instruments {
    private long id;
    private long rowId; // в документации replId
    private String instrCode; // код инструмента, в документации isin
    private Integer instrId; // Уникальный числовой идентификатор инструмента, в документации isin_id
    private String baseCode; //Код базового инструмента
    private Integer roundTo; // Количество знаков после запятой в цене
    private BigDecimal minPriceStep; // Минимальный шаг цены
    private Integer lotVolume; // Количество единиц базового актива в инструменте
    private Date lastTradeDate; // Дата окончания обращения инструмента
    private Date instrExecDate; //Дата начала исполнения инструмента
    private Byte isFutures; // Признак контракта. 0 – обычный фьючерс, 1 – процентный фьючерс, 2 – фьючерс на погоду и электричество, 3 – фьючерс на евробонды
    private BigDecimal percentRate; // Процентная ставка для расчета вариационной маржи по процентным фьючерсам
    private BigDecimal coeff; // Коэффициент для вычисления доходности по фьючерсам на процентные ставки
    private BigDecimal coeffTotal; // Сумма ставок для вычисления доходности по фьючерсам на процентные ставки
    private Date downloadDate;

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }

    public BigDecimal getCoeff() {
        return coeff;
    }

    public void setCoeff(BigDecimal coeff) {
        this.coeff = coeff;
    }

    public BigDecimal getCoeffTotal() {
        return coeffTotal;
    }

    public void setCoeffTotal(BigDecimal coeffTotal) {
        this.coeffTotal = coeffTotal;
    }

    public BigDecimal getPercentRate() {
        return percentRate;
    }

    public void setPercentRate(BigDecimal percentRate) {
        this.percentRate = percentRate;
    }

    public Byte getIsFutures() {
        return isFutures;
    }

    public void setIsFutures(Byte isFutures) {
        this.isFutures = isFutures;
    }

    public Date getInstrExecDate() {
        return instrExecDate;
    }

    public void setInstrExecDate(Date instrExecDate) {
        this.instrExecDate = instrExecDate;
    }

    public Date getLastTradeDate() {
        return lastTradeDate;
    }

    public void setLastTradeDate(Date lastTradeDate) {
        this.lastTradeDate = lastTradeDate;
    }

    public Integer getLotVolume() {
        return lotVolume;
    }

    public void setLotVolume(Integer lotVolume) {
        this.lotVolume = lotVolume;
    }

    public BigDecimal getMinPriceStep() {
        return minPriceStep;
    }

    public void setMinPriceStep(BigDecimal minPriceStep) {
        this.minPriceStep = minPriceStep;
    }

    public Integer getRoundTo() {
        return roundTo;
    }

    public void setRoundTo(Integer roundTo) {
        this.roundTo = roundTo;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public String getInstrCode() {
        return instrCode;
    }

    public void setInstrCode(String instrCode) {
        this.instrCode = instrCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public Integer getInstrId() {
        return instrId;
    }

    public void setInstrId(Integer instrId) {
        this.instrId = instrId;
    }

    @Override
    public String toString() {
        return "Instruments{" +
                "id=" + id +
                ", rowId=" + rowId +
                ", instrCode='" + instrCode + '\'' +
                ", instrId=" + instrId +
                ", baseCode='" + baseCode + '\'' +
                ", roundTo=" + roundTo +
                ", minPriceStep=" + minPriceStep +
                ", lotVolume=" + lotVolume +
                ", lastTradeDate=" + lastTradeDate +
                ", instrExecDate=" + instrExecDate +
                ", isFutures=" + isFutures +
                ", percentRate=" + percentRate +
                ", coeff=" + coeff +
                ", coeffTotal=" + coeffTotal +
                ", downloadDate=" + downloadDate +
                '}';
    }
}

