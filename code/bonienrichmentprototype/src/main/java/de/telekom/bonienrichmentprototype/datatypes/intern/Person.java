package de.telekom.bonienrichmentprototype.datatypes.intern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private String name;
    private String firstName;
    private LocalDate birthdate;

}
