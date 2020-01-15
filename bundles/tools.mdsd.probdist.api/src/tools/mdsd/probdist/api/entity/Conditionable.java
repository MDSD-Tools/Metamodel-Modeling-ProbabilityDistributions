package tools.mdsd.probdist.api.entity;

import java.util.List;

import tools.mdsd.probdist.model.probdist.distributionfunction.Domain;
import tools.mdsd.probdist.model.probdist.distributionfunction.RandomVariable;

public interface Conditionable<T> {

	public static class Conditional {

		private final Domain valueSpace;
		private final Value<?> value;

		public Conditional(Domain valueSpace, Value<?> value) {
			this.valueSpace = valueSpace;
			this.value = value;
		}
		
		public Conditional(RandomVariable randomVariable, Value<?> value) {
			this(randomVariable.getValueSpace(), value);
		}

		public Value<?> getValue() {
			return value;
		}

		public Domain getValueSpace() {
			return valueSpace;
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof Conditional) {
				Conditional otherCon = (Conditional) other;
				return getValueSpace() == otherCon.getValueSpace() && value.equals(otherCon.value);
			}
			return false;
		}

	}
	
	public T given(List<Conditional> conditionals);
	
}
