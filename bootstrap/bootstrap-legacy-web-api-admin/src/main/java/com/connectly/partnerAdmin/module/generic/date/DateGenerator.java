package com.connectly.partnerAdmin.module.generic.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateGenerator {
    private LocalDateTime dateTime;
    private final DateTimeFormatter formatter;

    public DateGenerator(LocalDateTime dateTime, String format) {
        this.dateTime = dateTime;
        this.formatter = DateTimeFormatter.ofPattern(format);
    }

    public DateGenerator() {
        this.dateTime = LocalDateTime.now();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public LocalDateTime getCurrentDateTime() {
        return dateTime;
    }

    public String format() {
        return dateTime.format(formatter);
    }

    public Date toDate() {
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public DateGenerator plus(long amountToAdd, ChronoUnit unit) {
        this.dateTime = this.dateTime.plus(amountToAdd, unit);
        return this;
    }

    public DateGenerator minus(long amountToSubtract, ChronoUnit unit) {
        this.dateTime = this.dateTime.minus(amountToSubtract, unit);
        return this;
    }

}
