package de.telekom.bonienrichmentprototype.services;


import co.elastic.apm.api.CaptureSpan;
import de.telekom.bonienrichmentprototype.configuration.AppConfiguration;
import de.telekom.bonienrichmentprototype.datatypes.api.BoniCheckRequest;
import de.telekom.bonienrichmentprototype.datatypes.api.BoniCheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoniCallService {

    @Autowired
    AppConfiguration appConfiguration;

    @Autowired
    RestTemplate restTemplate;
    @CaptureSpan(value = "callBoniCheck")
    public ResponseEntity<BoniCheckResponse> callBoniCheck(BoniCheckRequest boniCheckRequest){


        HttpEntity<BoniCheckRequest> request = new HttpEntity<>(boniCheckRequest);

        return restTemplate.exchange(appConfiguration.getBONI_CHECK_SERVICE_URL(), HttpMethod.POST, request, BoniCheckResponse.class);
    }
}
