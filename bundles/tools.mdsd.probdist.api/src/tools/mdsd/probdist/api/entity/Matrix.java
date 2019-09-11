package tools.mdsd.probdist.api.entity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.MatrixDimensionMismatchException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;

public class Matrix {

	private final RealMatrix matrix;

	private Matrix(double[][] rowVectors) {
		this.matrix = MatrixUtils.createRealMatrix(rowVectors);
	}

	private Matrix(RealMatrix matrix) {
		this.matrix = matrix;
	}

	public static Matrix of(Vector... rowVectors) {
		return of(Arrays.asList(rowVectors));
	}

	public static Matrix of(List<Vector> rowVectors) {
		return new Matrix(toArray(rowVectors));
	}

	private static double[][] toArray(List<Vector> rowVectors) {
		return rowVectors.stream().map(each -> each.getElements().toArray()).toArray(double[][]::new);
	}

	public Matrix add(Matrix other) {
		try {
			return new Matrix(matrix.add(other.matrix));
		} catch (MatrixDimensionMismatchException e) {
			throw new ProbabilityDistributionException("The matrix dimensions do not match.", e);
		}
	}

	public Matrix sub(Matrix other) {
		try {
			return new Matrix(matrix.subtract(other.matrix));
		} catch (MatrixDimensionMismatchException e) {
			throw new ProbabilityDistributionException("The matrix dimensions do not match.", e);
		}
	}

	public Matrix mult(Matrix other) {
		try {
			return new Matrix(matrix.multiply(other.matrix));
		} catch (MatrixDimensionMismatchException e) {
			throw new ProbabilityDistributionException("The matrix dimensions do not match.", e);
		}
	}

	public Matrix transpose() {
		return new Matrix(matrix.transpose());
	}

	public Double getElement(int row, int column) {
		try {
			return matrix.getEntry(row, column);
		} catch (OutOfRangeException e) {
			throw new ProbabilityDistributionException("The index is out of range.", e);
		}
	}

	public Vector getRow(int index) {
		try {
			return Vector.of(toDoubleList(matrix.getRow(index)));
		} catch (OutOfRangeException e) {
			throw new ProbabilityDistributionException("The index is out of range.", e);
		}
	}

	public Vector getColumn(int index) {
		try {
			return Vector.of(toDoubleList(matrix.getColumn(index)));
		} catch (OutOfRangeException e) {
			throw new ProbabilityDistributionException("The index is out of range.", e);
		}
	}
	
	public int getRowDimension() {
		return matrix.getRowDimension();
	}
	
	public int getColumnDimension() {
		return matrix.getColumnDimension();
	}

	private List<Double> toDoubleList(double[] doubles) {
		return DoubleStream.of(doubles).boxed().collect(Collectors.toList());
	}

}
