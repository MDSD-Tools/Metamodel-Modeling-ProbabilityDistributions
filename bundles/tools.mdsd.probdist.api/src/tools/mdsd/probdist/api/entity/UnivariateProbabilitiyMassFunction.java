package tools.mdsd.probdist.api.entity;

import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;

public abstract class UnivariateProbabilitiyMassFunction extends ProbabilityDistributionFunction<CategoricalValue> {
	
	public UnivariateProbabilitiyMassFunction(ProbabilityDistributionSkeleton distSkeleton) {
		super(distSkeleton);
	}
	
}
