package tools.mdsd.probdist.api.factory;

import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.entity.Value;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;

public interface ProbabilityDistributionSupplier<I extends Value<?>> {

    public ProbabilityDistributionFunction<I> get(ProbabilityDistribution distribution);

    public ProbabilityDistributionSkeleton getImplementedSkeleton();

}
