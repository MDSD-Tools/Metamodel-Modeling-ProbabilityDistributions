package tools.mdsd.probdist.api.entity;

import java.util.Arrays;
import java.util.List;

import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;

public abstract class Matrix {

	public static Matrix of(Vector...columVectors) {
		return of(Arrays.asList(columVectors));
	}
	
	public static Matrix of(List<Vector> columVectors) {
		try {
			return Matrix.class.newInstance().create(columVectors);
		} catch (InstantiationException e) {
			throw new ProbabilityDistributionException(String.format("An error occured: %s", e.getMessage()), e);
		} catch (IllegalAccessException e) {
			throw new ProbabilityDistributionException(String.format("An error occured: %s", e.getMessage()), e);
		}
	}
	
	public abstract Matrix add(Matrix other);
	
	public abstract Matrix sub(Matrix other);
	
	public abstract Matrix mult(Matrix other);
	
	public abstract Matrix transposed();
	
	public abstract Double getElement(int row, int column);
	
	public abstract Double getRow(int index);
	
	public abstract Double getColumn(int index);
	
	protected abstract Matrix create(List<Vector> columVectors);
}
