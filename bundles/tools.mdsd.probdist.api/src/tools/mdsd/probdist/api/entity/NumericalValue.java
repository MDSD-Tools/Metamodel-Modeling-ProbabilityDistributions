package tools.mdsd.probdist.api.entity;

import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.model.probdist.distributionfunction.Domain;

public class NumericalValue extends Value<Number> {

	public Integer asNatural() {
		if (isNoNatural()) {
			throw new ProbabilityDistributionException("The value is not a natural number.");
		}
		return value.intValue();
	}

	public Integer asInteger() {
		if (isRealNumber()) {
			throw new ProbabilityDistributionException("The value is not a integer number.");
		}
		return value.intValue();
	}
	
	public Double asReal() {
		return value.doubleValue();
	}
	
	private boolean isNoNatural() {
		return value.intValue() < 0 && domain == Domain.NATURAL;
	}
	
	private boolean isRealNumber() {
		return domain == Domain.REAL;
	}
}
