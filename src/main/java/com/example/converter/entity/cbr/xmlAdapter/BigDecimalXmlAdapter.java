package com.example.converter.entity.cbr.xmlAdapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;
import java.util.Optional;

public class BigDecimalXmlAdapter extends XmlAdapter<String, BigDecimal> {

    @Override
    public BigDecimal unmarshal(String stringValue) throws Exception {
        return Optional.ofNullable(stringValue)
                .map(value -> new BigDecimal(value.replace(',', '.')))
                .orElse(null);
    }

    @Override
    public String marshal(BigDecimal bigDecimal) throws Exception {
        return null;
    }


}
