package de.telekom.bonicheckprototype.controller;

import de.telekom.bonicheckprototype.configuration.ApplicationInit;
import de.telekom.bonicheckprototype.datatypes.api.BoniCheckRequest;
import de.telekom.bonicheckprototype.datatypes.api.BoniCheckResponse;
import de.telekom.bonicheckprototype.datatypes.api.RiskDataIdRequest;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportAutoConfiguration(classes = CollectorRegistry.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BoniCheckControllerTest {

    @Autowired
    JsonConverter jsonConverter;

    @LocalServerPort
    private int randomServerPort;

    private WebTestClient webTestClient;

    private final BoniCheckRequest requestBodyGood = new BoniCheckRequest("sm1985");

    private final BoniCheckRequest requestBodyBad = new BoniCheckRequest(null);


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
    @DisplayName("Post Decision -> ok")
    void postDecision1() {
        String uri = "https://localhost:" + randomServerPort + "/postdecision";

        BoniCheckResponse expectedBody = new BoniCheckResponse("sm1985", true, 55, false, null);

        webTestClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBodyGood)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BoniCheckResponse.class)
                .isEqualTo(expectedBody);

    }

    @Test
    @Order(2)
    @DisplayName("Post Decision -> not ok")
    void postDecision2() {
        String uri = "https://localhost:" + randomServerPort + "/postdecision";

        BoniCheckResponse expectedBody = new BoniCheckResponse("error", false, 100, false, "The given id must not be null!");

        webTestClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBodyBad)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(BoniCheckResponse.class)
                .isEqualTo(expectedBody);

    }

    //
    //Gutfall und schlechtfall

    @Test
    void getDecision() {
        //nicht benötigt
    }
}