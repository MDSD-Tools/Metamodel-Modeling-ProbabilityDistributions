package tools.mdsd.probdist.api.entity;

import java.util.ArrayList;
import java.util.List;

import tools.mdsd.probdist.model.probdist.distributionfunction.Domain;
import tools.mdsd.probdist.model.probdist.distributionfunction.ProbabilityDistribution;
import tools.mdsd.probdist.model.probdist.distributionfunction.RandomVariable;
import tools.mdsd.probdist.model.probdist.distributionfunction.TabularCPD;

public class ConditionalProbabilityDistribution extends ProbabilityDistributionFunction<CategoricalValue> {

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

	// private final CPDRepresentation cpdRepresentation;
	private final CPDEvaluator cpdEvaluator;
	private final List<Conditional> conditionals;

	public ConditionalProbabilityDistribution(ProbabilityDistribution distribution, TabularCPD tabularCPD) {
		super(distribution.getInstantiated());

		// this.cpdRepresentation = cpdRepresentation;
		this.cpdEvaluator = new TabularCPDEvaluator(tabularCPD, distribution);
		this.conditionals = new ArrayList<>();
	}

	@Override
	public Double probability(CategoricalValue value) {
//		ProbabilityDistributionFunction<?> pdf = cpdRepresentation.getPDFGiven(value.getConditionals())
//				.orElseThrow(() -> new ProbabilityDistributionException(
//						"The specified conditionals are not in accordance with the CPD.",
//						new IllegalArgumentException()));
//		return pdf.probability(value.value);
		return cpdEvaluator.evaluate(value, conditionals);
	}

	@Override
	public CategoricalValue sample() {
		return cpdEvaluator.getCPDGiven(conditionals).sample();
	}

	public ConditionalProbabilityDistribution given(List<Conditional> conditionals) {
		this.conditionals.clear();
		this.conditionals.addAll(conditionals);
		return this;
	}
	
}
