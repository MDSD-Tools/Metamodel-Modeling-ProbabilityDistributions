package tools.mdsd.probdist.api.parser;

import java.util.Set;

import tools.mdsd.probdist.api.entity.CategoricalValue;
import tools.mdsd.probdist.api.entity.Matrix;
import tools.mdsd.probdist.api.entity.Vector;
import tools.mdsd.probdist.model.probdist.distributionfunction.Parameter;

public interface ParameterParser {

	public static class Sample {
		
		public CategoricalValue value;
		public Double probability;
		
		public Sample(CategoricalValue value, Double probability) {
			this.value = value;
			this.probability = probability;
		}
	}
	
	public Double parseScalar(Parameter param);
	
	public Vector parseVector(Parameter param);
	
	public Matrix parseMatrix(Parameter param);
	
	public Set<Sample> parseSampleSpace(Parameter param);
}
