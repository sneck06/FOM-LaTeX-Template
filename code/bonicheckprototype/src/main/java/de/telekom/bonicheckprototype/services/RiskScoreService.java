package de.telekom.bonicheckprototype.services;

import co.elastic.apm.api.CaptureSpan;
import de.telekom.bonicheckprototype.datatypes.intern.RiskData;
import de.telekom.bonicheckprototype.repository.RiskDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class RiskScoreService {

    @Autowired
    RiskDataRepository riskDataRepository;

    @CaptureSpan(value = "mongo-RiskData-getScore", type = "db", action="findById" )
    public int getScore(String internalId){

        Optional<RiskData> data = riskDataRepository.findById(internalId);
        return data.map(RiskData::getScore).orElse(100);

    }

    @CaptureSpan(value = "mongo-RiskData-checkScore")
    public boolean checkScore(String internalId){

        return checkScore(getScore(internalId));
    }

    public boolean checkScore(int score){

        return score < 40;

    }

    @PostConstruct
    private void createData(){

        riskDataRepository.deleteAll();
        riskDataRepository.insert(new RiskData("sm1985", 55));
        riskDataRepository.insert(new RiskData("mb1980", 60));
        riskDataRepository.insert(new RiskData("mm2000", 30));
        riskDataRepository.insert(new RiskData("mj1995", 10));

    }
}
