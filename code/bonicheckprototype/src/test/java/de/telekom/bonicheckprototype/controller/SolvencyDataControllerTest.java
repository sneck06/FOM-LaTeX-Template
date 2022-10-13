package de.telekom.bonicheckprototype.controller;

import de.telekom.bonicheckprototype.configuration.ApplicationInit;
import de.telekom.bonicheckprototype.datatypes.api.SolvencyDataIdRequest;
import de.telekom.bonicheckprototype.datatypes.intern.SolvencyData;
import de.telekom.bonicheckprototype.repository.SolvencyDataRepository;
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
class SolvencyDataControllerTest {

    @Autowired
    JsonConverter jsonConverter;

    @Autowired
    SolvencyDataRepository solvencyDataRepository;

    @LocalServerPort
    private int randomServerPort;

    private WebTestClient webTestClient;

    @BeforeAll
    private static void initTest() {
        ApplicationInit.init();
    }

    private final SolvencyDataIdRequest requestBodyGood = new SolvencyDataIdRequest("sm1985");
    private final SolvencyDataIdRequest requestBodyBad = new SolvencyDataIdRequest("sm1986");

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
    @DisplayName("Create Solvency Data Endpoint -> ok")
    void createSolvencyDataEndPoint() {
        String uri = "https://localhost:" + randomServerPort + "/createsolvencydata";

        webTestClient.post()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(generateSolvencyDataForTest())
                .exchange()
                .expectStatus()
                .isOk();

        SolvencyData expected = generateSolvencyDataForTest();
        SolvencyData result = solvencyDataRepository.findById(expected.getInternalId()).get();

        Assertions.assertEquals(expected, result);

        solvencyDataRepository.deleteById("TD0001");
    }

    @Test
    @Order(3)
    @DisplayName("find Solvency Data by ID Endpoint -> ok")
    void findSolvencyDataByIdEndPoint1() {
        String uri = "https://localhost:" + randomServerPort + "/findsolvencydatabyid";
        SolvencyData expectedBody =new SolvencyData("sm1985",true);

        webTestClient.post()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBodyGood)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SolvencyData.class)
                .isEqualTo(expectedBody);
    }

    @Test
    @Order(4)
    @DisplayName("find Solvency Data by ID Endpoint -> not found")
    void findSolvencyDataByIdEndPoint2() {
        String uri = "https://localhost:" + randomServerPort + "/findsolvencydatabyid";
        SolvencyData expectedBody =new SolvencyData("Not found", false);

        webTestClient.post()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBodyBad)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SolvencyData.class)
                .isEqualTo(expectedBody);
    }

    @Test
    @Order(5)
    @DisplayName("get All Solvency Data Endpoint -> ok")
    void getAllSolvencyDataEndPoint() {
        String uri = "https://localhost:" + randomServerPort + "/getallsolvencydata";
        List<SolvencyData> solvencyDataExpected = new ArrayList<>();

        solvencyDataExpected.add(new SolvencyData("sm1985", true));
        solvencyDataExpected.add(new SolvencyData("mb1980", false));
        solvencyDataExpected.add(new SolvencyData("mm2000", false));
        solvencyDataExpected.add(new SolvencyData("mj1995", true));

         webTestClient.get()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                        Assertions.assertEquals(jsonConverter.convertObjectToJSON(solvencyDataExpected),new String(response.getResponseBody()));
                });
    }

    @Test
    @Order(7)
    @DisplayName("update Solvency Data by Id Endpoint -> ok")
    void updateSolvencyDataByIdEndPoint() {

        String uri = "https://localhost:" + randomServerPort + "/updatesolvencydatabyid";

        webTestClient.post()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(generateSolvencyDataForTest("mm2000",true))
                .exchange()
                .expectStatus()
                .isOk();

        SolvencyData expected = new SolvencyData("mm2000",true);
        SolvencyData result = solvencyDataRepository.findById("mm2000").get();

        Assertions.assertEquals(expected, result);

        solvencyDataRepository.save(generateSolvencyDataForTest("mm2000",false));
    }

    @Test
    @Order(9)
    @DisplayName("delete Solvency Data by Id Endpoint -> ok")
    void deleteSolvencyDataByIdEndPoint() {

        String uri = "https://localhost:" + randomServerPort + "/deletesolvencydatabyid";

        webTestClient.post()
                .uri(uri)
                .header("X-Request-ID", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBodyGood)
                .exchange()
                .expectStatus()
                .isOk();

        solvencyDataRepository.save(new SolvencyData("sm1985", true));

    }


    //---- Helper Methods
    private SolvencyData generateSolvencyDataForTest() {

        return new SolvencyData("TD0001", true);

    }

    private SolvencyData generateSolvencyDataForTest(String internalId, boolean solvent) {

        return new SolvencyData(internalId, solvent);

    }

    private Map<String, String> generateRequestHeaders() {
        Map<String, String> requestheaders = new HashMap<>();

        requestheaders.put("X-Request-ID", "12345");

        return requestheaders;
    }

}
