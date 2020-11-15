package fr.ippon.logbooksample.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.Sink;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.logstash.LogstashLogbackSink;

@Configuration
@AllArgsConstructor
public class LogbookConfiguration {

    private final ObjectMapper objectMapper;

    @Bean
    public Sink sink() {
        HttpLogFormatter formatter = new JsonHttpLogFormatter(objectMapper);
        return new LogstashLogbackSink(formatter);
    }

}