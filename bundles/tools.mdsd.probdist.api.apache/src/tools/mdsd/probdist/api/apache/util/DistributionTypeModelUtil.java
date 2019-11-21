package tools.mdsd.probdist.api.apache.util;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import tools.mdsd.probdist.model.basic.loader.BasicDistributionTypesLoader;
import tools.mdsd.probdist.model.probdist.distributionfunction.Parameter;
import tools.mdsd.probdist.model.probdist.distributionfunction.SimpleParameter;
import tools.mdsd.probdist.model.probdist.distributiontype.ParameterSignature;
import tools.mdsd.probdist.model.probdist.distributiontype.ProbabilityDistributionRepository;
import tools.mdsd.probdist.model.probdist.distributiontype.ProbabilityDistributionSkeleton;

public class DistributionTypeModelUtil {

	private final static DistributionTypeModelUtil utilInstance = new DistributionTypeModelUtil();

	private final ProbabilityDistributionRepository basicRepository;

	private DistributionTypeModelUtil() {
		basicRepository = BasicDistributionTypesLoader.loadRepository();
	}

	public static DistributionTypeModelUtil get() {
		return utilInstance;
	}

	public Optional<ProbabilityDistributionSkeleton> findSkeleton(String name) {
		return getAllDistributions().filter(each -> each.getEntityName().equals(name)).findFirst();
	}

	private Stream<ProbabilityDistributionSkeleton> getAllDistributions() {
		return basicRepository.getDistributionFamilies().stream();
	}

	public Optional<ParameterSignature> findParameterSignatureWith(String name) {
		return getAllDistributions().flatMap(each -> each.getParamStructures().stream())
				.filter(paramSignatureWith(name)).findFirst();
	}

	public static List<SimpleParameter> filterSimpleParametersWith(ParameterSignature paramSignature,
			List<Parameter> params) {
		return params.stream().filter(paramsWithSignature(paramSignature).and(type(SimpleParameter.class)))
				.map(p -> (SimpleParameter) p).collect(Collectors.toList());
	}

	private static Predicate<Parameter> type(Class<?> givenClass) {
		return param -> givenClass.isInstance(givenClass);
	}

	private static Predicate<Parameter> paramsWithSignature(ParameterSignature paramSignature) {
		return param -> param.getInstantiated().getId().equals(paramSignature.getId());
	}

	private static Predicate<ParameterSignature> paramSignatureWith(String name) {
		return param -> param.getEntityName().equals(name);
	}
}
