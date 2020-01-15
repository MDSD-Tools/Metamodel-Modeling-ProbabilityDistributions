package tools.mdsd.probdist.api.entity;

import java.util.List;

import tools.mdsd.probdist.api.entity.Conditionable.Conditional;

public interface CPDEvaluator {
	
	public Double evaluate(CategoricalValue value, List<Conditional> conditionals);
	
	public ProbabilityDistributionFunction<CategoricalValue> getCPDGiven(List<Conditional> conditionals);
}
