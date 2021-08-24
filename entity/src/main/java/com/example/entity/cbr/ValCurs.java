package com.example.entity.cbr;
import com.example.entity.cbr.xmlAdapter.LocalDateXmlAdapter;


import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ValCurs")
public class ValCurs {

    @XmlAttribute
    private String name;

    @XmlAttribute(name = "Date")
    @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
    private LocalDate date;

    @XmlElement(name = "Valute")
    private List<Currency> currencies;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ValCurs{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", currencies=" + currencies +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }
}