package fr.ippon.logbooksample.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import fr.ippon.logbooksample.functional.client.ProviderClient;
import lombok.AllArgsConstructor;
import okhttp3.ConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.okhttp.GzipInterceptor;
import org.zalando.logbook.okhttp.LogbookInterceptor;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Configuration
public class ProviderClientConfiguration {

    private final Logbook logbook;

    private final ObjectMapper objectMapper;

    @Bean
    public ProviderClient providerClient() {
        return Feign.builder()
                .client(feignClient())
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .logLevel(Logger.Level.FULL)
                .target(ProviderClient.class, "http://localhost:8081");
    }


    private Client feignClient() {
        return new OkHttpClient(
                new okhttp3.OkHttpClient.Builder()
                        .connectionPool(new ConnectionPool(100, 1, TimeUnit.HOURS))
                        .addNetworkInterceptor(new LogbookInterceptor(logbook))
                        .addNetworkInterceptor(new GzipInterceptor())
                        .build());
    }

}
