package tools.mdsd.probdist.api.factory;

import java.util.Optional;

import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.entity.Value;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;

public interface IProbabilityDistributionFactory<I extends Value<?>> {

    Optional<ProbabilityDistributionFunction<I>> getInstanceOf(ProbabilityDistribution distribution);

}
