package tools.mdsd.probdist.api.apache.util;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import tools.mdsd.probdist.distributionfunction.ComplexParameter;
import tools.mdsd.probdist.distributionfunction.Parameter;
import tools.mdsd.probdist.distributionfunction.SimpleParameter;
import tools.mdsd.probdist.distributiontype.ParameterSignature;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionRepository;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;
import tools.mdsd.probdist.model.basic.loader.BasicDistributionTypesLoader;

public class DistributionTypeModelUtil {

	private static DistributionTypeModelUtil utilInstance = null;

	private final ProbabilityDistributionRepository basicRepository;

	private DistributionTypeModelUtil(ProbabilityDistributionRepository basicRepository) {
		this.basicRepository = basicRepository;
	}

	private DistributionTypeModelUtil() {
		this(BasicDistributionTypesLoader.loadRepository());
	}

	public static DistributionTypeModelUtil get() {
		if (utilInstance == null) {
			utilInstance = new DistributionTypeModelUtil();
		}
		return utilInstance;
	}

	public static DistributionTypeModelUtil get(ProbabilityDistributionRepository basicRepository) {
		if (utilInstance == null) {
			utilInstance = new DistributionTypeModelUtil(basicRepository);
		}
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

	private static Predicate<ParameterSignature> paramSignatureWith(String name) {
		return param -> param.getEntityName().equals(name);
	}
}
