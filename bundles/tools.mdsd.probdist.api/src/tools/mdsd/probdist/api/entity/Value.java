package tools.mdsd.probdist.api.entity;

import tools.mdsd.probdist.distributionfunction.Domain;

public abstract class Value<T> {

	protected final T value;
	protected final Domain domain;
	
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
