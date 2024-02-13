package tools.mdsd.probdist.api.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import tools.mdsd.probdist.api.entity.Conditionable.Conditional;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.api.factory.IProbabilityDistributionFactory;
import tools.mdsd.probdist.distributionfunction.DistributionfunctionFactory;
import tools.mdsd.probdist.distributionfunction.Domain;
import tools.mdsd.probdist.distributionfunction.Parameter;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;
import tools.mdsd.probdist.distributionfunction.SimpleParameter;
import tools.mdsd.probdist.distributionfunction.TabularCPD;
import tools.mdsd.probdist.distributionfunction.TabularCPDEntry;

public class TabularCPDEvaluator implements CPDEvaluator {

    private final Map<TabularCPDEntry, UnivariateProbabilitiyMassFunction> entryToPMF = new HashMap<>();
    private final IProbabilityDistributionFactory<CategoricalValue> probabilityDistributionFactory;

    public TabularCPDEvaluator(TabularCPD tabularCPD, ProbabilityDistribution distribution,
            IProbabilityDistributionFactory<CategoricalValue> probabilityDistributionFactory) {
        this.probabilityDistributionFactory = probabilityDistributionFactory;
        initPMFEntries(tabularCPD, distribution);
    }

    private void initPMFEntries(TabularCPD tabularCPD, ProbabilityDistribution distribution) {
        for (TabularCPDEntry each : tabularCPD.getCpdEntries()) {
            entryToPMF.put(each, createPMFRealisation(distribution, each));
        }
    }

    private UnivariateProbabilitiyMassFunction createPMFRealisation(ProbabilityDistribution distribution,
            TabularCPDEntry cpdEntry) {
        ProbabilityDistribution pmfEntry = createPMFEntry(distribution);
        setParamRepresentation(pmfEntry, cpdEntry);
        return getPMFRealisation(distribution, pmfEntry);
    }

    private UnivariateProbabilitiyMassFunction getPMFRealisation(ProbabilityDistribution distribution,
            ProbabilityDistribution pmfEntry) {
        return (UnivariateProbabilitiyMassFunction) probabilityDistributionFactory.getInstanceOf(pmfEntry)
            .orElseThrow(() -> new ProbabilityDistributionException(
                    String.format("There is no realisation for the PDF: %s", distribution.getInstantiated()
                        .getEntityName())));
    }

    private ProbabilityDistribution createPMFEntry(ProbabilityDistribution distribution) {
        ProbabilityDistribution pmfEntry = createPMFStructure(distribution);
        Parameter param = createParam(distribution);

        pmfEntry.getParams()
            .add(param);

        return pmfEntry;
    }

    private ProbabilityDistribution createPMFStructure(ProbabilityDistribution distribution) {
        DistributionfunctionFactory factory = DistributionfunctionFactory.eINSTANCE;
        ProbabilityDistribution structure = factory.createProbabilityDistribution();
        structure.getRandomVariables()
            .addAll(distribution.getRandomVariables());
        structure.setInstantiated(distribution.getInstantiated());
        return structure;
    }

    private Parameter createParam(ProbabilityDistribution distribution) {
        DistributionfunctionFactory factory = DistributionfunctionFactory.eINSTANCE;
        Parameter param = factory.createParameter();
        param.setInstantiated(distribution.getParams()
            .get(0)
            .getInstantiated());
        return param;
    }

    private void setParamRepresentation(ProbabilityDistribution pmfEntry, TabularCPDEntry cpdEntry) {
        SimpleParameter sParam = DistributionfunctionFactory.eINSTANCE.createSimpleParameter();
        sParam.setType(cpdEntry.getEntry()
            .getType());
        sParam.setValue(cpdEntry.getEntry()
            .getValue());

        Parameter param = pmfEntry.getParams()
            .get(0);
        param.setRepresentation(sParam);
    }

    @Override
    public Double evaluate(CategoricalValue value, List<Conditional<CategoricalValue>> conditionals) {
        return getCPDGiven(conditionals).probability(value);
    }

    @Override
    public ProbabilityDistributionFunction<CategoricalValue> getCPDGiven(
            List<Conditional<CategoricalValue>> conditionals) {
        return entryToPMF.get(findCPDEntryMatching(conditionals));
    }

    private TabularCPDEntry findCPDEntryMatching(List<Conditional<CategoricalValue>> conditionals) {
        return entryToPMF.keySet()
            .stream()
            .filter(entryMatching(conditionals))
            .findFirst()
            .orElseThrow(() -> new ProbabilityDistributionException(
                    String.format("The conditionals %1s are not included in the CPD table with %2s.",
                            toString(conditionals), toString(entryToPMF.keySet()))));
    }

    private Predicate<TabularCPDEntry> entryMatching(List<Conditional<CategoricalValue>> queriedConditionals) {
        return e -> {
            List<Conditional<CategoricalValue>> entryConditionals = toCPDConditionals(e.getConditonals());
            if (entryConditionals.size() != queriedConditionals.size()) {
                throw new IllegalArgumentException(
                        "The number of queried conditionals do not match the size of the tabular conditionals");
            }

            for (Conditional<CategoricalValue> each : queriedConditionals) {
                entryConditionals.removeIf(isEqualTo(each));
            }
            return entryConditionals.isEmpty();
        };
    }

    private Predicate<Conditional<CategoricalValue>> isEqualTo(Conditional<CategoricalValue> other) {
        return given -> {
            if (given.getValueSpace() != other.getValueSpace()) {
                return false;
            }
            return given.getValue().value.equals(other.getValue().value);
        };
    }

    private List<Conditional<CategoricalValue>> toCPDConditionals(List<String> conditonals) {
        return conditonals.stream()
            .map(each -> new Conditional<>(Domain.CATEGORY, CategoricalValue.create(each)))
            .collect(Collectors.toList());
    }

    private String toString(List<Conditional<CategoricalValue>> conditionals) {
        if (conditionals.size() == 1) {
            return conditionals.get(0)
                .getValue()
                .toString();
        }
        StringBuilder builder = new StringBuilder();
        for (Conditional<CategoricalValue> each : conditionals) {
            builder.append(String.format(",%s", each.getValue()
                .toString()));
        }
        return builder.toString()
            .replaceFirst(",", "");
    }

    private String toString(Set<TabularCPDEntry> entries) {
        List<TabularCPDEntry> entryList = Lists.newArrayList(entries);
        if (entryList.size() == 1) {
            return entryList.get(0)
                .getEntry()
                .getValue();
        }
        StringBuilder builder = new StringBuilder();
        for (TabularCPDEntry each : entryList) {
            builder.append(String.format(",%s", each.getEntry()
                .getValue()
                .toString()));
        }
        return builder.toString()
            .replaceFirst(",", "");
    }
}
