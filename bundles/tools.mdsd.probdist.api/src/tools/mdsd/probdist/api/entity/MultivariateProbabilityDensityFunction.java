package tools.mdsd.probdist.api.entity;

import java.util.List;

import tools.mdsd.probdist.model.probdist.distributiontype.ProbabilityDistributionSkeleton;

public abstract class MultivariateProbabilityDensityFunction
		extends ProbabilityDistributionFunction<List<NumericalValue>> {

	public MultivariateProbabilityDensityFunction(ProbabilityDistributionSkeleton distSkeleton) {
		super(distSkeleton);
	}

}
