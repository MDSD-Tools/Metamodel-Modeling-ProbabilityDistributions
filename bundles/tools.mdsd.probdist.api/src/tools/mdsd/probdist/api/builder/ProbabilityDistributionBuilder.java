package tools.mdsd.probdist.api.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import tools.mdsd.probdist.api.entity.ConditionalProbabilityDistribution;
import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.entity.Value;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.api.factory.ProbabilityDistributionFactory;
import tools.mdsd.probdist.api.parser.ParameterParser;
import tools.mdsd.probdist.model.probdist.distributionfunction.ProbabilityDistribution;
import tools.mdsd.probdist.model.probdist.distributionfunction.RandomVariable;

public class ProbabilityDistributionBuilder {
	private ProbabilityDistribution distribution = null;
	private Optional<ParameterParser> parser = Optional.empty();
	private List<ConditionalProbabilityDistribution.Conditional> conditionals = new ArrayList<>();
	
	private ProbabilityDistributionBuilder() {
		
	}
	
	public static ProbabilityDistributionBuilder create() {
		return new ProbabilityDistributionBuilder();
	}
	
	public ProbabilityDistributionBuilder withStructure(ProbabilityDistribution distribution) {
		this.distribution = distribution;
		return this;
	}
	
	public ProbabilityDistributionBuilder andConditional(RandomVariable randomVariable, Value<?> value) {
		conditionals.add(new ConditionalProbabilityDistribution.Conditional(randomVariable, value));
		return this;
	}
	
	public ProbabilityDistributionBuilder andOptionalParser(ParameterParser parser) {
		this.parser = Optional.of(parser);
		return this;
	}
	
	public ProbabilityDistributionFunction build() {
		Objects.requireNonNull(distribution, "There need to be a probability distribution function.");
		
		ProbabilityDistributionFunction realisation = queryRealisation();
		realisation = setOptionalParameter(realisation);
		realisation = setConditionalsIfAvailable(realisation);
		return realisation;
	}

	private ProbabilityDistributionFunction queryRealisation() {
		return ProbabilityDistributionFactory.get().getInstanceOf(distribution)
				.orElseThrow(() -> new ProbabilityDistributionException(String.format("There is no realisation for the PDF: %s", distribution.getInstantiated().getEntityName())));
	}
	
	private ProbabilityDistributionFunction setOptionalParameter(ProbabilityDistributionFunction realisation) {
		parser.ifPresent(parser -> realisation.setParameterParser(parser));
		return realisation;
	}

	private ProbabilityDistributionFunction setConditionalsIfAvailable(ProbabilityDistributionFunction realisation) {
		if (conditionals.isEmpty() == false) {
			realisation = new ConditionalProbabilityDistribution(realisation, conditionals);
		}
		return realisation;
	}
	
}
