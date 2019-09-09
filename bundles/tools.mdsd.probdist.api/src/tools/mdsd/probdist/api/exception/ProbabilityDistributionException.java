package tools.mdsd.probdist.api.exception;

public class ProbabilityDistributionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ProbabilityDistributionException(String errorMessage) {
		super(errorMessage);
	}	
	
	public ProbabilityDistributionException(String errorMessage, Throwable throwable) {
		super(errorMessage, throwable);
	}
}
