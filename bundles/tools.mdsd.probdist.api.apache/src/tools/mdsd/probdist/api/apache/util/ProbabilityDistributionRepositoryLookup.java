package tools.mdsd.probdist.api.apache.util;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import tools.mdsd.probdist.distributiontype.ParameterSignature;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionRepository;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;

public class ProbabilityDistributionRepositoryLookup implements IProbabilityDistributionRepositoryLookup {
    

    private final ProbabilityDistributionRepository basicRepository;

    public ProbabilityDistributionRepositoryLookup(ProbabilityDistributionRepository basicRepository) {
        this.basicRepository = basicRepository;
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