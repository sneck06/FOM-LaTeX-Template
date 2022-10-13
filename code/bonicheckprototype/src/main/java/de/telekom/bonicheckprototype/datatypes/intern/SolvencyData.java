package de.telekom.bonicheckprototype.datatypes.intern;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "SolvencyData")
public class SolvencyData {

    @Id
    private String internalId;
    private boolean solvent;
}
