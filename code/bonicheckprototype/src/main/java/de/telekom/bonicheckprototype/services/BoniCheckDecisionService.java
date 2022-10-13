package de.telekom.bonicheckprototype.services;

import co.elastic.apm.api.CaptureSpan;
import de.telekom.bonicheckprototype.datatypes.api.BoniCheckResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BoniCheckDecisionService {

    @Autowired
    private SolvencyCheckService solvencyCheckService;

    @Autowired
    private RiskScoreService riskScoreService;

    @CaptureSpan(value = "doBoniCheckAndGetDecision")
    public ResponseEntity<BoniCheckResponse> doBoniCheckAndGetDecision(String internalId) {

        boolean decision;
        boolean solvent = solvencyCheckService.checkSolvency(internalId);
        boolean inScore = riskScoreService.checkScore(internalId);
        decision = solvent && inScore;


        BoniCheckResponse boniCheckResponse = new BoniCheckResponse(internalId, solvent, riskScoreService.getScore(internalId), decision, null);

        return new ResponseEntity<>(boniCheckResponse, HttpStatus.OK);
    }

}
