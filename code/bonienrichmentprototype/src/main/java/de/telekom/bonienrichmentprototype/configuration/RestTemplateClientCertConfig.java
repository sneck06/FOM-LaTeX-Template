package de.telekom.bonienrichmentprototype.configuration;


import java.time.Duration;
import javax.net.ssl.SSLContext;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RestTemplateClientCertConfig {
    final Duration TIMEOUT = Duration.ofMillis(10_000);

    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;

    @Value("${server.ssl.trust-store-password}")
    private String trustStorePassword;

    @Value("${server.ssl.key-store}")
    private String fileKeyStore;

    @Value("${server.ssl.trust-store}")
    private String fileTrustStore;


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {

        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(ResourceUtils.getFile(fileTrustStore), trustStorePassword.toCharArray())
                .loadKeyMaterial(ResourceUtils.getFile(fileKeyStore), keyStorePassword.toCharArray(), keyStorePassword.toCharArray())
                .build();

        HttpClient client = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();


        log.info("using key/truststore: {} and {}", fileKeyStore, fileTrustStore);
        return builder
                .requestFactory(() ->new HttpComponentsClientHttpRequestFactory(client))
                .errorHandler(new RestTemplateErrorHandler())
                .setConnectTimeout(TIMEOUT)
                .setReadTimeout(TIMEOUT)
                .build();
    }


    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }
}
