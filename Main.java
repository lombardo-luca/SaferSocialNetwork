// Luca Lombardo, mat. 546688

public class Main {
	public static void main(String[] args) {
		
		// Test della classe Post
		if (Test.testPost()) {
			System.out.println("\nClasse Post: OK\n");
		}
		
		else {
			System.out.println("\nClasse Post: ERRORE\n");
		}
		
		// Test della classe SocialNetwork
		if (Test.testSocialNetwork()) {
			System.out.println("\nClasse SocialNetwork: OK\n");
		}
		
		else {
			System.out.println("\nClasse SocialNetwork: ERRORE\n");
		}
		
		// Test della classe SaferSocialNetwork
		if (Test.testSaferSocialNetwork()) {
			System.out.println("\nClasse SaferSocialNetwork: OK\n");
		}
		
		else {
			System.out.println("\nClasse SaferSocialNetwork: ERRORE\n");
		}
	}
}
