package de.telekom.bonicheckprototype.services;

import co.elastic.apm.api.CaptureSpan;
import de.telekom.bonicheckprototype.datatypes.intern.SolvencyData;
import de.telekom.bonicheckprototype.repository.SolvencyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;


@Service
public class SolvencyCheckService {

    @Autowired
    SolvencyDataRepository solvencyDataRepository;

    @CaptureSpan(value = "mongo-SolvencyData", type = "db", action="findById" )
    public boolean checkSolvency (String internalId){

        Optional<SolvencyData> data = solvencyDataRepository.findById(internalId);
        return data.map(SolvencyData::isSolvent).orElse(false);

    }

    @PostConstruct
    private void createData(){

        solvencyDataRepository.deleteAll();
        solvencyDataRepository.insert(new SolvencyData("sm1985", true));
        solvencyDataRepository.insert(new SolvencyData("mb1980", false));
        solvencyDataRepository.insert(new SolvencyData("mm2000", false));
        solvencyDataRepository.insert(new SolvencyData("mj1995", true));

    }
}
