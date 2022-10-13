package de.telekom.bonienrichmentprototype.services;


import co.elastic.apm.api.CaptureSpan;
import de.telekom.bonienrichmentprototype.datatypes.api.BoniCheckRequest;
import de.telekom.bonienrichmentprototype.datatypes.intern.Person;
import de.telekom.bonienrichmentprototype.datatypes.intern.PersonWithInternalId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IDLookUpService {

    private List<PersonWithInternalId> internalIdList;
    @Autowired
    InternalIdListService internalIdListService;

    @CaptureSpan(value = "RequestData")
    public BoniCheckRequest findId(Person person){
        log.info("person: "+person);

        internalIdList = internalIdListService.getInternalIdList();

        for (PersonWithInternalId listItem : internalIdList) {
            if(person.getName().equalsIgnoreCase(listItem.getName()) &&
                    person.getFirstName().equalsIgnoreCase(listItem.getFirstName()) &&
                    person.getBirthdate().isEqual(listItem.getBirthdate()))
            {
                return new BoniCheckRequest(listItem.getInternalId());
            }
        }

        return new BoniCheckRequest(null);
    }
}
