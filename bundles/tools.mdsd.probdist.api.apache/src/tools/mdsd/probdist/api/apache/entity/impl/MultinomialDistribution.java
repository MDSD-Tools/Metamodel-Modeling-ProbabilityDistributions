package tools.mdsd.probdist.api.apache.entity.impl;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import tools.mdsd.probdist.api.entity.CategoricalValue;
import tools.mdsd.probdist.api.entity.UnivariateProbabilitiyMassFunction;
import tools.mdsd.probdist.api.entity.Value;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.api.random.ISeedProvider;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;

public class MultinomialDistribution extends UnivariateProbabilitiyMassFunction {

    private final EnumeratedDistribution<CategoricalValue> multDistribution;

    public MultinomialDistribution(ProbabilityDistributionSkeleton distSkeleton,
            EnumeratedDistribution<CategoricalValue> multDistribution) {
        super(distSkeleton);
        this.multDistribution = multDistribution;
    }

    @Override
    public Double probability(CategoricalValue value) {
        return findPairWith(value).getValue();
    }

    @Override
    public void init(Optional<ISeedProvider> seedProvider) {
        if (initialized) {
            return;
        }
        initialized = true;
        seedProvider.ifPresent(sp -> multDistribution.reseedRandomGenerator(sp.getLong()));
    }

    @Override
    public CategoricalValue sample() {
        if (!initialized) {
            throw new RuntimeException("not initialized");
        }
        return multDistribution.sample();
    }

    private Pair<CategoricalValue, Double> findPairWith(Value<?> value) {
        return getSampleSpace().filter(s -> withSameValue(s.getKey(), value))
            .findFirst()
            .orElseThrow(() -> new ProbabilityDistributionException(
                    String.format("There is no sample with value: %s", value.get())));
    }

    private boolean withSameValue(CategoricalValue cValue, Value<?> value) {
        if (CategoricalValue.class.isInstance(value) == false) {
            return false;
        }
        return CategoricalValue.class.cast(value)
            .get()
            .equals(cValue.get());
    }

    private Stream<Pair<CategoricalValue, Double>> getSampleSpace() {
        return multDistribution.getPmf()
            .stream();
    }

}
