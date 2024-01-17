package tools.mdsd.probdist.api.factory;

public interface IProbabilityDistributionRegistry<V> {

    void register(ProbabilityDistributionSupplier<V> supplier);

    void unregister(ProbabilityDistributionSupplier<V> supplier);

}
