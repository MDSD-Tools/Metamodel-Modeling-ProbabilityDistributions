package tools.mdsd.probdist.api.entity;

import java.util.ArrayList;
import java.util.List;

import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.model.probdist.distributionfunction.Domain;
import tools.mdsd.probdist.model.probdist.distributionfunction.RandomVariable;

public class ConditionalProbabilityDistribution extends ProbabilityDistribution {

	public static class Conditional {
		
		private final RandomVariable randomVariable;
		private final Value<?> value;
		
		public Conditional(RandomVariable randomVariable, Value<?> value) {
			this.randomVariable = randomVariable;
			this.value = value;
		}
		
		public RandomVariable getRandomVariable( ) {
			return randomVariable;
		}
		
		public Value<?> getValue() {
			return value;
		}
		
		public Domain getValueSpace() {
			return randomVariable.getValueSpace();
		}
	}
	
	private final List<Conditional> conditionals = new ArrayList<>();
	private final ProbabilityDistribution decoratedPDF;

	public ConditionalProbabilityDistribution(ProbabilityDistribution decoratedPDF, List<Conditional> conditionals) {
		super(decoratedPDF.pdf);
		
		if (conditionals.isEmpty()) {
			throw new ProbabilityDistributionException("Conditionals must not be empty.");
		}
		this.conditionals.addAll(conditionals);
		this.decoratedPDF = decoratedPDF;
	}

	@Override
	public double probability(Value<?> value) {
		return decoratedPDF.probability(value);
	}

	@Override
	public double cumulativeProbability(Value<?> value) {
		return decoratedPDF.cumulativeProbability(value);
	}

	@Override
	public Value<?> getMean() {
		return decoratedPDF.getMean();
	}

	@Override
	public Value<?> getVariance() {
		return decoratedPDF.getVariance();
	}
}
