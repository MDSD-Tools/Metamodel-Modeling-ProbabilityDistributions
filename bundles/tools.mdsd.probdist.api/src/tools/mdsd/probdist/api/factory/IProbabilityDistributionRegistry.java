package tools.mdsd.probdist.api.factory;

public interface IProbabilityDistributionRegistry {
    
    void register(ProbabilityDistributionSupplier supplier);

    void unregister(ProbabilityDistributionSupplier supplier);
    

}
