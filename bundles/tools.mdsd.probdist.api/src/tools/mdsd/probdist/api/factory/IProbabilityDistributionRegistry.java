package tools.mdsd.probdist.api.factory;

import tools.mdsd.probdist.api.entity.Value;

public interface IProbabilityDistributionRegistry<I extends Value<?>> {

    void register(ProbabilityDistributionSupplier<I> supplier);

    void unregister(ProbabilityDistributionSupplier<I> supplier);

}
