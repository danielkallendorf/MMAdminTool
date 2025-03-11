package com.migesok.jaxb.adapter.javatime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * {@code XmlAdapter} mapping JSR-310 {@code LocalDate} to ISO-8601 string
 * <p>
 * It uses {@link java.time.format.DateTimeFormatter#ISO_DATE} for parsing and serializing,
 * time-zone information ignored.
 *
 * @see jakarta.xml.bind.annotation.adapters.XmlAdapter
 * @see java.time.LocalDate
 */
public class LocalDateXmlAdapter extends TemporalAccessorXmlAdapter<LocalDate> {
    public LocalDateXmlAdapter() {
        super(DateTimeFormatter.ISO_DATE, LocalDate::from);
    }
}
