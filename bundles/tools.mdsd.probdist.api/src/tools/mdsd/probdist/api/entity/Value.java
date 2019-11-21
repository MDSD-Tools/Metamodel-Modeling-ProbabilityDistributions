package tools.mdsd.probdist.api.entity;

import tools.mdsd.probdist.model.probdist.distributionfunction.Domain;

public abstract class Value<T> {

	protected T value;
	protected Domain domain;
	
	protected Value(T value, Domain domain) {
		this.value = value;
		this.domain = domain;
	}
	
	public T get() {
		return value;
	}
	
	public Domain getDomain() {
		return domain;
	}
}
