package com.example.converter.entity.cbr;

import com.example.converter.entity.cbr.xmlAdapter.BigDecimalXmlAdapter;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
@XmlRootElement(name = "Valute")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "cbr")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_pk;
    private LocalDate date;
    @XmlAttribute(name = "ID")
    private String id;
    @Column(name = "num_code")
    @XmlElement(name = "NumCode")
    private int numCode;
    @Column(name = "char_code")
    @XmlElement(name = "CharCode")
    private String charCode;

    @XmlElement(name = "Nominal")
    private Integer nominal;

    @XmlElement(name = "Name")
    private String name;

    @XmlJavaTypeAdapter(BigDecimalXmlAdapter.class)
    @XmlElement(name = "Value")
    private BigDecimal value;

}
