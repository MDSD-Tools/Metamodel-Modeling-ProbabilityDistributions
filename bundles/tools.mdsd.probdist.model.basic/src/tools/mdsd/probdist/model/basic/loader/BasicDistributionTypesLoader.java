package tools.mdsd.probdist.model.basic.loader;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import tools.mdsd.probdist.model.probdist.distributiontype.DistributiontypePackage;
import tools.mdsd.probdist.model.probdist.distributiontype.ProbabilityDistributionRepository;

public class BasicDistributionTypesLoader {

	private final static URI BASIC_MODEL_URI = URI.createPlatformPluginURI(
			"/tools.mdsd.probdist.model.basic/model/BasicDistributionTypes.distributiontype", true);

	public static ProbabilityDistributionRepository loadRepository() {
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		rs.getPackageRegistry().put(DistributiontypePackage.eNS_URI, DistributiontypePackage.eINSTANCE);

		return (ProbabilityDistributionRepository) rs.getResource(BASIC_MODEL_URI, true).getContents().get(0);
	}

}
