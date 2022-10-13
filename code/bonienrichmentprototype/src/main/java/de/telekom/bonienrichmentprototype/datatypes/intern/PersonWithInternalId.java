package de.telekom.bonienrichmentprototype.datatypes.intern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonWithInternalId extends Person{

    private String internalId;

    public PersonWithInternalId(String name, String firstName, LocalDate birthdate, String internalId) {
        super(name, firstName, birthdate);
        this.internalId = internalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PersonWithInternalId that = (PersonWithInternalId) o;
        return Objects.equals(internalId, that.internalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), internalId);
    }
}
