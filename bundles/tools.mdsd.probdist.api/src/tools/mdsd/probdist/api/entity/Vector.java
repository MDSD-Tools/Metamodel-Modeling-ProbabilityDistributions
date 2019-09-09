package tools.mdsd.probdist.api.entity;

import java.util.Arrays;
import java.util.List;

import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;

public abstract class Vector {
	
	public static Vector of(Double...elements) {
		return of(Arrays.asList(elements));
	}
	
	public static Vector of(List<Double> elements) {
		try {
			return Vector.class.newInstance().create(elements);
		} catch (InstantiationException e) {
			throw new ProbabilityDistributionException(String.format("An error occured: %s", e.getMessage()), e);
		} catch (IllegalAccessException e) {
			throw new ProbabilityDistributionException(String.format("An error occured: %s", e.getMessage()), e);
		}
	}
	
	public abstract Vector add(Vector other);
	
	public abstract Vector sub(Vector other);
	
	public abstract Double mult(Vector other);
	
	public abstract Double getElement(int index);
	
	protected abstract Vector create(List<Double> elements);
}
