package tools.mdsd.probdist.api.factory;

import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;

public interface ProbabilityDistributionSupplier<V> {

    public ProbabilityDistributionFunction<V> get(ProbabilityDistribution distribution);

    public ProbabilityDistributionSkeleton getImplementedSkeleton();

}
