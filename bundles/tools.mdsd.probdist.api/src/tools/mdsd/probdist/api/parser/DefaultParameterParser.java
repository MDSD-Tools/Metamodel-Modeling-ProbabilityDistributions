package tools.mdsd.probdist.api.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tools.mdsd.probdist.api.entity.Matrix;
import tools.mdsd.probdist.api.entity.Vector;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.model.probdist.distributionfunction.Parameter;

public class DefaultParameterParser implements ParameterParser {

	public static final String FLOATING_POINT_PATTERN = "[-+]?[0-9]+\\.?[0-9]*";
	public static final String VECTOR_PATTERN = String.format("\\[(%1s)(,%2s)+\\]", FLOATING_POINT_PATTERN, FLOATING_POINT_PATTERN);
	public static final String MATRIX_PATTERN = String.format("\\[(%1s)(,%2s)+\\]", VECTOR_PATTERN, VECTOR_PATTERN);
	
	@Override
	public Double parseScalar(Parameter param) {
		return Double.parseDouble(param.getValue().getValue());
	}

	/**
	 * Valid string pattern: '[v1,v2,...]', in which v1,v2,... represent double values.
	 */
	@Override
	public Vector parseVector(Parameter param) {
		String value = param.getValue().getValue();
		checkSchemaConformance(value, VECTOR_PATTERN);
		
		return Vector.of(extractElements(value));
	}

	/**
	 * Valid string pattern: '[[v11,v12,...],[v21,v22,...],...]', in which [v11,v12,...],[v21,v22,...],... represent vectors.
	 */
	@Override
	public Matrix parseMatrix(Parameter param) {
		String value = param.getValue().getValue();
		checkSchemaConformance(value, MATRIX_PATTERN);
		
		return Matrix.of(extractVectors(value));
	}

	private void checkSchemaConformance(String value, String regex) {
		if (value.matches(regex) == false) {
			throw new ProbabilityDistributionException(String.format("The paramter value %s does not fullfill the specification schema.", value));
		} 
	}
	
	private List<Double> extractElements(String value) {
		List<Double> elements = new ArrayList<>();
		Matcher matcher = Pattern.compile(FLOATING_POINT_PATTERN).matcher(value);
		while (matcher.find()) {
			Double element = Double.parseDouble(matcher.group());
			elements.add(element);
		}
		return elements;
	}
	
	private List<Vector> extractVectors(String value) {
		List<Vector> vectors = new ArrayList<>();
		Matcher matcher = Pattern.compile(VECTOR_PATTERN).matcher(value);
		while (matcher.find()) {
			List<Double> elements = extractElements(value);
			vectors.add(Vector.of(elements));
		}
		return vectors;
	}

}
