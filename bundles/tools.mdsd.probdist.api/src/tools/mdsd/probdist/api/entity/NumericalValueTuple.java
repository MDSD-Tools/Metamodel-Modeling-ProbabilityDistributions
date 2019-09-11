package tools.mdsd.probdist.api.entity;

import java.util.List;
import java.util.stream.Collectors;

import tools.mdsd.probdist.model.probdist.distributionfunction.Domain;

public class NumericalValueTuple extends Value<List<NumericalValue>> {

	private NumericalValueTuple(List<NumericalValue> values, Domain domain) {
		this.value = values;
		this.domain = domain;
	}

	public static NumericalValueTuple create(List<NumericalValue> values, Domain domain) {
		return new NumericalValueTuple(values, domain);
	}
	
	public List<Double> asDoubles() {
		return value.stream().map(v -> v.asReal()).collect(Collectors.toList());
	}
	
}
