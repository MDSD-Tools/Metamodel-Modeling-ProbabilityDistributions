package tools.mdsd.probdist.api.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.parser.DefaultParameterParser;
import tools.mdsd.probdist.api.parser.ParameterParser;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;

public class ProbabilityDistributionFactory implements IProbabilityDistributionRegistry, IProbabilityDistributionFactory {

	private static ParameterParser parameterParser = new DefaultParameterParser();
	
	private final Map<String, ProbabilityDistributionSupplier> registry = new HashMap<>();
	
	public ProbabilityDistributionFactory() {
	}
	
	public static ParameterParser getParameterParser() {
		return parameterParser;
	}
	
	public static void setParameterParser(ParameterParser paramParser) {
		parameterParser = paramParser;
	}
	
	@Override
    public Optional<ProbabilityDistributionFunction<?>> getInstanceOf(ProbabilityDistribution distribution) {
		if (registry.isEmpty()) {
			//TODO logging
			return Optional.empty();
		}
		
		ProbabilityDistributionSupplier supplier = queryRegister(distribution);
		if (supplier == null) {
			return Optional.empty();
		}
		return Optional.of(supplier.get(distribution));
	}
	
	@Override
    public void register(ProbabilityDistributionSupplier supplier) {
		if (isNotAlreadyRegistered(supplier)) {
			registry.put(supplier.getImplementedSkeleton().getId(), supplier);
		}
		//TODO logging
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
