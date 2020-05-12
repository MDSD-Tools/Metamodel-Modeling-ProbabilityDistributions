package tools.mdsd.probdist.api.entity;

import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;

public abstract class ProbabilityDistributionFunction<T> implements ProbabilityMeasure<T>, Sampler<T> {
	
	protected final ProbabilityDistributionSkeleton distSkeleton;
	
	public ProbabilityDistributionFunction(ProbabilityDistributionSkeleton distSkeleton) {
		this.distSkeleton = distSkeleton;
	}
	
	public ProbabilityDistributionSkeleton getDistributionSkeleton() {
		return distSkeleton;
	}
	
}
