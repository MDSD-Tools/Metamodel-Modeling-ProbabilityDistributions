package tools.mdsd.probdist.api.entity;

import java.util.List;
import java.util.Optional;

import tools.mdsd.probdist.api.entity.Conditionable.Conditional;

public interface CPDRepresentation<I extends Value<?>> {

    public abstract static class CPDRepresentationBuilder<I extends Value<?>> {

        public abstract CPDRepresentation<I> build();

    }

    public Optional<ProbabilityDistributionFunction<I>> getPDFGiven(List<Conditional<I>> conditionals);

    public CPDRepresentationBuilder<I> builder();

}
