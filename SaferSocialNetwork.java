// Luca Lombardo, mat. 546688

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SaferSocialNetwork extends SocialNetwork {
	/*
	 * OVERVIEW: struttura dati modificabile rappresentante una rete sociale. Consente agli utenti di inviare messaggi lunghi al più 140 caratteri e di lasciare "like" ai post degli altri utenti. 
	 * 		     Un "like" è un particolare post p1 il cui testo è nel formato: "like:id", dove id è un numero intero corrispondente all'id di un qualche altro post p2. 
	 * 			 Se l'autore del post p1 mette un "like" a un post p2, egli incomincia a "seguire" l'autore di p2 (ne diventa un "follower"). 
	 * 			 Gli utenti non possono seguire sé stessi, di conseguenza non è permesso mettere like a un proprio post.
	 * 			 Permette, inoltre, di segnalare i post con contenuti offensivi.
	 * 
	 * TYPICAL ELEMENT: una tripla di insiemi <Post, ReportedPost, Followers> dove:
	 * 					Post è l'insieme dei post presenti nella rete sociale, dove ogni post è una quadrupla <id, author, text, timestamp>;
	 * 					ReportedPost è un sottoinsieme di Post, che include tutti i post nella rete sociale che sono stati segnalati per contenuti offensivi;
	 * 					Followers è l'insieme delle coppie di stringhe <Autore1, Autore2> tali che Autore1 segue (è un follower di) Autore2 nella rete sociale.
	 * 
	 * AF: <(String -> {Post_1, ..., Post_n}), {Post_1, ..., Post_n}, (String -> {String_1, ..., String_n})> -> <{Post1_, ..., Post_n}, {Post_1, ..., Post_n}, {<String_1-1, String_1-2>, ..., <String_n-1, String_n-2>}>
	 * 	   Associa la Map<String, Set<Post>> all'insieme dei post di ogni utente
	 * 	   Si comporta da identità sull'insieme di post reportedPosts
	 * 	   Associa la Map<String, Set<String>> all'insieme di coppie <Autore, Followed> 
	 * 
	 * RI: @ALSO (reportedPosts != null) &&	
	 * 	         (per ogni x, y . (x != y && reportedPosts.contains(x) && reportedPosts.contains(y)) -> x.getId() != y.getId())
	 */
	
	private Set<Post> reportedPosts;	// insieme dei post segnalati per contenuti offensivi
	
	// Metodo costruttore
	public SaferSocialNetwork() {
		super();
		reportedPosts = new HashSet<Post>();
		
		checkSaferRep();
	}
	/*
	 * EFFECTS: restituisce un nuovo oggetto ssn di tipo SaferSocialNetwork tale che ssn(Post), ssn(ReportedPosts) e ssn(Followers) sono insiemi vuoti
	 */
	
	// Metodo privato che restituisce true se il testo del post dato come parametro contiene almeno una delle parole offensive presenti nella lista data come parametro (non case-sensitive), false altrimenti
	private static boolean isOffensive(Post p, List<String> words) throws NullPointerException {
		// Se uno dei parametri è null, solleva un'eccezione
		if (p == null || words == null) {
			throw new NullPointerException();
		}
		
		return contains(p, words);
	}
	/*
	 * REQUIRES: (p != null) && (words != null)
	 * THROWS: se p == null || words == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce true se esiste una stringa w appartenente a words . w è sottostringa di p.getText(), false altrimenti
	 */
	
	/* Prende come parametro l'id di un post nella rete sociale e una lista di parole considerate offensive: se il post contiene almeno una di quelle parole, 
	 * esso viene aggiunto alla lista dei post segnalati e il metodo restituisce true; altrimenti, il metodo restituisce false */
	public boolean reportIfOffensive(int id, List<String> words) throws NullPointerException, IllegalArgumentException {
		checkRep();
		checkSaferRep();
		
		// Se uno dei parametri è null, solleva un'eccezione
		if (words == null) {
			throw new NullPointerException();
		}
		
		Post post = postLookup(id);
		
		// Se nella rete sociale non vi è alcun post il cui id corrisponde al parametro passato, solleva un'eccezione
		if (post == null) {
			throw new IllegalArgumentException();
		}
		
		// Se il post contiene almeno una delle parole all'interno della lista data come parametro, aggiungilo all'insieme dei post segnalati e restituisci true
		if (isOffensive(post, words)) {
			reportedPosts.add(post);
			
			checkSaferRep();
			return true;
		}
		
		checkRep();
		checkSaferRep();
		return false;
	}
	/*
	 * REQUIRES: (words != null) && (esiste p appartenente a postMap.values() . p.getId() == id)
	 * THROWS: words == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   se !(postMap.values().contains(p)), solleva una IllegalArgumentException (eccezione disponibile in Java, unchecked)
	 * MODIFIES: this
	 * EFFECTS: se esiste una stringa w appartenente a words. w è sottostringa di p.getText() dove p appartiene a this(Post) && p.getId() == id, 
	 * 				this(ReportedPosts) = this(ReportedPosts) U {p} && restituisce true
	 * 			altrimenti restituisce false
	 */
	
	// Restituisce l'insieme dei post che sono stati segnalati per contenuti offensivi
	public Set<Post> getReportedPosts() {
		checkRep();
		checkSaferRep();
		
		// Copy-out perché HashSet è una struttura dati mutable
		return new HashSet<Post>(reportedPosts);
	}
	/*
	 * EFFECTS: restituisce un insieme I di post . per ogni p appartenente a I, p appartiene a this(ReportedPosts)
	 */
	
	// Metodo privato che prende come parametro d'ingresso un insieme di post e ne restituisce una copia senza post con contenuti offensivi
	private Set<Post> cleanUpPosts(Set<Post> ps) throws NullPointerException {
		checkRep();
		checkSaferRep();
		
		// Se il parametro è null, solleva un'eccezione
		if (ps == null) {
			throw new NullPointerException();
		}
		
		Set<Post> res = new HashSet<Post>(ps);
		
		// Rimuovo dall'insieme tutti i post che sono stati segnalati per contenuti offensivi
		for (Post p : ps) {
			if (reportedPosts.contains(p)) {	// Se il post appartiene a reportedPosts, significa che è stato segnalato
				res.remove(p);
			}
		}
		
		checkRep();
		checkSaferRep();
		return res;
	}
	/*
	 * REQUIRES: ps != null
	 * THROWS: se ps == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce un insieme I di post . per ogni p appartenente a I, p appartiene a ps && p non appartiene a this(ReportedPosts)
	 */
	
	// Restituisce la mappa che associa ogni autore ai post non offensivi da egli creati
	public Map<String, Set<Post>> getSaferPostMap() {
		checkRep();
		checkSaferRep();
		
		// Ottengo la mappa di tutti i post presenti nella rete sociale (inclusi quelli segnalati)
		Map<String, Set<Post>> res = new HashMap<String, Set<Post>>(getPostMap());
		
		// Rimuovo dalla mappa tutti i post che sono stati segnalati per contenuti offensivi
		for (String u : res.keySet()) {
			Set<Post> newSet = cleanUpPosts(res.get(u));
			res.put(u, newSet);		// Aggiorna la mappa da restituire con il nuovo insieme
		}
		
		checkRep();
		checkSaferRep();	
		return res;
	}
	/*
	 * EFFECTS: restituisce una mappa M<Autore, Set<Post>> . per ogni p appartente a M.get(a), p.getAuthor().equals(a) && p non appartiene a this(ReportedPosts)
	 */
	
	// Restituisce la lista dei post non offensivi creati dall'autore nella rete sociale il cui nome è dato dal parametro username
	public List<Post> saferWrittenBy(String username) throws NullPointerException {
		checkRep();
		checkSaferRep();
		
		// Se il parametro è null, solleva un'eccezione
		if (username == null) {
			throw new NullPointerException();
		}
		
		if (getSaferPostMap().containsKey(username)) {
			
			return new ArrayList<Post>(getSaferPostMap().get(username)); 
		}
				
		// Se l'autore non è presente nella postMap significa che non ha creato alcun post, quindi restituisci una lista vuota
		else {
			return new ArrayList<Post>();
		}
	}
	/*
	 * REQUIRES: username != null
	 * THROWS: se ps == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce una lista L di post . per ogni p appartenente a L, p.getAuthor().equals(username) && p non appartiene a this(ReportedPosts)
	 */
	
	// Metodo privato che verifica che l'invariante di rappresentazione sia rispettata
	private void checkSaferRep() throws NullPointerException {	
		// Verifica che l'insieme dei post segnalati non sia uguale a null
		if(reportedPosts == null) {
			throw new BrokenInvariantException();
		};
		
		// Verifica che ogni post segnalato abbia un indice diverso
		List<Post> list = new ArrayList<Post>();
		for (Post p : reportedPosts) {
			list.add(p);
		}
		
		// Se il metodo ritorna false, significa che ci sono almeno due post con lo stesso id, quindi la RI è stata violata
		if (!postRepOk(list)) {
			throw new BrokenInvariantException();
		}
	}
	/*
	 * REQUIRES: (reportedPosts != null) &&	(per ogni x, y . (x != y && reportedPosts.contains(x) && reportedPosts.contains(y)) -> x.getId() != y.getId())
	 * THROWS: se !(reportedPosts != null) || !((per ogni x, y . (x != y && reportedPosts.contains(x) && reportedPosts.contains(y)) -> x.getId() != y.getId())), 
	 * 		   solleva una BrokenInvariantException (eccezione non disponibile in Java, unchecked)
	 */
}
