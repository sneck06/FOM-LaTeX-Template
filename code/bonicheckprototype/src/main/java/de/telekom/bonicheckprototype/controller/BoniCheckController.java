package de.telekom.bonicheckprototype.controller;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Transaction;
import de.telekom.bonicheckprototype.datatypes.api.BoniCheckRequest;
import de.telekom.bonicheckprototype.datatypes.api.BoniCheckResponse;
import de.telekom.bonicheckprototype.services.BoniCheckDecisionService;
import de.telekom.bonicheckprototype.services.ErrorMessageService;
import de.telekom.servicelogger.ServiceLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BoniCheckController {

    @Autowired
    private BoniCheckDecisionService boniCheckDecisionService;

    @Autowired
    private ErrorMessageService errorMessageService;

    @Autowired
    ServiceLogger serviceLogger;

    @PostMapping("/postdecision")
    public ResponseEntity<BoniCheckResponse> postDecision(@RequestBody BoniCheckRequest request, @RequestHeader Map<String,String> headers){

        Transaction transaction = ElasticApm.startTransactionWithRemoteParent(headerName -> headers.get("traceparent"));

        try (final Scope scope = transaction.activate()) {

            transaction.setType(Transaction.TYPE_REQUEST);
            transaction.setName("BoniCheckService");

            serviceLogger.requestIn(headers,request, HttpMethod.POST, "/postdecision" );

            ResponseEntity<BoniCheckResponse> output;
            try {
                output = boniCheckDecisionService.doBoniCheckAndGetDecision(request.getInternalId());

            } catch (Exception e){
                output = errorMessageService.createErrorMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return output;

        } catch (Exception e) {
            transaction.captureException(e);
            throw e;
        } finally {
            transaction.end();
        }

    }


    @GetMapping("/getdecision")
    public ResponseEntity<BoniCheckResponse> getDecision(@RequestParam String internalId) {
        BoniCheckRequest boniCheckRequest = new BoniCheckRequest(internalId);
        System.out.println(boniCheckRequest);
        System.out.println(boniCheckDecisionService);
        return boniCheckDecisionService.doBoniCheckAndGetDecision(boniCheckRequest.getInternalId());
    }
}
