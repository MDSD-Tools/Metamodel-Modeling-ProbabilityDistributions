package tools.mdsd.probdist.api.apache.supplier;

import static tools.mdsd.probdist.api.apache.util.DistributionTypeModelUtil.findParameterWith;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import tools.mdsd.probdist.api.apache.entity.impl.MultinomialDistribution;
import tools.mdsd.probdist.api.apache.util.DistributionTypeModelUtil;
import tools.mdsd.probdist.api.entity.CategoricalValue;
import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.api.factory.ProbabilityDistributionFactory;
import tools.mdsd.probdist.api.factory.ProbabilityDistributionSupplier;
import tools.mdsd.probdist.api.parser.ParameterParser;
import tools.mdsd.probdist.api.parser.ParameterParser.Sample;
import tools.mdsd.probdist.model.probdist.distributionfunction.Parameter;
import tools.mdsd.probdist.model.probdist.distributionfunction.ProbabilityDistribution;
import tools.mdsd.probdist.model.probdist.distributiontype.ParameterSignature;
import tools.mdsd.probdist.model.probdist.distributiontype.ProbabilityDistributionSkeleton;

public class MultinomialDistributionSupplier implements ProbabilityDistributionSupplier {

	private final static String MD_SKELETON_DISTRIBUTION_NAME = "MulitnomialDistribution";
	private final static String EP_PARAMETER_SIGNATURE_NAME = "EventProbability";

	private final ProbabilityDistributionSkeleton distSkeleton;
	private final ParameterSignature eventProbability;

	public MultinomialDistributionSupplier() {
		this.distSkeleton = DistributionTypeModelUtil.get().findSkeleton(MD_SKELETON_DISTRIBUTION_NAME)
				.orElseThrow(() -> new ProbabilityDistributionException(
						String.format("Skeleton %s is not included in the basic distribtuion model.",
								MD_SKELETON_DISTRIBUTION_NAME)));
		this.eventProbability = DistributionTypeModelUtil.get().findParameterSignatureWith(EP_PARAMETER_SIGNATURE_NAME)
				.orElseThrow(() -> new ProbabilityDistributionException(
						String.format("There is no paramter signature with name %s.", EP_PARAMETER_SIGNATURE_NAME)));
	}

	@Override
	public ProbabilityDistributionFunction<?> get(ProbabilityDistribution distribution) {
		Parameter instantiated = retrieveParameter(distribution.getParams());
		return new MultinomialDistribution(distSkeleton,
				new EnumeratedDistribution<CategoricalValue>(createSampleSpace(instantiated)));
	}

	private Parameter retrieveParameter(List<Parameter> params) {
		List<Parameter> results = findParameterWith(eventProbability, params);
		if (results.isEmpty()) {
			throw new ProbabilityDistributionException(String
					.format("There is no paramter instantiation for signature %s", eventProbability.getEntityName()));
		}
		return results.get(0);
	}

	@Override
	public ProbabilityDistributionSkeleton getImplementedSkeleton() {
		return distSkeleton;
	}

	private List<Pair<CategoricalValue, Double>> createSampleSpace(Parameter instantiated) {
		ParameterParser parser = ProbabilityDistributionFactory.getParameterParser();
		return parser.parseSampleSpace(instantiated).stream().map(this::toPair).collect(Collectors.toList());
	}

	public Pair<CategoricalValue, Double> toPair(Sample s) {
		return Pair.create(s.value, s.probability);
	}

}