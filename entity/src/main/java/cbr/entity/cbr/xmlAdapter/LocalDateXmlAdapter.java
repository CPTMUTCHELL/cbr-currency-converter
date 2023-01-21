package cbr.entity.cbr.xmlAdapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public LocalDate unmarshal(String stringValue) throws Exception {
        return Optional.ofNullable(stringValue)
                .map(value -> LocalDate.from(DATE_FORMATTER.parse(value)))
                .orElse(null);
    }

    @Override
    public String marshal(LocalDate dateValue) throws Exception {
        return null;
    }
}