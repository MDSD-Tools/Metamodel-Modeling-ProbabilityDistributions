package tools.mdsd.probdist.api.parser;

import java.util.Set;

import tools.mdsd.probdist.api.entity.CategoricalValue;
import tools.mdsd.probdist.api.entity.Matrix;
import tools.mdsd.probdist.api.entity.Vector;
import tools.mdsd.probdist.model.probdist.distributionfunction.SimpleParameter;

public interface ParameterParser {

	public static class Sample {
		
		public CategoricalValue value;
		public Double probability;
		
		public Sample(CategoricalValue value, Double probability) {
			this.value = value;
			this.probability = probability;
		}
	}
	
	public Double parseScalar(SimpleParameter param);
	
	public Vector parseVector(SimpleParameter param);
	
	public Matrix parseMatrix(SimpleParameter param);
	
	public Set<Sample> parseSampleSpace(SimpleParameter param);
}
