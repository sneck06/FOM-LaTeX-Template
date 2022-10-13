package de.telekom.bonicheckprototype.controller;

import de.telekom.bonicheckprototype.configuration.ApplicationInit;
import de.telekom.bonicheckprototype.datatypes.api.RiskDataIdRequest;
import de.telekom.bonicheckprototype.datatypes.intern.RiskData;
import de.telekom.bonicheckprototype.repository.RiskDataRepository;
import de.telekom.jsonconverter.JsonConverter;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.prometheus.client.CollectorRegistry;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportAutoConfiguration(classes = CollectorRegistry.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RiskDataControllerTest {

    @Autowired
    JsonConverter jsonConverter;

    @LocalServerPort
    private int randomServerPort;

    @Autowired
    RiskDataRepository riskDataRepository;

    private WebTestClient webTestClient;

    private final RiskDataIdRequest requestBodyGood = new RiskDataIdRequest("sm1985");

    private final RiskDataIdRequest requestBodyBad = new RiskDataIdRequest("sm1986");


    @BeforeAll
    private static void initTest() {
        ApplicationInit.init();
    }

    @BeforeEach
    public void setup() {
        String baseUri = "https://localhost:" + randomServerPort;
        // WebTestClient config to ignore X.509 for running the tests. Needed because Spring Boot Security is active.
        try {
            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

            HttpClient httpClient = HttpClient.create().secure(ssl -> {
                ssl.sslContext(sslContext);
            });

            ClientHttpConnector httpConnector = new ReactorClientHttpConnector(
                    httpClient);

            webTestClient = WebTestClient.bindToServer(httpConnector).baseUrl(baseUri).build();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(1)
    @DisplayName("Create Risk Data Endpoint -> ok")
    void createRiskDataEndPoint() {
        String uri = "https://localhost:" + randomServerPort + "/createriskdata";

        webTestClient.post()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(generateRiskDataForTest())
                .exchange()
                .expectStatus()
                .isOk();

        RiskData expected = generateRiskDataForTest();
        RiskData actual = riskDataRepository.findById(expected.getInternalId()).get();

        Assertions.assertEquals(expected, actual);

        riskDataRepository.save(generateRiskDataForTest("mb1980", 60));

        riskDataRepository.deleteById("TD001");
    }

    @Test
    @Order(2)
    @DisplayName("Find Risk Data By Id Endpoint -> ok")
    void findRiskDataByIdEndPoint1() {
        String uri = "https://localhost:" + randomServerPort + "/findriskdatabyid";
        RiskData expectedBody = new RiskData("sm1985",55);

        webTestClient.post()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBodyGood)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(RiskData.class)
                .isEqualTo(expectedBody);
    }

    @Test
    @Order(3)
    @DisplayName("Find Risk Data By Id Endpoint -> not found")
    void findRiskDataByIdEndPoint2() {
        String uri = "https://localhost:" + randomServerPort + "/findriskdatabyid";
        RiskData expectedBody = new RiskData("Not found",0);

        webTestClient.post()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBodyBad)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(RiskData.class)
                .isEqualTo(expectedBody);
    }

    // man könnte auch noch einen Testfall für Id ist null schreiben

    @Test
    @Order(4)
    @DisplayName("Find All Risk Data Endpoint -> ok")
    void getAllRiskDataEndPoint() {
        String uri = "https://localhost:" + randomServerPort + "/getallriskdata";
        List<RiskData> riskDataExpected = new ArrayList<>();

        riskDataExpected.add(new RiskData("sm1985", 55));
        riskDataExpected.add(new RiskData("mb1980", 60));
        riskDataExpected.add(new RiskData("mm2000", 30));
        riskDataExpected.add(new RiskData("mj1995", 10));

        webTestClient.get()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    Assertions.assertEquals(jsonConverter.convertObjectToJSON(riskDataExpected), new String(response.getResponseBody()));
                });

    }

    @Test
    @Order(5)
    @DisplayName("Update Risk Data By Id Endpoint -> ok")
    void updateRiskDataByIdEndPoint() {
        String uri = "https://localhost:" + randomServerPort + "/updateriskdatabyid";

        webTestClient.post()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(generateRiskDataForTest("mb1980", 20))
                .exchange()
                .expectStatus()
                .isOk();

        RiskData expected = new RiskData("mb1980", 20);
        RiskData actual = riskDataRepository.findById("mb1980").get();

        Assertions.assertEquals(expected, actual);

        riskDataRepository.save(generateRiskDataForTest("mb1980", 60));
    }

    @Test
    @Order(6)
    @DisplayName("Delete Risk Data By Id Endpoint -> ok")
    void deleteRiskDataByIdEndPoint() {
        String uri = "https://localhost:" + randomServerPort + "/deleteriskdatabyid";

        webTestClient.post()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBodyGood)
                .exchange()
                .expectStatus()
                .isOk();

        riskDataRepository.save(new RiskData("sm1985", 55));

    }


    //---- Helper Methods
    private RiskData generateRiskDataForTest() {

        return new RiskData("TD001", 60);
    }

    private RiskData generateRiskDataForTest(String internalId, int score) {

        return new RiskData(internalId, score);
    }

    private Map<String, String> generateRequestHeaders() {
        Map<String, String> requestheaders = new HashMap<>();

        requestheaders.put("X-Request-ID", "12345");

        return requestheaders;
    }

}
