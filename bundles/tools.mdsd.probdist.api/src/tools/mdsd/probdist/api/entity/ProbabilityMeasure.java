package tools.mdsd.probdist.api.entity;

public interface ProbabilityMeasure<T> {

	public Double probability(T value);
}
