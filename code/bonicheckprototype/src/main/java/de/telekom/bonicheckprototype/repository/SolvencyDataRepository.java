package de.telekom.bonicheckprototype.repository;

import de.telekom.bonicheckprototype.datatypes.intern.SolvencyData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SolvencyDataRepository extends MongoRepository<SolvencyData, String> {}
