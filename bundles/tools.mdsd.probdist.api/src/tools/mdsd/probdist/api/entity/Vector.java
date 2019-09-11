package tools.mdsd.probdist.api.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;

public class Vector {

	private final static BiFunction<Double, Double, Double> ADDITION = (e1, e2) -> e1 + e2;
	private final static BiFunction<Double, Double, Double> SUBTRACTION = (e1, e2) -> e1 - e2;
	private final static BiFunction<Double, Double, Double> MULTIPLICATION = (e1, e2) -> e1 * e2;

	private static class VectorOperation {

		public final static VectorOperation helperInstance = new VectorOperation();

		private Vector v1;
		private Vector v2;

		private VectorOperation() {

		}

		public static VectorOperation applyTo(Vector v1, Vector v2) {
			helperInstance.v1 = v1;
			helperInstance.v2 = v2;
			return helperInstance;
		}

		public Vector elementWise(BiFunction<Double, Double, Double> operation) {
			List<Double> result = new ArrayList<>();
			for (int i = 0; i < v1.getDimension(); i++) {
				result.add(operation.apply(v1.getElementAt(i), v2.getElementAt(i)));
			}
			return Vector.of(result);
		}

	}

	private final java.util.Vector<Double> vector = new java.util.Vector<>();

	private Vector(List<Double> elements) {
		this.vector.addAll(elements);
	}

	public static Vector of(Double... elements) {
		return of(Arrays.asList(elements));
	}

	public static Vector of(List<Double> elements) {
		return new Vector(elements);
	}

	public Vector add(Vector other) {
		checkValidity(other);

		return VectorOperation.applyTo(this, other).elementWise(ADDITION);
	}

	public Vector sub(Vector other) {
		checkValidity(other);

		return VectorOperation.applyTo(this, other).elementWise(SUBTRACTION);
	}

	public Double mult(Vector other) {
		checkValidity(other);

		Stream<Double> result = VectorOperation.applyTo(this, other).elementWise(MULTIPLICATION).getElements().stream();
		return result.reduce(Double::sum).get();
	}

	public Double getElementAt(int index) {
		return vector.elementAt(index);
	}

	public List<Double> getElements() {
		return Collections.list(vector.elements());
	}

	public int getDimension() {
		return vector.capacity();
	}

	private void checkValidity(Vector other) {
		if (getDimension() != other.getDimension()) {
			throw new ProbabilityDistributionException("The vector dimensions are not equal.",
					new IllegalArgumentException());
		}
	}

}
