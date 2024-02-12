package tools.mdsd.probdist.api.builder;

import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.entity.Value;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;

public interface ProbabilityDistributionBuilder<I extends Value<?>> {

    ProbabilityDistributionBuilder<I> withStructure(ProbabilityDistribution distribution);

    ProbabilityDistributionBuilder<I> asConditionalProbabilityDistribution();

    ProbabilityDistributionFunction<I> build();
}
