package tools.mdsd.probdist.api.entity;

import tools.mdsd.probdist.api.parser.DefaultParameterParser;
import tools.mdsd.probdist.api.parser.ParameterParser;
import tools.mdsd.probdist.model.probdist.distributionfunction.ProbabilityDistributionFunction;
import tools.mdsd.probdist.model.probdist.distributiontype.ProbabilityDistributionSkeleton;

public abstract class ProbabilityDistribution {
	
	protected ParameterParser paramParser;
	protected final ProbabilityDistributionFunction pdf;
	
	public ProbabilityDistribution(ProbabilityDistributionFunction pdf) {
		this.pdf = pdf;
		this.paramParser = new DefaultParameterParser();
	}
	
	public ProbabilityDistributionSkeleton getDistributionSkeleton() {
		return pdf.getInstantiated();
	}
	
	public void setParameterParser(ParameterParser parser) {
		this.paramParser = parser;
	}
	
	public abstract double probability(Value<?> value);

	public abstract double cumulativeProbability(Value<?> value);
	
	public abstract Value<?> getMean();
	
	public abstract Value<?> getVariance();
	
}
