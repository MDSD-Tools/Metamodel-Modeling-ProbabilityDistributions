package tools.mdsd.probdist.api.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import tools.mdsd.probdist.api.entity.Conditionable.Conditional;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.distributionfunction.RandomVariable;

public class TabularCPDRepresentation implements CPDRepresentation<CategoricalValue> {

    public static class TabularCPDBuilder extends CPDRepresentationBuilder<CategoricalValue> {

        private static class TableEntry {
            public List<CategoricalValue> values;
            public ProbabilityDistributionFunction<CategoricalValue> pdf;

            public TableEntry(List<CategoricalValue> values, ProbabilityDistributionFunction<CategoricalValue> pdf) {
                this.values = values;
                this.pdf = pdf;
            }
        }

        private final List<RandomVariable> tableHeader = new ArrayList<>();
        private final List<TableEntry> tableEntries = new ArrayList<>();

        public TabularCPDBuilder addConditionalSignature(RandomVariable... randomVariables) {
            if (randomVariables.length == 0) {
                throw new ProbabilityDistributionException(
                        "The conditional signature has to include at least one random variable.");
            }
            tableHeader.addAll(Arrays.asList(randomVariables));
            return TabularCPDBuilder.this;
        }

        public TabularCPDBuilder addValueEntry(ProbabilityDistributionFunction<CategoricalValue> pdf,
                CategoricalValue values) {
            List<CategoricalValue> orderedValues = Arrays.asList(values);
            if (matchesTableHeader(orderedValues)) {
                tableEntries.add(new TableEntry(orderedValues, pdf));
            }

            throw new ProbabilityDistributionException("The values do not match the signature of the table header.");
        }

        private boolean matchesTableHeader(List<CategoricalValue> orderedValues) {
            if (tableHeader.isEmpty() || tableHeader.size() != orderedValues.size()) {
                return false;
            }

            for (int i = 0; i < orderedValues.size(); i++) {
                if (orderedValues.get(i)
                    .getDomain() != tableHeader.get(i)
                        .getValueSpace()) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public CPDRepresentation<CategoricalValue> build() {
            return new TabularCPDRepresentation(buildConditionalTable());
        }

        private Set<CPDTableEntry> buildConditionalTable() {
            if (tableEntries.isEmpty()) {
                throw new ProbabilityDistributionException("There are no table entries specified.");
            }

            Set<CPDTableEntry> conditionalTable = new LinkedHashSet<>();
            for (TableEntry each : tableEntries) {
                conditionalTable.add(buildConditionalEntry(each));
            }
            return conditionalTable;
        }

        private CPDTableEntry buildConditionalEntry(TableEntry tableEntries) {
            List<Conditional<CategoricalValue>> conditionals = new ArrayList<>();
            for (int i = 0; i < tableEntries.values.size(); i++) {
                conditionals.add(new Conditional<>(tableHeader.get(i), tableEntries.values.get(i)));
            }
            return new CPDTableEntry(conditionals, tableEntries.pdf);
        }

    }

    private static class CPDTableEntry {

        public List<Conditional<CategoricalValue>> orderedConditionals = new ArrayList<>();
        public ProbabilityDistributionFunction<CategoricalValue> pdf;

        public CPDTableEntry(List<Conditional<CategoricalValue>> orderedConditionals,
                ProbabilityDistributionFunction<CategoricalValue> pdf) {
            this.orderedConditionals = orderedConditionals;
            this.pdf = pdf;
        }

        // TODO assuming an ordered set may not be the best solution...
        public boolean matches(List<Conditional<CategoricalValue>> orderedConditionalsToCheck) {
            if (orderedConditionalsToCheck.size() != orderedConditionals.size()) {
                return false;
            }

            for (int i = 0; i < orderedConditionalsToCheck.size(); i++) {
                if (orderedConditionalsToCheck.get(i)
                    .equals(orderedConditionals.get(i)) == false) {
                    return false;
                }
            }

            return true;
        }
    }

    private final Set<CPDTableEntry> conditionalTable = new LinkedHashSet<>();

    private TabularCPDRepresentation(Set<CPDTableEntry> conditionalTable) {
        this.conditionalTable.addAll(conditionalTable);
    }

    @Override
    public Optional<ProbabilityDistributionFunction<CategoricalValue>> getPDFGiven(
            List<Conditional<CategoricalValue>> conditionals) {
        Optional<CPDTableEntry> result = getEntries().filter(entryMatching(conditionals))
            .findFirst();
        if (result.isPresent()) {
            CPDTableEntry cpdTableEntry = result.get();
            return Optional.of(cpdTableEntry.pdf);
        }
        return Optional.empty();
    }

    @Override
    public TabularCPDBuilder builder() {
        return new TabularCPDBuilder();
    }

    private Stream<CPDTableEntry> getEntries() {
        return conditionalTable.stream();
    }

    private Predicate<CPDTableEntry> entryMatching(List<Conditional<CategoricalValue>> conditionals) {
        return entry -> entry.matches(conditionals);
    }
}
