package tools.mdsd.probdist.api.entity;

import tools.mdsd.probdist.model.probdist.distributionfunction.Domain;

public abstract class Value<T> {

	protected T value;
	protected Domain domain;
	
	public T get() {
		return value;
	}
	
	public Domain getDomain() {
		return domain;
	}
}
