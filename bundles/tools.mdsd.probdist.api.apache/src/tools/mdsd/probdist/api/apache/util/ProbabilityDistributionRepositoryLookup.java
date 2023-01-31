package tools.mdsd.probdist.api.apache.util;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import tools.mdsd.probdist.distributiontype.ParameterSignature;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionRepository;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;
import tools.mdsd.probdist.model.basic.loader.BasicDistributionTypesLoader;

public class ProbabilityDistributionRepositoryLookup implements IProbabilityDistributionRepositoryLookup {
    
    private static ProbabilityDistributionRepositoryLookup utilInstance = null;

    private final ProbabilityDistributionRepository basicRepository;

    private ProbabilityDistributionRepositoryLookup(ProbabilityDistributionRepository basicRepository) {
        this.basicRepository = basicRepository;
    }

    private ProbabilityDistributionRepositoryLookup() {
        this(BasicDistributionTypesLoader.loadRepository());
    }

    public static IProbabilityDistributionRepositoryLookup get() {
        if (utilInstance == null) {
            utilInstance = new ProbabilityDistributionRepositoryLookup();
        }
        return utilInstance;
    }

    public static IProbabilityDistributionRepositoryLookup get(ProbabilityDistributionRepository basicRepository) {
        if (utilInstance == null) {
            utilInstance = new ProbabilityDistributionRepositoryLookup(basicRepository);
        }
        return utilInstance;
    }

    @Override
    public Optional<ProbabilityDistributionSkeleton> findSkeleton(String name) {
        return getAllDistributions().filter(each -> each.getEntityName().equals(name)).findFirst();
    }

    private Stream<ProbabilityDistributionSkeleton> getAllDistributions() {
        return basicRepository.getDistributionFamilies().stream();
    }

    @Override
    public Optional<ParameterSignature> findParameterSignatureWith(String name) {
        return getAllDistributions().flatMap(each -> each.getParamStructures().stream())
                .filter(paramSignatureWith(name)).findFirst();
    }
    
    private static Predicate<ParameterSignature> paramSignatureWith(String name) {
        return param -> param.getEntityName().equals(name);
    }
}