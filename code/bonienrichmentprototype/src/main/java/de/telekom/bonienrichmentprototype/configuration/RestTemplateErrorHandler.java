package de.telekom.bonienrichmentprototype.configuration;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler{

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

        return (
                httpResponse.getStatusCode().series() == Series.CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) {}

}
