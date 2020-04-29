package tools.mdsd.probdist.api.entity;

import tools.mdsd.probdist.model.probdist.distributionfunction.Domain;

public class CategoricalValue extends Value<String> {
	
	protected CategoricalValue(String value) {
		super(value, Domain.CATEGORY);
	}
	
	public static CategoricalValue create(String value) {
		return new CategoricalValue(value);
	}
	
	@Override
	public boolean equals(Object other) {
		return value.equals(other); 
	}
	
	@Override
	public String toString() {
		return value; 
	}
	
}
