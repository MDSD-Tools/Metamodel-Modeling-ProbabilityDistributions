package tools.mdsd.probdist.api.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tools.mdsd.probdist.api.builder.CategoricalProbabilityDistributionBuilder;
import tools.mdsd.probdist.api.builder.ProbabilityDistributionBuilder;
import tools.mdsd.probdist.api.entity.CategoricalValue;
import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;

public class ProbabilityDistributionFactory implements IProbabilityDistributionRegistry<CategoricalValue>,
        IProbabilityDistributionFactory<CategoricalValue> {

    private final Map<String, ProbabilityDistributionSupplier<CategoricalValue>> registry = new HashMap<>();

    public ProbabilityDistributionFactory() {
    }

    @Override
    public Optional<ProbabilityDistributionFunction<CategoricalValue>> getInstanceOf(
            ProbabilityDistribution distribution) {
        if (registry.isEmpty()) {
            throw new RuntimeException("registry is empty");
        }

        ProbabilityDistributionSupplier<CategoricalValue> supplier = queryRegister(distribution);
        if (supplier == null) {
            throw new RuntimeException("probability distribution supplier not found");
        }
        return Optional.of(supplier.get(distribution));
    }

    @Override
    public void register(ProbabilityDistributionSupplier<CategoricalValue> supplier) {
        if (isAlreadyRegistered(supplier)) {
            throw new RuntimeException(String.format("supplier %s already registered", supplier.getClass()
                .getName()));
        }
        registry.put(supplier.getImplementedSkeleton()
            .getId(), supplier);
    }

    @Override
    public void unregister(ProbabilityDistributionSupplier<CategoricalValue> supplier) {
        registry.remove(supplier.getImplementedSkeleton()
            .getId());
    }

    private ProbabilityDistributionSupplier<CategoricalValue> queryRegister(ProbabilityDistribution distribution) {
        return registry.get(distribution.getInstantiated()
            .getId());
    }

    private boolean isAlreadyRegistered(ProbabilityDistributionSupplier<CategoricalValue> supplier) {
        return registry.containsKey(supplier.getImplementedSkeleton()
            .getId());
    }

    @Override
    public ProbabilityCalculator<CategoricalValue> getProbabilityCalculator() {
        return new CategoricalProbabilityCalculator();
    }

    @Override
    public ProbabilityDistributionBuilder<CategoricalValue> getProbabilityDistributionBuilder() {
        return CategoricalProbabilityDistributionBuilder.create(this);
    }

}
