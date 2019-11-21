package tools.mdsd.probdist.api.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import tools.mdsd.probdist.api.entity.ConditionalProbabilityDistribution.Conditional;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.api.factory.ProbabilityDistributionFactory;
import tools.mdsd.probdist.model.probdist.distributionfunction.Parameter;
import tools.mdsd.probdist.model.probdist.distributionfunction.ProbabilityDistribution;
import tools.mdsd.probdist.model.probdist.distributionfunction.TabularCPD;
import tools.mdsd.probdist.model.probdist.distributionfunction.TabularCPDEntry;

public class TabularCPDEvaluator implements CPDEvaluator {

	private final Map<TabularCPDEntry, UnivariateProbabilitiyMassFunction> entryToPMF = new HashMap<>();

	public TabularCPDEvaluator(TabularCPD tabularCPD, ProbabilityDistribution distribution) {
		initPMFEntries(tabularCPD, distribution);
	}

	private void initPMFEntries(TabularCPD tabularCPD, ProbabilityDistribution distribution) {
		for (TabularCPDEntry each : tabularCPD.getCpdEntries()) {
			entryToPMF.put(each, createPMFRealisation(distribution, each));
		}
	}

	private UnivariateProbabilitiyMassFunction createPMFRealisation(ProbabilityDistribution distribution,
			TabularCPDEntry cpdEntry) {
		setParamRepresentation(distribution, cpdEntry);
		return (UnivariateProbabilitiyMassFunction) ProbabilityDistributionFactory.get().getInstanceOf(distribution)
				.orElseThrow(() -> new ProbabilityDistributionException(String.format(
						"There is no realisation for the PDF: %s", distribution.getInstantiated().getEntityName())));
	}

	private void setParamRepresentation(ProbabilityDistribution distribution, TabularCPDEntry cpdEntry) {
		if (distribution.getParams().size() == 1) {
			throw new ProbabilityDistributionException("Only a single parameter realisation may be defined.");
		}
		Parameter param = distribution.getParams().get(0);
		param.setRepresentation(cpdEntry.getEntry());
	}

	@Override
	public Double evaluate(CategoricalValue value, List<Conditional> conditionals) {
		return findCPDEntryMatching(conditionals).probability(value);
	}

	@Override
	public ProbabilityDistributionFunction<CategoricalValue> getCPDGiven(List<Conditional> conditionals) {
		return findCPDEntryMatching(conditionals);
	}

	private UnivariateProbabilitiyMassFunction findCPDEntryMatching(List<Conditional> conditionals) {
		return (UnivariateProbabilitiyMassFunction) entryToPMF.keySet().stream().filter(entryMatching(conditionals))
				.findFirst().orElseThrow(() -> new ProbabilityDistributionException(
						"The specified conditionals are not included in the CPD table."));
	}

	private Predicate<TabularCPDEntry> entryMatching(List<Conditional> queriedConditionals) {
		return e -> {
			List<Conditional> entryConditionals = toCPDConditionals(e.getConditonals());
			if (entryConditionals.size() != queriedConditionals.size()) {
				throw new IllegalArgumentException(
						"The number of queried conditionals do not match the size of the tabular conditionals");
			}

			for (Conditional each : queriedConditionals) {
				entryConditionals.removeIf(c -> each.getValue().equals(c.getValue()));
			}
			return entryConditionals.isEmpty();
		};
	}

	private List<Conditional> toCPDConditionals(List<String> conditonals) {
		return conditonals.stream().map(each -> new Conditional(null, CategoricalValue.create(each)))
				.collect(Collectors.toList());
	}
}
