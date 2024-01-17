package tools.mdsd.probdist.api.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;

public class ProbabilityDistributionFactory<V>
        implements IProbabilityDistributionRegistry<V>, IProbabilityDistributionFactory<V> {

    private final Map<String, ProbabilityDistributionSupplier<V>> registry = new HashMap<>();

    public ProbabilityDistributionFactory() {
    }

    @Override
    public Optional<ProbabilityDistributionFunction<V>> getInstanceOf(ProbabilityDistribution distribution) {
        if (registry.isEmpty()) {
            throw new RuntimeException("registry is empty");
        }

        ProbabilityDistributionSupplier<V> supplier = queryRegister(distribution);
        if (supplier == null) {
            throw new RuntimeException("probability distribution supplier not found");
        }
        return Optional.of(supplier.get(distribution));
    }

    @Override
    public void register(ProbabilityDistributionSupplier<V> supplier) {
        if (isAlreadyRegistered(supplier)) {
            throw new RuntimeException(String.format("supplier %s already registered", supplier.getClass()
                .getName()));
        }
        registry.put(supplier.getImplementedSkeleton()
            .getId(), supplier);
    }

    @Override
    public void unregister(ProbabilityDistributionSupplier<V> supplier) {
        registry.remove(supplier.getImplementedSkeleton()
            .getId());
    }

    private ProbabilityDistributionSupplier<V> queryRegister(ProbabilityDistribution distribution) {
        return registry.get(distribution.getInstantiated()
            .getId());
    }

    private boolean isAlreadyRegistered(ProbabilityDistributionSupplier<V> supplier) {
        return registry.containsKey(supplier.getImplementedSkeleton()
            .getId());
    }

}
