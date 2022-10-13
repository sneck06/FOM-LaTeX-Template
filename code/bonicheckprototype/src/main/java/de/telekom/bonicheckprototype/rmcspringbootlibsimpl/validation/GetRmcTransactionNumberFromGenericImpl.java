package de.telekom.bonicheckprototype.rmcspringbootlibsimpl.validation;

import de.telekom.RmcTransactionNumberFromGenericInterface;
import org.springframework.stereotype.Component;

@Component
public class GetRmcTransactionNumberFromGenericImpl implements RmcTransactionNumberFromGenericInterface {
    @Override
    public <T> String getRmCTransactionNumber(T input) {
        String rmcTransactionNumber = "noRmcTransactionNumber";

// Input Types
        //Implement if with Inputtypes



// OutputTypes
        //Implement if with Outputtypes

        return rmcTransactionNumber;
    }
}

// Examples:
// Input Types
        /*if (input instanceof PushOrderResultRequest) {
            PushOrderResultRequest pushOrderResultRequest = (PushOrderResultRequest) input;

            if (pushOrderResultRequest.getData().getRiskCheckResponse().getRmcTransactionNumber() != null) {
                rmcTransactionNumber = pushOrderResultRequest.getData().getRiskCheckResponse().getRmcTransactionNumber();
            }
        }*/



// OutputTypes
        /*
        if (input instanceof InternalOrderCheckResponse) {
            InternalOrderCheckResponse internalOrderCheckResponse = (InternalOrderCheckResponse) input;

            if (internalOrderCheckResponse.getData().getInternalOrderCheckSolvencyResult().getRmcTransactionNumber() != null) {
                rmcTransactionNumber = internalOrderCheckResponse.getData().getInternalOrderCheckSolvencyResult().getRmcTransactionNumber();
            }
        }
        */
