package tools.mdsd.probdist.api.builder;

import java.util.List;
import java.util.Objects;

import tools.mdsd.probdist.api.entity.CategoricalValue;
import tools.mdsd.probdist.api.entity.ConditionalProbabilityDistribution;
import tools.mdsd.probdist.api.entity.ProbabilityDistributionFunction;
import tools.mdsd.probdist.api.exception.ProbabilityDistributionException;
import tools.mdsd.probdist.api.factory.IProbabilityDistributionFactory;
import tools.mdsd.probdist.distributionfunction.Parameter;
import tools.mdsd.probdist.distributionfunction.ProbabilityDistribution;
import tools.mdsd.probdist.distributionfunction.TabularCPD;

public class ProbabilityDistributionBuilder {

    private final IProbabilityDistributionFactory<CategoricalValue> probabilityDistributionFactory;

    private ProbabilityDistribution distribution = null;
    private boolean asCPD = false;

    private ProbabilityDistributionBuilder(
            IProbabilityDistributionFactory<CategoricalValue> probabilityDistributionFactory) {
        this.probabilityDistributionFactory = probabilityDistributionFactory;
    }

    public static ProbabilityDistributionBuilder create(
            IProbabilityDistributionFactory<CategoricalValue> probabilityDistributionFactory) {
        return new ProbabilityDistributionBuilder(probabilityDistributionFactory);
    }

    public ProbabilityDistributionBuilder withStructure(ProbabilityDistribution distribution) {
        this.distribution = distribution;
        return this;
    }

    public ProbabilityDistributionBuilder asConditionalProbabilityDistribution() {
        this.asCPD = true;
        return this;
    }

    public ProbabilityDistributionFunction<?> build() {
        Objects.requireNonNull(distribution, "There need to be a probability distribution function.");

        if (asCPD) {
            return createCPD();
        }
        return queryRealisation(probabilityDistributionFactory);
    }

    private ProbabilityDistributionFunction<CategoricalValue> queryRealisation(
            IProbabilityDistributionFactory<CategoricalValue> probabilityDistributionFactory) {
        return probabilityDistributionFactory.getInstanceOf(distribution)
            .orElseThrow(() -> new ProbabilityDistributionException(
                    String.format("There is no realisation for the PDF: %s", distribution.getInstantiated()
                        .getEntityName())));
    }

    private ProbabilityDistributionFunction<CategoricalValue> createCPD() {
        List<Parameter> params = distribution.getParams();
        if (isTabularCPD(params)) {
            return new ConditionalProbabilityDistribution(distribution, (TabularCPD) params.get(0)
                .getRepresentation(), probabilityDistributionFactory);
        }

        throw new ProbabilityDistributionException(String
            .format("The probability distribution of type %s cannot be used as a CPD.", distribution.getInstantiated()
                .getEntityName()));
    }

    private boolean isTabularCPD(List<Parameter> params) {
        if (params.size() != 1) {
            return false;
        }
        return TabularCPD.class.isInstance(params.get(0)
            .getRepresentation());
    }

}
