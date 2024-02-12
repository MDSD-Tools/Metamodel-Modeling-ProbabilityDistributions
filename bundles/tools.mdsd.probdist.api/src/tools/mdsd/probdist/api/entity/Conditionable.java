package tools.mdsd.probdist.api.entity;

import java.util.List;

import tools.mdsd.probdist.distributionfunction.Domain;
import tools.mdsd.probdist.distributionfunction.RandomVariable;

public interface Conditionable<I extends Value<?>> {

    public static class Conditional<I extends Value<?>> {

        private final Domain valueSpace;
        private final I value;

        public Conditional(Domain valueSpace, I value) {
            this.valueSpace = valueSpace;
            this.value = value;
        }

        public Conditional(RandomVariable randomVariable, I value) {
            this(randomVariable.getValueSpace(), value);
        }

        public I getValue() {
            return value;
        }

        public Domain getValueSpace() {
            return valueSpace;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Conditional) {
                Conditional<?> otherCon = (Conditional<?>) other;
                return getValueSpace() == otherCon.getValueSpace() && value.equals(otherCon.value);
            }
            return false;
        }

    }

    public Conditionable<I> given(List<Conditional<I>> conditionals);

}
