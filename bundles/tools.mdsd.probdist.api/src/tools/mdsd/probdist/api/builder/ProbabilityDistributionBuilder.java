package tools.mdsd.probdist.api.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import tools.mdsd.probdist.api.entity.ConditionalProbabilityDistribution;
import tools.mdsd.probdist.api.entity.ProbabilityDistribution;
import tools.mdsd.probdist.api.entity.Value;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.api.parser.ParameterParser;
import tools.mdsd.probdist.api.registry.ProbabilityDistributionRegistry;
import tools.mdsd.probdist.model.probdist.distributionfunction.ProbabilityDistributionFunction;
import tools.mdsd.probdist.model.probdist.distributionfunction.RandomVariable;

public class ProbabilityDistributionBuilder {

	private ProbabilityDistributionFunction pdf = null;
	private Optional<ParameterParser> parser = Optional.empty();
	private List<ConditionalProbabilityDistribution.Conditional> conditionals = new ArrayList<>();
	
	private ProbabilityDistributionBuilder() {
		
	}
	
	public static ProbabilityDistributionBuilder create() {
		return new ProbabilityDistributionBuilder();
	}
	
	public ProbabilityDistributionBuilder withStructure(ProbabilityDistributionFunction pdf) {
		this.pdf = pdf;
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
	
	public ProbabilityDistribution build() {
		Objects.requireNonNull(pdf, "There need to be a probability distribution function.");
		
		ProbabilityDistribution realisation = getRealisation();
		realisation = setOptionalParameter(realisation);
		realisation = setConditionalsIfAvailabe(realisation);
		return realisation;
	}

	private ProbabilityDistribution getRealisation() {
		return ProbabilityDistributionRegistry.get().getRealisationFor(pdf)
				.orElseThrow(() -> new ProbabilityDistributionException(String.format("There is no realisation for the PDF: %s", pdf.getInstantiated().getEntityName())));
	}
	
	private ProbabilityDistribution setOptionalParameter(ProbabilityDistribution realisation) {
		parser.ifPresent(parser -> realisation.setParameterParser(parser));
		return realisation;
	}

	private ProbabilityDistribution setConditionalsIfAvailabe(ProbabilityDistribution realisation) {
		if (conditionals.isEmpty() == false) {
			realisation = new ConditionalProbabilityDistribution(realisation, conditionals);
		}
		return realisation;
	}
	
}
