package tools.mdsd.probdist.api.entity;

import java.util.ArrayList;
import java.util.List;

import tools.mdsd.probdist.api.factory.IProbabilityDistributionFactory;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;
import tools.mdsd.probdist.distributionfunction.TabularCPD;

public class ConditionalProbabilityDistribution extends ProbabilityDistributionFunction<CategoricalValue>
        implements Conditionable<ConditionalProbabilityDistribution, CategoricalValue> {

    private final CPDEvaluator cpdEvaluator;
    private final List<Conditional<CategoricalValue>> conditionals;

    public ConditionalProbabilityDistribution(ProbabilityDistribution distribution, TabularCPD tabularCPD,
            IProbabilityDistributionFactory<CategoricalValue> probabilityDistributionFactory) {
        super(distribution.getInstantiated());

        this.cpdEvaluator = new TabularCPDEvaluator(tabularCPD, distribution, probabilityDistributionFactory);
        this.conditionals = new ArrayList<>();
    }

    @Override
    public Double probability(CategoricalValue value) {
        return cpdEvaluator.evaluate(value, conditionals);
    }

    @Override
    public CategoricalValue sample() {
        return cpdEvaluator.getCPDGiven(conditionals)
            .sample();
    }

    @Override
    public ConditionalProbabilityDistribution given(List<Conditional<CategoricalValue>> conditionals) {
        this.conditionals.clear();
        this.conditionals.addAll(conditionals);
        return this;
    }

}
