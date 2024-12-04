package tools.mdsd.probdist.api.apache.entity.impl;

import static tools.mdsd.probdist.api.apache.util.ValueUtil.asDoubleArray;
import static tools.mdsd.probdist.api.apache.util.ValueUtil.toValueList;

import java.util.List;
import java.util.Optional;

import tools.mdsd.probdist.api.entity.MultivariateProbabilityDensityFunction;
import tools.mdsd.probdist.api.entity.NumericalValue;
import tools.mdsd.probdist.api.random.ISeedProvider;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;

public class MultivariateNormalDistribution extends MultivariateProbabilityDensityFunction {

    private final org.apache.commons.math3.distribution.MultivariateNormalDistribution normalDistribution;

    public MultivariateNormalDistribution(ProbabilityDistributionSkeleton distSkeleton,
            org.apache.commons.math3.distribution.MultivariateNormalDistribution normalDistribution) {
        super(distSkeleton);
        this.normalDistribution = normalDistribution;
    }

    @Override
    public Double probability(List<NumericalValue> value) {
        return normalDistribution.density(asDoubleArray(value));
    }

    @Override
    public void init(Optional<ISeedProvider> seedProvider) {
        if (initialized) {
            throw new RuntimeException("initialized");
        }
        initialized = true;
        seedProvider.ifPresent(sp -> normalDistribution.reseedRandomGenerator(sp.getLong()));
    }

    @Override
    public List<NumericalValue> sample() {
        if (!initialized) {
            throw new RuntimeException("not initialized");
        }
        return toValueList(normalDistribution.sample(), v -> NumericalValue.create(v));
    }

}
