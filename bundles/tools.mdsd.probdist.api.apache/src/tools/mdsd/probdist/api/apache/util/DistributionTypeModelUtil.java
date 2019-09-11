package tools.mdsd.probdist.api.apache.util;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import tools.mdsd.probdist.model.probdist.distributionfunction.Parameter;
import tools.mdsd.probdist.model.probdist.distributiontype.DistributiontypePackage;
import tools.mdsd.probdist.model.probdist.distributiontype.ParameterSignature;
import tools.mdsd.probdist.model.probdist.distributiontype.ProbabilityDistributionRepository;
import tools.mdsd.probdist.model.probdist.distributiontype.ProbabilityDistributionSkeleton;

public class DistributionTypeModelUtil {

	private final static URI BASIC_MODEL_URI = URI.createPlatformPluginURI(
			"platform:/plugin/tools.mdsd.probdist.model.basic/model/BasicDistributionTypes.distributiontype", false);

	private final static DistributionTypeModelUtil utilInstance = new DistributionTypeModelUtil();

	private final ProbabilityDistributionRepository basicRepository;

	private DistributionTypeModelUtil() {
		basicRepository = load();
	}

	public static DistributionTypeModelUtil get() {
		return utilInstance;
	}

	private ProbabilityDistributionRepository load() {
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		rs.getPackageRegistry().put(DistributiontypePackage.eNS_URI, DistributiontypePackage.eINSTANCE);

		return (ProbabilityDistributionRepository) rs.getResource(BASIC_MODEL_URI, true).getContents().get(0);
	}

	public Optional<ProbabilityDistributionSkeleton> findSkeleton(String name) {
		return getAllDistributions().filter(each -> each.getEntityName() == name).findFirst();
	}

	private Stream<ProbabilityDistributionSkeleton> getAllDistributions() {
		return basicRepository.getDistributionFamilies().stream();
	}

	public Optional<ParameterSignature> findParameterSignatureWith(String name) {
		return getAllDistributions().flatMap(each -> each.getParamStructures().stream())
									.filter(paramSignatureWith(name))
									.findFirst();
	}
	
	public static List<Parameter> findParameterWith(ParameterSignature paramSignature, List<Parameter> params) {
		return params.stream().filter(paramsWithSignature(paramSignature)).collect(Collectors.toList());
	}

	private static Predicate<Parameter> paramsWithSignature(ParameterSignature paramSignature) {
		return param -> param.getInstantiated().getId() == paramSignature.getId();
	}
	
	private static Predicate<ParameterSignature> paramSignatureWith(String name) {
		return param -> param.getEntityName() == name;
	}
}
