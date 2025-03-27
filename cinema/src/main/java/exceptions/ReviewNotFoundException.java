package exceptions;

public class ReviewNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReviewNotFoundException(String id) {
		super("Review de ID: "+id+" não encontrada");
	}
}
