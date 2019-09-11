package tools.mdsd.probdist.api.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import tools.mdsd.probdist.api.entity.CategoricalValue;
import tools.mdsd.probdist.api.entity.Matrix;
import tools.mdsd.probdist.api.entity.Vector;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.model.probdist.distributionfunction.Parameter;

public class DefaultParameterParser implements ParameterParser {

	public static final String SAMPLE_DELIMITER = ";";
	public static final String PAIR_DELIMITER = ",";
	public static final String FLOATING_POINT_PATTERN = "[-+]?[0-9]+\\.?[0-9]*";
	public static final String VECTOR_PATTERN = String.format("\\[(%1s)(,%2s)+\\]", FLOATING_POINT_PATTERN,
			FLOATING_POINT_PATTERN);
	public static final String MATRIX_PATTERN = String.format("\\[(%1s)(,%2s)+\\]", VECTOR_PATTERN, VECTOR_PATTERN);
	public static final String MATRIX_SAMPLESPACE = String.format("{(.)+,%1s}(%2s{(.)+,%3s})*", FLOATING_POINT_PATTERN,
			SAMPLE_DELIMITER, FLOATING_POINT_PATTERN);

	@Override
	public Double parseScalar(Parameter param) {
		return Double.parseDouble(param.getValue().getValue());
	}

	/**
	 * Valid string pattern: '[v1,v2,...]', in which v1,v2,... represent double
	 * values.
	 */
	@Override
	public Vector parseVector(Parameter param) {
		String value = param.getValue().getValue();
		checkSchemaConformance(value, VECTOR_PATTERN);

		return Vector.of(extractElements(value));
	}

	/**
	 * Valid string pattern: '[[v11,v12,...],[v21,v22,...],...]', in which
	 * [v11,v12,...],[v21,v22,...],... represent vectors.
	 */
	@Override
	public Matrix parseMatrix(Parameter param) {
		String value = param.getValue().getValue();
		checkSchemaConformance(value, MATRIX_PATTERN);

		return Matrix.of(extractVectors(value));
	}

	/**
	 * Valid string pattern: '{v1,p1};{v2,p2},...' in which v1 represents a string
	 * and p1 a double value
	 */
	@Override
	public Set<Sample> parseSampleSpace(Parameter param) {
		String value = param.getValue().getValue();
		checkSchemaConformance(value, MATRIX_PATTERN);

		return extractSamples(value);
	}

	private void checkSchemaConformance(String value, String regex) {
		if (value.matches(regex) == false) {
			throw new ProbabilityDistributionException(
					String.format("The paramter value %s does not fullfill the specification schema.", value));
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

	private Set<Sample> extractSamples(String value) {
		return Stream.of(value.split(SAMPLE_DELIMITER)).map(toSample()).collect(Collectors.toSet());
	}

	private Function<String, Sample> toSample() {
		return s -> {
			String[] pair = removeAllSpaces(s).split(PAIR_DELIMITER);
			String value = pair[0];
			Double probability = Double.parseDouble(pair[1]);
			return new Sample(CategoricalValue.create(value), probability);
		};
	}

	private String removeAllSpaces(String s) {
		return s.replaceAll("\\s", "");
	}

}
