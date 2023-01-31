package tools.mdsd.probdist.api.apache.supplier;

import static tools.mdsd.probdist.api.apache.util.DistributionTypeModelUtil.filterParametersWithSimpleRepresentation;
import static tools.mdsd.probdist.api.apache.util.ValueUtil.asDouble2DArray;
import static tools.mdsd.probdist.api.apache.util.ValueUtil.asDoubleArray;

import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;

import tools.mdsd.probdist.api.apache.entity.impl.MultivariateNormalDistribution;
import tools.mdsd.probdist.api.apache.entity.impl.UnivariateNormalDistribution;
import tools.mdsd.probdist.api.apache.util.ProbabilityDistributionRepositoryLookup;
import tools.mdsd.probdist.api.entity.Matrix;
import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.entity.Vector;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.api.factory.ProbabilityDistributionSupplier;
import tools.mdsd.probdist.api.parser.ParameterParser;
import tools.mdsd.probdist.distributionfunction.Parameter;
import tools.mdsd.probdist.distributionfunction.ParameterType;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;
import tools.mdsd.probdist.distributionfunction.SimpleParameter;
import tools.mdsd.probdist.distributiontype.ParameterSignature;
import tools.mdsd.probdist.distributiontype.ProbabilityDistributionSkeleton;

public class NormalDistributionSupplier implements ProbabilityDistributionSupplier {

	private final static String ND_SKELETON_DISTRIBUTION_NAME = "NormalDistribution";
	private final static String MEAN_PARAMETER_SIGNATURE_NAME = "Mean";
	private final static String VARIANCE_PARAMETER_SIGNATURE_NAME = "Variance";

	private final ProbabilityDistributionSkeleton distSkeleton;
	private final ParameterSignature mean;
	private final ParameterSignature variance;
	private final ParameterParser parameterParser;

	public NormalDistributionSupplier(ParameterParser parameterParser) {
		this.distSkeleton = ProbabilityDistributionRepositoryLookup.get().findSkeleton(ND_SKELETON_DISTRIBUTION_NAME)
				.orElseThrow(() -> new ProbabilityDistributionException(
						String.format("Skeleton %s is not included in the basic distribtuion model.",
								ND_SKELETON_DISTRIBUTION_NAME)));
		this.mean = ProbabilityDistributionRepositoryLookup.get().findParameterSignatureWith(MEAN_PARAMETER_SIGNATURE_NAME)
				.orElseThrow(() -> new ProbabilityDistributionException(
						String.format("There is no paramter signature with name %s.", MEAN_PARAMETER_SIGNATURE_NAME)));
		this.variance = ProbabilityDistributionRepositoryLookup.get().findParameterSignatureWith(VARIANCE_PARAMETER_SIGNATURE_NAME)
				.orElseThrow(() -> new ProbabilityDistributionException(String
						.format("There is no paramter signature with name %s.", VARIANCE_PARAMETER_SIGNATURE_NAME)));
		this.parameterParser = parameterParser;
	}

	@Override
	public ProbabilityDistributionFunction<?> get(ProbabilityDistribution distribution) {
		SimpleParameter meanInstant = retrieveMeanParameter(distribution.getParams());
		SimpleParameter varInstant = retrieveVarianceParameter(distribution.getParams());
		if (isUnivariate(meanInstant, varInstant)) {
			return createUnivariateND(meanInstant, varInstant);
		}
		if (isMultivariate(meanInstant, varInstant)) {
			return createMultivariateND(meanInstant, varInstant);
		}

		throw new ProbabilityDistributionException(
				String.format("Parameter types are incorrect for distribution %s", ND_SKELETON_DISTRIBUTION_NAME));
	}

	@Override
	public ProbabilityDistributionSkeleton getImplementedSkeleton() {
		return distSkeleton;
	}

	private SimpleParameter retrieveMeanParameter(List<Parameter> params) {
		return retrieveParameter(mean, params);
	}

	private SimpleParameter retrieveVarianceParameter(List<Parameter> params) {
		return retrieveParameter(variance, params);
	}

	private SimpleParameter retrieveParameter(ParameterSignature paramSignature, List<Parameter> params) {
		List<Parameter> results = filterParametersWithSimpleRepresentation(paramSignature, params);
		if (results.isEmpty()) {
			throw new ProbabilityDistributionException(String
					.format("There is no paramter instantiation for signature %s", paramSignature.getEntityName()));
		}
		return (SimpleParameter) results.get(0).getRepresentation();
	}

	private boolean isUnivariate(SimpleParameter meanInstant, SimpleParameter varInstant) {
		return meanInstant.getType() == ParameterType.SCALAR && varInstant.getType() == ParameterType.SCALAR;
	}

	private boolean isMultivariate(SimpleParameter meanInstant, SimpleParameter varInstant) {
		return meanInstant.getType() == ParameterType.VECTOR && varInstant.getType() == ParameterType.MATRIX;
	}

	private ProbabilityDistributionFunction<?> createUnivariateND(SimpleParameter meanInstant, SimpleParameter varInstant) {
		double mean = parameterParser.parseScalar(meanInstant);
		double variance = parameterParser.parseScalar(varInstant);
		return new UnivariateNormalDistribution(distSkeleton, new NormalDistribution(mean, Math.sqrt(variance)));
	}

	private ProbabilityDistributionFunction<?> createMultivariateND(SimpleParameter meanInstant, SimpleParameter varInstant) {
		Vector meanVec = parameterParser.parseVector(meanInstant);
		Matrix covMatrix = parameterParser.parseMatrix(varInstant);
		return new MultivariateNormalDistribution(distSkeleton,
				new org.apache.commons.math3.distribution.MultivariateNormalDistribution(asDoubleArray(meanVec),
						asDouble2DArray(covMatrix)));
	}

}
