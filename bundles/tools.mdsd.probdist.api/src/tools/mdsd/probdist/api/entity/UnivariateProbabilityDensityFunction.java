package tools.mdsd.probdist.api.entity;

import tools.mdsd.probdist.model.probdist.distributiontype.ProbabilityDistributionSkeleton;

public abstract class UnivariateProbabilityDensityFunction extends ProbabilityDistributionFunction<NumericalValue> {

	public UnivariateProbabilityDensityFunction(ProbabilityDistributionSkeleton distSkeleton) {
		super(distSkeleton);
	}
	
}
