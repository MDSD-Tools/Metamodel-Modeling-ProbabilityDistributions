package tools.mdsd.probdist.api.apache.util;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;

import tools.mdsd.probdist.distributionfunction.ComplexParameter;
import tools.mdsd.probdist.distributionfunction.Parameter;
import tools.mdsd.probdist.distributionfunction.SimpleParameter;
import tools.mdsd.probdist.distributiontype.ParameterSignature;

public class DistributionTypeModelUtil {



	public static List<Parameter> filterParametersWithSimpleRepresentation(ParameterSignature paramSignature,
			List<Parameter> params) {
		return params.stream().filter(paramsWithSignature(paramSignature).and(representation(SimpleParameter.class)))
				.collect(toList());
	}

	public static List<Parameter> filterParametersWithComplexRepresentation(ParameterSignature paramSignature,
			List<Parameter> params) {
		return params.stream().filter(paramsWithSignature(paramSignature).and(representation(ComplexParameter.class)))
				.collect(toList());
	}

	private static Predicate<Parameter> representation(Class<?> givenClass) {
		return param -> givenClass.isInstance(param.getRepresentation());
	}

	private static Predicate<Parameter> paramsWithSignature(ParameterSignature paramSignature) {
		return param -> param.getInstantiated().getId().equals(paramSignature.getId());
	}


}
