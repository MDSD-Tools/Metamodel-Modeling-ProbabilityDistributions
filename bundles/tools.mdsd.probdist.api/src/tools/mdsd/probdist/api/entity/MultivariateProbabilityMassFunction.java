package tools.mdsd.probdist.api.entity;

import java.util.List;

import tools.mdsd.probdist.model.probdist.distributiontype.ProbabilityDistributionSkeleton;

public abstract class MultivariateProbabilityMassFunction extends ProbabilityDistributionFunction<List<CategoricalValue>> {

	public MultivariateProbabilityMassFunction(ProbabilityDistributionSkeleton distSkeleton) {
		super(distSkeleton);
	}

}
