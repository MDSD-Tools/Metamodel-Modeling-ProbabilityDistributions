package tools.mdsd.probdist.api.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tools.mdsd.probdist.api.entity.ProbabilityDistribution;
import tools.mdsd.probdist.model.probdist.distributionfunction.ProbabilityDistributionFunction;

public class ProbabilityDistributionRegistry {

	private final static ProbabilityDistributionRegistry registryInstance = new ProbabilityDistributionRegistry();
	
	private final Map<String, ProbabilityDistribution> registry = new HashMap<>();
	
	private ProbabilityDistributionRegistry() {
		
	}
	
	public static ProbabilityDistributionRegistry get() {
		if (registryInstance.registry.isEmpty()) {
			registryInstance.loadDefaults();
		}
		return registryInstance;
	}
	
	public Optional<ProbabilityDistribution> getRealisationFor(ProbabilityDistributionFunction pdf) {
		String id = pdf.getInstantiated().getId();
		return Optional.ofNullable(registry.get(id));
	}
	
	public void register(ProbabilityDistribution probDist) {
		String id = probDist.getDistributionSkeleton().getId();
		if (isNotAlreadyRegistered(id)) {
			registry.put(id, probDist);
		}
		//TODO logging
	}
	
	public void unregister(ProbabilityDistribution probDist) {
		registry.remove(probDist.getDistributionSkeleton().getId());
	}
	
	private boolean isNotAlreadyRegistered(String id) {
		return !registry.containsKey(id);
	}
	
	private void loadDefaults() {
		
	}
	
}
