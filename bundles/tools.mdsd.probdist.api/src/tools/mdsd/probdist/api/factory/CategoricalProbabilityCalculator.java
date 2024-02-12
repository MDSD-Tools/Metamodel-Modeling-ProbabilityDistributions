package tools.mdsd.probdist.api.factory;

import tools.mdsd.probdist.api.entity.CategoricalValue;
import tools.mdsd.probdist.api.entity.ConditionalProbabilityDistribution;
import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.entity.UnivariateProbabilitiyMassFunction;

public class CategoricalProbabilityCalculator implements ProbabilityCalculator<CategoricalValue> {

    @Override
    public double calculateLocalProbability(ProbabilityDistributionFunction<CategoricalValue> pdf,
            CategoricalValue value) {
        if (UnivariateProbabilitiyMassFunction.class.isInstance(pdf)) {
            return UnivariateProbabilitiyMassFunction.class.cast(pdf)
                .probability(value);
        }
        return ConditionalProbabilityDistribution.class.cast(pdf)
            .probability(value);
    }

}
