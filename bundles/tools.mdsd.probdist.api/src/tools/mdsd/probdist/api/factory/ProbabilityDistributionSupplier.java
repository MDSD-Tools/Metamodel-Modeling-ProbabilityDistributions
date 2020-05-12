package tools.mdsd.probdist.api.factory;

import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;

public interface ProbabilityDistributionSupplier {
	
	public ProbabilityDistributionFunction<?> get(ProbabilityDistribution distribution);
	
	public ProbabilityDistributionSkeleton getImplementedSkeleton();
	
}
