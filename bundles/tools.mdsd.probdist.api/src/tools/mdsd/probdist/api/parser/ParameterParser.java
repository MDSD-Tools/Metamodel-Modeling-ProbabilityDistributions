package tools.mdsd.probdist.api.parser;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import tools.mdsd.probdist.api.entity.CategoricalValue;
import tools.mdsd.probdist.api.entity.Matrix;
import tools.mdsd.probdist.api.entity.Vector;
import tools.mdsd.probdist.distributionfunction.SimpleParameter;

public interface ParameterParser {

    public static class Sample {

        public CategoricalValue value;
        public Double probability;

        public Sample(CategoricalValue value, Double probability) {
            this.value = value;
            this.probability = probability;
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
            Sample rhs = (Sample) other;
            return new EqualsBuilder().append(value, rhs.value)
                .append(probability, rhs.probability)
                .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(value)
                .append(probability)
                .toHashCode();
        }
    }

    public Double parseScalar(SimpleParameter param);

    public Vector parseVector(SimpleParameter param);

    public Matrix parseMatrix(SimpleParameter param);

    public Set<Sample> parseSampleSpace(SimpleParameter param);
}
