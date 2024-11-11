package tools.mdsd.probdist.api.apache.entity.impl;

import static tools.mdsd.probdist.api.apache.util.ValueUtil.asDoubleArray;
import static tools.mdsd.probdist.api.apache.util.ValueUtil.toValueList;

import java.util.List;

import tools.mdsd.probdist.api.entity.MultivariateProbabilityDensityFunction;
import tools.mdsd.probdist.api.entity.NumericalValue;
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
    public void init(int seed) {
        normalDistribution.reseedRandomGenerator(seed);
    }

    @Override
    public List<NumericalValue> sample() {
        return toValueList(normalDistribution.sample(), v -> NumericalValue.create(v));
    }

}
