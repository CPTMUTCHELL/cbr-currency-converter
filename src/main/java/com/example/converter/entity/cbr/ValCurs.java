package com.example.converter.entity.cbr;

import com.example.converter.entity.cbr.xmlAdapter.LocalDateXmlAdapter;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.List;

@ToString
@Data
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

}

