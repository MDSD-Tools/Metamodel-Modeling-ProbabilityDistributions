package tools.mdsd.probdist.api.factory;

import tools.mdsd.probdist.api.entity.NumericalValue;
import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.entity.UnivariateProbabilityDensityFunction;

public class NumericalProbabilityCalculator implements ProbabilityCalculator<NumericalValue> {

    @Override
    public double calculateLocalProbability(ProbabilityDistributionFunction<NumericalValue> pdf, NumericalValue value) {
        return UnivariateProbabilityDensityFunction.class.cast(pdf)
            .probability(value);
    }

}
