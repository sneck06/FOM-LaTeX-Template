package de.telekom.bonienrichmentprototype.controller;



import co.elastic.apm.api.CaptureTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.telekom.bonienrichmentprototype.datatypes.api.BoniCheckRequest;
import de.telekom.bonienrichmentprototype.datatypes.api.BoniCheckResponse;
import de.telekom.bonienrichmentprototype.datatypes.intern.Person;
import de.telekom.bonienrichmentprototype.services.BoniCallService;
import de.telekom.bonienrichmentprototype.services.ErrorMessageService;
import de.telekom.bonienrichmentprototype.services.IDLookUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BoniLookUpController {

    @Autowired
    private IDLookUpService idLookUpService;

    @Autowired
    private BoniCallService boniCallService;

    @Autowired
    private ErrorMessageService errorMessageService;

    @Autowired
    ObjectMapper om;

    @CaptureTransaction(value = "BoniEnrichmentService")
    @PostMapping("/startbonicheck")
    public ResponseEntity<BoniCheckResponse> startBoniCheck(@RequestBody Person person) {
        BoniCheckRequest boniCheckRequest = idLookUpService.findId(person);
        ResponseEntity<BoniCheckResponse> bcr;
        try {
            bcr = boniCallService.callBoniCheck(boniCheckRequest);
        } catch (Exception e){
            bcr = errorMessageService.createErrorMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info(String.valueOf(bcr));
        return bcr;
    }
}
