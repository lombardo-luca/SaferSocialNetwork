// Luca Lombardo, mat. 546688

import java.util.Date;
import java.util.Objects;

public final class Post {
	/*
	 * OVERVIEW: tipo di dato non modificabile rappresentante un singolo post all'interno di una rete sociale.
	 * 
	 * TYPICAL ELEMENT: una quadrupla <id, author, text, timestamp> dove:
	 * 					id è un intero che identifica univocamente il post;
	 * 					author è una stringa identificante l'autore del post;
	 * 					text è una stringa rappresentante il contenuto del post (un testo non vuoto di massimo 140 caratteri);
	 * 					timestamp è una stringa rappresentante la data e l'ora della creazione del post.
	 * 
	 * AF: (int, String, String, Date) -> (int, String, String, String) 
	 * 	   Associa l'argomento di tipo Date alla sua rappresentazione come stringa
	 * 	   Si comporta da identità sugli altri argomenti
	 * 
	 * RI: (text != null) && (author != null) && (timestamp != null) && (id > 0) && (0 < text.length() <= 140)
	 */
	
	private final int id;			// identificatore univoco del post, sempre > 0
	private final String author;	// nome dell'autore del post
	private final String text;		// contenuto del post, compreso tra 1 e 140 caratteri
	private final Date timestamp;	// data e ora della creazione del post
	
	// Metodo costruttore 
	public Post(int id, String author, String text, Date timestamp) throws NullPointerException, IllegalArgumentException {
		// Se uno dei parametri è null, solleva un'eccezione
		if (author == null || text == null || timestamp == null) {
			throw new NullPointerException();
		}
		
		// Se il parametro text è vuoto oppure ha una lunghezza maggiore di 140 caratteri, solleva un'eccezione
		else if (text.length() > 140 || text.length() == 0 || id <= 0) {
			throw new IllegalArgumentException();
		}
		
		else {
			this.id = id;
			this.author = author;
			this.text = text;
			this.timestamp = (Date) timestamp.clone();
		}
		
		checkRep();
	}
	/*
	 * REQUIRES: (author != null) && (text != null) && (timestamp != null) && (0 < text.length() <= 140)
	 * THROWS: se author == null || text == null || timestamp == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   se !(0 < text.length() <= 140) || id < 0, solleva una IllegalArgumentException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce un nuovo post p = <id, author, text, timestamp>
	 */
	
	// Metodo osservatore che restituisce l'id del post
	public int getId() {
		checkRep();
		
		return id;
	}
	/*
	 * EFFECTS: restituisce l'id di this
	 */
	
	//  Metodo osservatore che restituisce l'autore del post
	public String getAuthor() {
		checkRep();
		
		return author;
	}
	/*
	 * EFFECTS: restituisce l'autore di this
	 */
	
	// Metodo osservatore che restituisce il testo (contenuto) del post
	public String getText() {
		checkRep();
		
		return text;
	}
	/*
	 * EFFECTS: restituisce il testo di this
	 */
	
	// Metodo osservatore che restituisce data e ora di creazione del post
	public Date getTimestamp() {
		checkRep();
		
		// Copy-out perché Date è una struttura dati modificabile
		Date res = (Date) timestamp.clone();
		
		return res;
	}
	/*
	 * EFFECTS: restituisce data e ora di this
	 */
	
	// Sovrascrive il metodo equals della classe Object. Restituisce true se l'oggetto dato come parametro è un Post con lo stesso id di this
	@Override
	public boolean equals(Object obj) {
		// Se l'oggetto dato come parametro è null oppure non è di tipo Post, restituisci false
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
			
		// Se l'oggetto dato come parametro è esattamente this
		if (obj == this) {
			return true;
		}
		
		Post p = (Post) obj;
		return (p.getId() == id);	// Restituisci true se l'oggetto dato come parametro ha lo stesso id di this, false altrimenti
	}
	/*
	 * EFFECTS: restituisce true se this è uguale ad obj, false altrimenti
	 */
	
	// Sovrascrive il metodo hashCode della classe Object. Restituisce un intero rappresentante il valore hash di this
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	/*
	 * EFFECTS: restituisce il valore hash di this, calcolato dall'identificatore univoco del post
	 */
	
	// Metodo privato che verifica che l'invariante di rappresentazione sia rispettata
	private void checkRep() throws BrokenInvariantException {
		if (text == null || author == null || timestamp == null || id < 0 || text.length() < 0 || text.length() > 140) {
			throw new BrokenInvariantException();
		}
	}
	/*
	 * REQUIRES: RI
	 * THROWS: se !RI, solleva una BrokenInvariantException (eccezione non disponibile in Java, unchecked)
	 */
}
