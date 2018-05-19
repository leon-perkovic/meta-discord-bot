package com.meta.leon.discordbot.config;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Leon on 16/03/2018
 */
@Configuration
public class JacksonConfig {

    @Bean
    public JodaModule jacksonJodaModule() {
        return JodaModuleConfigurator.jacksonJodaModule();
    }

}
