package de.telekom.bonicheckprototype.datatypes.intern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "RiskData")
public class RiskData {

    @Id
    private String internalId;
    private int score;
}
