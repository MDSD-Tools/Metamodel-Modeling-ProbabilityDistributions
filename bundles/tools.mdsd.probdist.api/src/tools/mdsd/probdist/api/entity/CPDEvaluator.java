package tools.mdsd.probdist.api.entity;

import java.util.List;

import tools.mdsd.probdist.api.entity.Conditionable.Conditional;
import tools.mdsd.probdist.api.random.ISeedable;

public interface CPDEvaluator extends ISeedable {
    public Double evaluate(CategoricalValue value, List<Conditional<CategoricalValue>> conditionals);

    public ProbabilityDistributionFunction<CategoricalValue> getCPDGiven(
            List<Conditional<CategoricalValue>> conditionals);

}
