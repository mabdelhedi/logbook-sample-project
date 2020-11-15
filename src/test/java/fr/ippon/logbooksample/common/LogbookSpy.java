package fr.ippon.logbooksample.common;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class LogbookSpy implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private Logger logger;
    private ListAppender<ILoggingEvent> appender;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        appender = new ListAppender<>();
        logger = (Logger) LoggerFactory.getLogger("org.zalando.logbook.Logbook");
        logger.addAppender(appender);
        logger.setLevel(Level.TRACE);
        appender.start();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        logger.detachAppender(appender);
    }

    public void assertLogged(Level level, String content, int count) {
        assertThat(appender.list.stream().filter(withLog(level, content)).count()).isEqualTo(count);
    }

    public List<Map<String,Object>> getHttpLogsFromContent(Level level, String content) {
        return appender.list.stream().filter(withLog(level, content))
                .map(ILoggingEvent::getMarker)
                .map(buildHttpLogMap())
                .filter(Objects::nonNull)
        .collect(Collectors.toList());
    }

    private Function<Marker, Map<String, Object>> buildHttpLogMap() {
        return marker -> {
            Map<String, Object> httpLogMap = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String markerString =  objectMapper.writeValueAsString(marker);
                Map<String, String> markerMap = objectMapper.readValue(markerString, Map.class);
                httpLogMap = objectMapper.readValue(markerMap.get("fieldValue"), Map.class);

            } catch (JsonProcessingException ignored) {
            }
            return httpLogMap;
        };
    }

    private Predicate<ILoggingEvent> withLog(Level level, String content) {
        return event -> level.equals(event.getLevel()) && event.toString().contains(content);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(LogbookSpy.class);
    }

    @Override
    public LogbookSpy resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return this;
    }
}
