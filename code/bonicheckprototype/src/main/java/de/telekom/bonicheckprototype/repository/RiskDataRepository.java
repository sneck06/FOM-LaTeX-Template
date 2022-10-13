package de.telekom.bonicheckprototype.repository;

import de.telekom.bonicheckprototype.datatypes.intern.RiskData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RiskDataRepository extends MongoRepository<RiskData, String> {
}
