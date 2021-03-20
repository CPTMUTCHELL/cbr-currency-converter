package com.example.converter.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
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
}
