package tools.mdsd.probdist.api.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;

public class ProbabilityDistributionFactory implements IProbabilityDistributionRegistry, IProbabilityDistributionFactory {

	private final Map<String, ProbabilityDistributionSupplier> registry = new HashMap<>();
	
	public ProbabilityDistributionFactory() { }

	
	@Override
    public Optional<ProbabilityDistributionFunction<?>> getInstanceOf(ProbabilityDistribution distribution) {
		if (registry.isEmpty()) {
		    throw new RuntimeException("registry is empty");
		}
		
		ProbabilityDistributionSupplier supplier = queryRegister(distribution);
		if (supplier == null) {
		    throw new RuntimeException("probability distribution supplier not found");
		}
		return Optional.of(supplier.get(distribution));
	}
	
	@Override
    public void register(ProbabilityDistributionSupplier supplier) {
	    // lgic invertieren + namen ändern: isAlreadyRegistered -> throw 
		if (isNotAlreadyRegistered(supplier)) {
			registry.put(supplier.getImplementedSkeleton().getId(), supplier);
		}
		//TODO logging: supplier already registered
	}
	
	@Override
    public void unregister(ProbabilityDistributionSupplier supplier) {
		registry.remove(supplier.getImplementedSkeleton().getId());
	}
	
	private ProbabilityDistributionSupplier queryRegister(ProbabilityDistribution distribution) {
		return registry.get(distribution.getInstantiated().getId());
	}
	
	private boolean isNotAlreadyRegistered(ProbabilityDistributionSupplier supplier) {
		return !registry.containsKey(supplier.getImplementedSkeleton().getId());
	}
	
}
