package com.example.entity.cbr;
import com.example.entity.cbr.xmlAdapter.BigDecimalXmlAdapter;


import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;


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

    public int getId_pk() {
        return id_pk;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id_pk=" + id_pk +
                ", date=" + date +
                ", id='" + id + '\'' +
                ", numCode=" + numCode +
                ", charCode='" + charCode + '\'' +
                ", nominal=" + nominal +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    public void setId_pk(int id_pk) {
        this.id_pk = id_pk;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumCode() {
        return numCode;
    }

    public void setNumCode(int numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public Integer getNominal() {
        return nominal;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}