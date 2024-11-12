package tools.mdsd.probdist.api.apache.entity.impl;

import org.apache.commons.math3.distribution.NormalDistribution;

import tools.mdsd.probdist.api.entity.NumericalValue;
import tools.mdsd.probdist.api.entity.UnivariateProbabilityDensityFunction;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;

public class UnivariateNormalDistribution extends UnivariateProbabilityDensityFunction {

    private final NormalDistribution normalDist;

    public UnivariateNormalDistribution(ProbabilityDistributionSkeleton distSkeleton, NormalDistribution normalDist) {
        super(distSkeleton);
        this.normalDist = normalDist;
    }

    @Override
    public Double probability(NumericalValue value) {
        return normalDist.density(value.asReal());
    }

    @Override
    public void init(int seed) {
        if (initialized) {
            throw new RuntimeException("initialized");
        }
        initialized = true;
        normalDist.reseedRandomGenerator(seed);
    }

    @Override
    public NumericalValue sample() {
        if (!initialized) {
            throw new RuntimeException("not initialized");
        }
        return NumericalValue.create(normalDist.sample());
    }

}
