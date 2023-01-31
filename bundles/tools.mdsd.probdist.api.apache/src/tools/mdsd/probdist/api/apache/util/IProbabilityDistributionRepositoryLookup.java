package tools.mdsd.probdist.api.apache.util;

import java.util.Optional;

import tools.mdsd.probdist.distributiontype.ParameterSignature;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;

public interface IProbabilityDistributionRepositoryLookup {

    Optional<ProbabilityDistributionSkeleton> findSkeleton(String name);

    Optional<ParameterSignature> findParameterSignatureWith(String name);
    
}
