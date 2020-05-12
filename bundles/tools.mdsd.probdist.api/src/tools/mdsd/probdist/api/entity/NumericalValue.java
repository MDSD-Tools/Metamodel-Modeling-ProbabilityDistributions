package tools.mdsd.probdist.api.entity;

import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.distributionfunction.Domain;

public class NumericalValue extends Value<Number> {

	private NumericalValue(Number value, Domain domain) {
		super(value, domain);
	}

	public static NumericalValue create(Number value, Domain domain) {
		return new NumericalValue(value, domain);
	}

	public static NumericalValue create(Double value) {
		return new NumericalValue(value, Domain.REAL);
	}

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

	@Override
	public boolean equals(Object other) {
		if (other instanceof NumericalValue) {
			NumericalValue otherVal = (NumericalValue) other;
			return domain == otherVal.getDomain() && value.equals(otherVal.value);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return value.toString(); 
	}
}
