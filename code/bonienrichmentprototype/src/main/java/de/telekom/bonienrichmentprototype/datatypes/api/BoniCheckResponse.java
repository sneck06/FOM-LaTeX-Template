package de.telekom.bonienrichmentprototype.datatypes.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BoniCheckResponse {

    private String internalId;
    private boolean solvent;
    private int score;
    private boolean decision;
    private String errorMessage;

}
