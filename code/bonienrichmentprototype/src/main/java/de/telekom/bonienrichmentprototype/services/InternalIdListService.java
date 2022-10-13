package de.telekom.bonienrichmentprototype.services;

import co.elastic.apm.api.CaptureSpan;
import de.telekom.bonienrichmentprototype.datatypes.intern.PersonWithInternalId;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InternalIdListService {

    private List<PersonWithInternalId> internalIdList;

    @CaptureSpan(value = "getInternalIdList")
    public List<PersonWithInternalId> getInternalIdList(){
        return internalIdList;
    }

    @PostConstruct
    private void createList(){

        internalIdList = new ArrayList<>();

        internalIdList.add(new PersonWithInternalId("Smith", "John", LocalDate.of(1985, 11, 11), "sm1985"));
        internalIdList.add(new PersonWithInternalId("Mustermann", "Bob", LocalDate.of(1980, 4, 13), "mb1980"));
        internalIdList.add(new PersonWithInternalId("Musterfrau", "Marianne", LocalDate.of(2000, 5, 2), "mm2000"));
        internalIdList.add(new PersonWithInternalId("Miller", "Jill", LocalDate.of(1995, 8, 29), "mj1995"));
    }
}
