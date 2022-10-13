package de.telekom.bonicheckprototype.services;

import de.telekom.bonicheckprototype.datatypes.intern.SolvencyData;
import de.telekom.bonicheckprototype.repository.SolvencyDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SolvencyDataService {

    @Autowired
    SolvencyDataRepository solvencyDataRepository;

    public void createSolvencyData(SolvencyData solvencyData) {
        if (solvencyData != null) {
            if (solvencyDataRepository.findById(solvencyData.getInternalId()).isPresent()) {
                log.info("Exists already");
            } else {
                solvencyDataRepository.insert(solvencyData);
            }
        }
    }

    public SolvencyData findSolvencyDataById(String internalId) {
        log.info("internalId={}",internalId);

        if (internalId != null) {
            Optional<SolvencyData> solvencyData = solvencyDataRepository.findById(internalId);

            return solvencyData.orElseGet(() -> new SolvencyData("Not found", false)); //function call within function
        } else {
            log.warn("InternalId is null");
            return new SolvencyData("Is null", false);
        }
    }

    public List<SolvencyData> findAllSolvencyData() {
        return solvencyDataRepository.findAll();
    }


    public void updateSolvencyDataById(SolvencyData solvencyData) {
        String internalId = solvencyData.getInternalId();
        if (internalId != null) {
            if (solvencyDataRepository.findById(internalId).isPresent()) {
                solvencyDataRepository.save(solvencyData);
            } else {
                log.info("Update failed - InternalId wasn't found");
            }
        }
    }

    public void deleteSolvencyDataById(String internalId) {
        if (internalId != null) {
            solvencyDataRepository.deleteById(internalId);
        } else {
            log.info("Deletion failed - InternalId wasn't found");
        }
    }

    public void deleteAllSolvencyData() {
        solvencyDataRepository.deleteAll();
    }

}
