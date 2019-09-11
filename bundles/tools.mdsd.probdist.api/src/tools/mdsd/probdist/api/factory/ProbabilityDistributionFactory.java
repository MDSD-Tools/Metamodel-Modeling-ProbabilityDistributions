package tools.mdsd.probdist.api.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.api.parser.DefaultParameterParser;
import tools.mdsd.probdist.api.parser.ParameterParser;
import tools.mdsd.probdist.model.probdist.distributionfunction.ProbabilityDistribution;

public class ProbabilityDistributionFactory {

	private final static ProbabilityDistributionFactory factoryInstance = new ProbabilityDistributionFactory();

	private static ParameterParser parameterParser = new DefaultParameterParser();
	
	private final Map<String, ProbabilityDistributionSupplier> registry = new HashMap<>();
	
	private ProbabilityDistributionFactory() {
		
	}
	
	public static ParameterParser getParameterParser() {
		return parameterParser;
	}
	
	public static void setParameterParser(ParameterParser paramParser) {
		parameterParser = paramParser;
	}
	
	public static ProbabilityDistributionFactory get() {
		if (factoryInstance.registry.isEmpty()) {
			throw new ProbabilityDistributionException("There are no registered distribution realisations.");
		}
		return factoryInstance;
	}
	
	public Optional<ProbabilityDistributionFunction<?>> getInstanceOf(ProbabilityDistribution distribution) {
		ProbabilityDistributionSupplier supplier = queryRegister(distribution);
		if (supplier == null) {
			return Optional.empty();
		}
		return Optional.of(supplier.get(distribution));
	}
	
	public void register(ProbabilityDistributionSupplier supplier) {
		if (isNotAlreadyRegistered(supplier)) {
			registry.put(supplier.getImplementedSkeleton().getId(), supplier);
		}
		//TODO logging
	}
	
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
