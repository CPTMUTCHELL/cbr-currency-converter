package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name = "history")
@Getter
@Setter
public class PresentationDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;
    @Column(name = "base_currency")
    @ApiModelProperty(notes = "Currency to convert from",example = "USD",value = "USD")

    private String baseCurrency;
    @Column(name = "base_quantity")
    @ApiModelProperty(notes = "How much to convert",example = "1.5")

    private BigDecimal quantityToConvert;
    @Column(name = "target_currency")
    @ApiModelProperty(notes = "Currency to convert to",example = "EUR",value = "EUR")

    private String targetCurrency;
    @ApiModelProperty(notes = "Result")

    @Column(name = "converted_quantity")
    private BigDecimal result;

    //for rabbitmq
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonIgnore
    @ApiModelProperty(notes = "Convert date")
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
}
