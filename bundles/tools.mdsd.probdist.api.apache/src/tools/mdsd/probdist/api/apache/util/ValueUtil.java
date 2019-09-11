package tools.mdsd.probdist.api.apache.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import tools.mdsd.probdist.api.entity.Matrix;
import tools.mdsd.probdist.api.entity.NumericalValue;
import tools.mdsd.probdist.api.entity.Value;
import tools.mdsd.probdist.api.entity.Vector;

public class ValueUtil {

	public static double[] asDoubleArray(List<NumericalValue> doubles) {
		double[] helper = new double[doubles.size()];
		for (int i = 0; i < doubles.size(); i++) {
			helper[i] = doubles.get(i).asReal();
		}
		return helper;
	}

	public static double[] asDoubleArray(Vector vector) {
		double[] helper = new double[vector.getDimension()];
		for (int i = 0; i < vector.getDimension(); i++) {
			helper[i] = vector.getElementAt(i);
		}
		return helper;
	}

	public static double[][] asDouble2DArray(Matrix matrix) {
		double[][] helper = new double[matrix.getRowDimension()][matrix.getColumnDimension()];
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			helper[i] = asDoubleArray(matrix.getRow(i));
		}
		return helper;
	}

	public static <T extends Value<?>> List<T> toValueList(double[] doubles, Function<Double, T> valueMapper) {
		return Arrays.stream(doubles).boxed().map(valueMapper).collect(Collectors.toList());
	}

}
