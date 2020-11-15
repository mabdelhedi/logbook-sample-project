package fr.ippon.logbooksample.functional.controller;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import fr.ippon.logbooksample.functional.model.Company;
import fr.ippon.logbooksample.functional.model.Status;
import fr.ippon.logbooksample.common.LogbookSpy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(LogbookSpy.class)
@AutoConfigureMockMvc
class SearchControllerIT {

    private static final String SEARCH_URL = "http://localhost/companies";

    private static final String PROVIDER_URL = "http://localhost:8081/provider/companies";

    @Autowired
    private MockMvc mockMvc;

    private WireMockServer providerWiremockServer = new WireMockServer(8081);

    private final LogbookSpy logbookSpy;

    public SearchControllerIT(final LogbookSpy logbookSpy) {
        this.logbookSpy = logbookSpy;
    }

    @BeforeEach
    void startupWiremock() {
        providerWiremockServer.start();
    }

    @AfterEach
    void stopWiremock() {
        providerWiremockServer.stop();
    }


    @Test
    void testSearchCompanies() throws Exception {

        final String queryString = "?country=FR&name=Ippon";

        List<Company> companies = List.of(Company.builder()
                        .name("Ippon Technologies Paris")
                        .streetName("75 Avenue de la compilation")
                        .country("FR")
                        .status(Status.A)
                        .build(),
                Company.builder()
                        .name("Ippon Technologies Marseille")
                        .streetName("13 Rue du NPE")
                        .country("FR")
                        .status(Status.A)
                        .build());

        String companiesJson = new ObjectMapper().writeValueAsString(companies);

        providerWiremockServer.stubFor(get(urlMatching("/provider/companies.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(companiesJson)));

        mockMvc.perform(MockMvcRequestBuilders.get("/companies")
                .queryParam("country", "FR")
                .queryParam("name", "Ippon")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));

        logbookSpy.assertLogged(Level.TRACE, SEARCH_URL, 2);
        logbookSpy.assertLogged(Level.TRACE, PROVIDER_URL, 2);

        verifyHttpLog(SEARCH_URL, queryString, "remote", "local", "127.0.0.1");

        verifyHttpLog(PROVIDER_URL, queryString, "local", "remote", "localhost");
    }


    private void verifyHttpLog(String url, String queryString, String originRequest, String originResponse,
                               String remote) {

        final String protocol = "HTTP/1.1";

        final List<Map<String, Object>> providerHttpLogs = logbookSpy.getHttpLogsFromContent(Level.TRACE, url);
        assertThat(providerHttpLogs).hasSize(2);

        final Map<String, Object> providerRequestLog = providerHttpLogs.stream()
                .filter(httpLog -> "request".equals(httpLog.get("type")))
                .findFirst()
                .orElse(Map.of());

        final Map<String, Object> providerResponseLog = providerHttpLogs.stream()
                .filter(httpLog -> "response".equals(httpLog.get("type")))
                .findFirst()
                .orElse(Map.of());

        verifyHttpRequestLog(providerRequestLog, url.concat(queryString),
                HttpMethod.GET.name(), protocol, originRequest, remote);

        verifyHttpResponseLog(providerResponseLog, protocol, originResponse, 200);

        assertThat(providerRequestLog.get("correlation")).isEqualTo(providerResponseLog.get("correlation"));
    }

    private void verifyHttpRequestLog(Map<String, Object> httpLogMap, String uri, String method, String protocol,
                                             String origin, String remote) {
        assertThat(httpLogMap.get("uri")).isEqualTo(uri);
        assertThat(httpLogMap.get("method")).isEqualTo(method);
        assertThat(httpLogMap.get("protocol")).isEqualTo(protocol);
        assertThat(httpLogMap.get("origin")).isEqualTo(origin);
        assertThat(httpLogMap.get("remote")).isEqualTo(remote);
        assertThat(httpLogMap.get("headers")).isInstanceOf(Map.class);
        assertThat(httpLogMap.get("correlation")).isNotNull();
    }

    private void verifyHttpResponseLog(Map<String, Object> httpLogMap, String protocol,
                                              String origin, Integer status) {

        assertThat(httpLogMap.get("protocol")).isEqualTo(protocol);
        assertThat(httpLogMap.get("origin")).isEqualTo(origin);
        assertThat(httpLogMap.get("status")).isEqualTo(status);
        assertThat(httpLogMap.get("headers")).isInstanceOf(Map.class);
        assertThat(httpLogMap.get("correlation")).isNotNull();
        assertThat(httpLogMap.get("duration")).isInstanceOf(Integer.class);
    }

}