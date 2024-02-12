package tools.mdsd.probdist.api.factory;

import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.entity.Value;

public interface ProbabilityCalculator<I extends Value<?>> {
    double calculateLocalProbability(ProbabilityDistributionFunction<I> pdf, I value);
}
