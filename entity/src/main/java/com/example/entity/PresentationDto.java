package com.example.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name = "history")
public class PresentationDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "base_currency")
    private String baseCurrency;
    @Column(name = "base_quantity")
    private BigDecimal quantityToConvert;
    @Column(name = "target_currency")
    private String targetCurrency;
    @Column(name = "converted_quantity")
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
