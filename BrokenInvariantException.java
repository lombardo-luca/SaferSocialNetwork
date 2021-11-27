// Luca Lombardo, mat. 546688

public class BrokenInvariantException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public BrokenInvariantException () {
		super();
	}
	
	public String toString() {
		return "L'invariante di rappresentazione è stato violato.";
	}
}