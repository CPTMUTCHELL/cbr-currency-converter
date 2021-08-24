package com.example.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PresentationDto {

    private int id;
    private String baseCurrency;
    private BigDecimal quantityToConvert;
    private String targetCurrency;
    private BigDecimal result;
    private LocalDate date;

    @Override
    public String toString() {
        return "PresentationDto{" +
                "id=" + id +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", quantityToConvert=" + quantityToConvert +
                ", targetCurrency='" + targetCurrency + '\'' +
                ", result=" + result +
                ", date=" + date +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public BigDecimal getQuantityToConvert() {
        return quantityToConvert;
    }

    public void setQuantityToConvert(BigDecimal quantityToConvert) {
        this.quantityToConvert = quantityToConvert;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
