package de.telekom.bonicheckprototype.services;

import de.telekom.bonicheckprototype.datatypes.api.BoniCheckResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ErrorMessageService {

    public ResponseEntity<BoniCheckResponse> createErrorMessage(String errorMessage, HttpStatus httpStatus){
        return new ResponseEntity<>(createBoniCheckResponse(errorMessage),httpStatus);
    }

    private BoniCheckResponse createBoniCheckResponse(String errorMessage){

        return new BoniCheckResponse(
                "error",
                false,
                100,
                false,
                errorMessage
        );
    }
}
