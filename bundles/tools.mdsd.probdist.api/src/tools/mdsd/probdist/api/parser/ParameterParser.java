package tools.mdsd.probdist.api.parser;

import tools.mdsd.probdist.api.entity.Matrix;
import tools.mdsd.probdist.api.entity.Vector;
import tools.mdsd.probdist.model.probdist.distributionfunction.Parameter;

public interface ParameterParser {

	public Double parseScalar(Parameter param);
	
	public Vector parseVector(Parameter param);
	
	public Matrix parseMatrix(Parameter param);
}
