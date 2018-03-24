package com.meta.leon.discordbot.config;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;

import java.util.TimeZone;

/**
 * @author Leon, created on 16/03/2018
 */
public class JodaModuleConfigurator{

    public static JodaModule jacksonJodaModule(){

        JodaModule jodaModule = new JodaModule();

        DateTimeFormatterFactory formatterFactory = new DateTimeFormatterFactory();

        formatterFactory.setIso(DateTimeFormat.ISO.DATE_TIME);

        jodaModule.addSerializer(DateTime.class, new DateTimeSerializer(
                new JacksonJodaDateFormat(
                        formatterFactory.createDateTimeFormatter().withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("CET")))
                )));

        return jodaModule;
    }

}
