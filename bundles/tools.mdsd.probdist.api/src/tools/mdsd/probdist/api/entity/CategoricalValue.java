package tools.mdsd.probdist.api.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;

import tools.mdsd.probdist.distributionfunction.Domain;

public class CategoricalValue extends Value<String> {

    protected CategoricalValue(String value) {
        super(value, Domain.CATEGORY);
    }

    public static CategoricalValue create(String value) {
        return new CategoricalValue(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        CategoricalValue rhs = (CategoricalValue) other;
        return new EqualsBuilder().append(value, rhs.value)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }

}
