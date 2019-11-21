package tools.mdsd.probdist.api.entity;

import java.util.List;
import java.util.Optional;

import tools.mdsd.probdist.api.entity.ConditionalProbabilityDistribution.Conditional;

public interface CPDRepresentation {
	
	public abstract static class CPDRepresentationBuilder {
		
		public abstract CPDRepresentation build();
		
	}
	
	public Optional<ProbabilityDistributionFunction<?>> getPDFGiven(List<Conditional> conditionals);
	
	public CPDRepresentationBuilder builder();
	
}
