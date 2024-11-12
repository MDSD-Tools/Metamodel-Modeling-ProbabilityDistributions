package tools.mdsd.probdist.api.entity;

import java.util.List;

import tools.mdsd.probdist.api.entity.Conditionable.Conditional;

public interface CPDEvaluator {
    void init(int seed);

    public Double evaluate(CategoricalValue value, List<Conditional<CategoricalValue>> conditionals);

    public ProbabilityDistributionFunction<CategoricalValue> getCPDGiven(
            List<Conditional<CategoricalValue>> conditionals);

}
