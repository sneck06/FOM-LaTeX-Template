package de.telekom.bonicheckprototype.services;

import de.telekom.bonicheckprototype.datatypes.intern.RiskData;
import de.telekom.bonicheckprototype.repository.RiskDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RiskDataService {

    @Autowired
    RiskDataRepository riskDataRepository;

    public void createRiskData(RiskData riskData) {
        if (riskData != null) {
            if (riskDataRepository.findById(riskData.getInternalId()).isPresent()) {
                log.info("Exists already");
            } else {
                riskDataRepository.insert(riskData);
            }
        }
    }

    public RiskData findRiskDataById(String internalId) {
        if (internalId != null) {
            Optional<RiskData> riskData = riskDataRepository.findById(internalId);
            return riskData.orElseGet(() -> {
                return new RiskData("Not found", 0);
            }); //function call within function
        } else {
            log.warn("InternalId is null");
            return new RiskData("Is null", 0);
        }
    }

    public List<RiskData> findAllRiskData() { // get
        return riskDataRepository.findAll();
    }

    public void updateRiskDataById(RiskData riskData) {
        String internalId = riskData.getInternalId();
        if (internalId != null) {
            if (riskDataRepository.findById(internalId).isPresent()) {
                riskDataRepository.save(riskData);
            } else {
                log.info("Update failed - InternalId not found");
            }
        }
    }

    public void deleteRiskDataById(String internalId) {
        if (internalId != null) {
            riskDataRepository.deleteById(internalId);
        } else {
            log.info("Deletion by Id failed - InternalId not found");
        }
    }

    public void deleteAllRiskData() {
        riskDataRepository.deleteAll();
    }

}
