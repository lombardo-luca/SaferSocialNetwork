// Luca Lombardo, mat. 546688

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class SocialNetwork {
	/*
	 * OVERVIEW: struttura dati modificabile rappresentante una rete sociale. Consente agli utenti di inviare messaggi lunghi al più 140 caratteri e di lasciare "like" ai post degli altri utenti. 
	 * 		     Un "like" è un particolare post p1 il cui testo è nel formato: "like:id", dove id è un numero intero corrispondente all'id di un qualche altro post p2. 
	 * 			 Se l'autore del post p1 mette un "like" a un post p2, egli incomincia a "seguire" l'autore di p2 (ne diventa un "follower"). 
	 * 			 Gli utenti non possono seguire sé stessi, di conseguenza non è permesso mettere like a un proprio post.
	 * 
	 * TYPICAL ELEMENT: una coppia <Post, Followers> dove:
	 * 					Post è l'insieme dei post presenti nella rete sociale, dove ogni post è una quadrupla <id, author, text, timestamp>;
	 * 					Followers è l'insieme delle coppie <Autore1, Autore2> tali che Autore1 segue (è un follower di) Autore2 nella rete sociale.
	 * 
	 * AF: <(String -> {Post_1, ..., Post_n}), (String -> {String_1, ..., String_n})> -> <{Post_1, ..., Post_n}, {<String_1-1, String_1-2>, ..., <String_n-1, String_n-2>}>
	 * 	   Associa la Map<String, Set<Post>> all'insieme dei post di ogni utente
	 * 	   Associa la Map<String, Set<String>> all'insieme di coppie <Autore, Followed> 
	 * 
	 * RI: (postMap != null) && (followedMap != null) && (followersMap != null) && (numPost >= 0) && 											
	 * 	   (per ogni x, y . (x != y && postMap.containsValue(x) && postMap.containsValue(y)) -> x.getId() != y.getId()) && 
	 * 	   (per ogni x . followedMap.containsKey(x) -> per ogni y appartenente a followedMap.get(x) . x != y) && 
	 * 	   (per ogni x . followersMap.containsKey(x) -> per ogni y appartenente a followersMap.get(x) . x != y)
	 */
	
	private int numPost;							// numero di post all'interno della rete sociale, sempre >= 0
	private Map<String, Set<Post>> postMap; 	    // postMap.get(a) -> insieme dei post creati dall'autore a
	private Map<String, Set<String>> followedMap;	// followedMap.get(a) -> insieme degli autori seguiti dall'autore a
	private Map<String, Set<String>> followersMap;  // followersMap.get(a) -> insieme degli autori che seguono l'autore a
	
	// Metodo costruttore
	public SocialNetwork() {
		numPost = 0;
		postMap = new HashMap<String, Set<Post>>();
		followedMap = new HashMap<String, Set<String>>(); 
		followersMap = new HashMap<String, Set<String>>();
		
		checkRep();
	}
	/*
	 * EFFECTS: restituisce un nuovo oggetto sn di tipo SocialNetwork tale che sn(Post) ed sn(Followers) sono insiemi vuoti
	 */
	
	/* Crea un nuovo post con autore e contenuto dati rispettivamente dai parametri author e text.
	   Il timestamp del post corrisponde a data e ora al momento della chiamata, mentre l'id viene assegnato in modo incrementale tramite la variabile di stato numPost. 
	   Se il parametro text è nel formato "like:id" con id corrispondente all'id di un post all'interno della rete sociale, il post creato viene interpretato come un "like" a tale post; 
	   in questo caso, author diventa follower dell'autore del post */
	public Post createPost(String author, String text) throws NullPointerException, IllegalArgumentException {
		checkRep();
		
		// Se uno dei parametri è null, solleva un'eccezione
		if (author == null || text == null) {
			throw new NullPointerException();
		}
		
		numPost++;														// Aggiorna il numero di post della rete sociale
		Post post = new Post(numPost, author, text, new Date());		// Crea il nuovo post
		
		// Controlla se il post è un like
		if (text.startsWith("like:")) {
			int id = Integer.parseInt(text.substring(5));
			Post p = postLookup(id);
			
			// Controlla se l'id corrisponde a un post presente nella rete sociale
			if (p != null) {
				
				// Controlla se author sta cercando di mettere "like" a un proprio post
				if (!p.getAuthor().equals(author)) {
					Set<String> newSet;
					
					// Se author è già presente nella followedMap (quindi ha già seguito almeno un autore), ottieni l'insieme degli autori da lui seguiti
					if (followedMap.get(author) == null) {
						newSet = new HashSet<String>();
					}
					
					// Se invece author non ha ancora seguito alcun autore, crea un nuovo insieme
					else {
						newSet = followedMap.get(author);
					}
					
					// Aggiungi l'autore del post che ha ricevuto il like all'insieme degli autori seguiti da author
					newSet.add(p.getAuthor());
					followedMap.put(author, newSet);	// Aggiorna la followedMap
					
					Set<String> newSet2;
					
					// Se l'autore del post che ha ricevuto il like è già presente nella followersMap (quindi ha già almeno un follower), ottieni l'insieme dei suoi follower
					if (followersMap.get(p.getAuthor()) == null) {
						newSet2 = new HashSet<String>();
					}
					
					// Se invece l'autore del post che ha ricevuto il like non ha ancora followers, crea un nuovo insieme
					else {
						newSet2 = new HashSet<String>(followersMap.get(p.getAuthor()));
					}
					
					// Aggiungi author all'insieme degli autori che seguono l'autore del post che ha ricevuto il like
					newSet2.add(author);
					followersMap.put(p.getAuthor(), newSet2);	// Aggiorna la followersMap
				}	
			}	
		}
		
		Set<Post> newSet;
		
		// Se author è già presente nella postMap (quindi ha già creato almeno un post), ottieni l'insieme dei suoi post
		if (postMap.containsKey(author)) {
			newSet = postMap.get(author);
		}
			
		// Se invece è il primo post dell'utente author, crea un nuovo insieme di post
		else {
			newSet = new HashSet<Post>();
		}
		
		// Aggiungi il post all'insieme dei post creati da author
		newSet.add(post);
		postMap.put(author, newSet);	// Aggiorna la postMap
		
		checkRep();
		
		// Non è necessario il copy-out poiché Post è una struttura dati non modificabile
		return post;
	}
	/*
	 * REQUIRES: (author != null) && (text != null) && !(0 < text.length() <= 140)
	 * THROWS: se author == null || text == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   se !(0 < text.length() <= 140), solleva una IllegalArgumentException (eccezione disponibile in Java, unchecked)
	 * MODIFIES: this
	 * EFFECTS: this(Post) = this(Post) U {p} dove p è un post . p.getAuthor().equals(author) && p.getText().equals(text) && p.getTimestamp() è la data e l'ora al momento della chiamata
	 *   		&& se (text.equals("like:id") && esiste p appartenente a this(Post) . p.getId() == id) -> this(Followers) = this(Followers) U {<author, author(p)}  
	 *   		&& restituisce p
	 */
	
	// Metodo osservatore che restituisce il numero di post presenti all'interno della rete sociale (inclusi i like)
	public int getNumPost() {
		checkRep();
		
		return numPost;
	}
	/*
	* EFFECTS: restituisce il numero di post presenti nella rete sociale, ovvero |this(Post)|
	*/
	
	// Metodo osservatore che restituisce la mappa che associa ogni autore ai post da egli creati
	public Map<String, Set<Post>> getPostMap() {
		checkRep();
		
		return new HashMap<String, Set<Post>>(postMap);	// Copy-out per proteggere la rappresentazione interna di postMap
	}
	/*
	* EFFECTS: restituisce una mappa M<Autore, Set<Post>> . per ogni p appartenente a M.get(a) -> p.getAuthor().equals(a)
	*/
	
	// Metodo osservatore che restituisce la mappa che associa ogni autore agli autori da egli seguiti
	public Map<String, Set<String>> getFollowedMap(){
		checkRep();
		
		return new HashMap<String, Set<String>>(followedMap); // Copy-out per proteggere la rappresentazione interna di followedMap
	}
	/*
	* EFFECTS: restituisce una mappa M<Autore, Set<Followed>> . per ogni f appartenente a M.get(a), a è un follower di f
	*/
	
	// Metodo osservatore che restituisce la mappa che associa ogni autore agli autori che lo seguono
	public Map<String, Set<String>> getFollowersMap(){
		checkRep();
		
		return new HashMap<String, Set<String>>(followersMap); // Copy-out per proteggere la rappresentazione interna di followersMap
	}
	/*
	* EFFECTS: restituisce una mappa M<Autore, Set<Followers>> . per ogni f appartenente a M.get(a), f è un follower di a
	*/
	
	// Metodo protected che ricerca un post il cui id è dato dal parametro omonimo; se lo trova, lo restituisce, altrimenti restituisce null
	protected Post postLookup(int id) {
		checkRep();
		
		Iterator<String> it = postMap.keySet().iterator();
		
		// Cerca il post nella rete sociale
		while (it.hasNext()) {
			String user = it.next();
				
			Iterator<Post> it2 = postMap.get(user).iterator();
			
			while (it2.hasNext()) {
				Post p = it2.next();
				
				if (p.getId() == id) {
					
					checkRep();
					// Copy-out non necessario perché Post è una struttura dati non modificabile
					return p;
				}
			}
		}
		
		checkRep();
		
		// Se il post p non è presente nella rete sociale, ritorna null
		return null;
	}
	/*
	 * EFFECTS: se esiste p appartenente a this(Post) . p.getId() == id -> ritorna p
	 * 			altrimenti -> ritorna null
	 */
	
	// Verifica se l'autore il cui nome è indicato dal parametro follower segue l'autore il cui nome è indicato dal parametro followed
	public boolean isFollower(String follower, String followed) throws NullPointerException {
		checkRep();
		
		// Se uno dei parametri è null, solleva un'eccezione
		if (follower == null || followed == null) {
			throw new NullPointerException();
		}
		
		// Ottieni l'insieme degli autori seguiti da follower
		Set<String> set = followedMap.get(follower);
		
		checkRep();
		
		if (set != null && set.contains(followed)) {
			return true;
		}
		
		else {
			return false;
		}
	}
	/*
	 * REQUIRES: (follower != null) && (followed != null)
	 * THROWS: se follower == null || followed == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce true se la coppia <follower, followed> appartiene a this(Followers), false altrimenti
	 */

	// Prende come parametro il nome di un autore nella rete sociale e restituisce l'insieme di tutti gli autori che lo seguono
	public Set<String> getFollowers(String username) throws NullPointerException {
		checkRep();
		
		// Se il parametro è null, solleva un'eccezione
		if (username == null) {
			throw new NullPointerException();
		}
		
		// Ottieni l'insieme degli autori che seguono l'autore di nome username
		Set<String> res = followersMap.get(username);
		
		checkRep();
		
		// Se nessun autore segue username, restituisci un insieme vuoto
		if (res == null) {
			return new HashSet<String>();
		}

		else {
			return new HashSet<String>(res);	// Copy-out perché HashSet è una struttura dati modificabile
		}
	}
	/*
	 * REQUIRES: username != null
	 * THROWS: se username == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce un insieme I di stringhe . per ogni s appartenente a I, la coppia <s, username> appartiene a this(Followers)
	 */
	
	// Prende come parametro il nome di un autore nella rete sociale e restituisce una l'insieme di tutti gli autori da egli seguiti
	public Set<String> getFollowed(String username) throws NullPointerException {
		checkRep();
		
		// Se il parametro è null, solleva un'eccezione
		if (username == null) {
			throw new NullPointerException();
		}
		
		// Ottieni l'insieme degli autori seguiti da username
		Set<String> res = followedMap.get(username);
		
		checkRep();
		
		// Se username non segue alcun autore, restituisci un insieme vuoto
		if (res == null) {
			return new HashSet<String>();
		}

		else {
			return new HashSet<String>(res);	// Copy-out perché HashSet è una struttura dati modificabile
		}
	}
	/*
	 * REQUIRES: username != null
	 * THROWS: se username == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce un insieme I di stringhe . per ogni s appartenente a I, la coppia <username, s> appartiene a this(Followers)
	 */
	
	// Metodo statico che costruisce una rete sociale <Autore, Set<Follower>> derivata dalla lista dei post data come parametro, utilizzando i "like" come criterio
	public static Map<String, Set<String>> guessFollowers(List<Post> ps) throws NullPointerException, IllegalArgumentException {
		// Se il parametro è null, solleva un'eccezione
		if (ps == null) {
			throw new NullPointerException();
		}
		
		// Se la lista data come parametro non rispetta la proprietà di univocità degli id, solleva un'eccezione
		if (!postRepOk(ps)) {
			throw new IllegalArgumentException();
		}

		Map<String, Set<String>> res = new HashMap<String, Set<String>>();
		
		// Itera sulla lista di post data come parametro
		for (Post p : ps) {
			String author = p.getAuthor();
			String text = p.getText();
			
			// Se il post è un like...
			if (text.startsWith("like:")) {
				boolean found = false;
				int id = Integer.parseInt(text.substring(5));
				Iterator<Post> it = ps.iterator();
				
				// ...cerca il post che ha ricevuto il like
				while (it.hasNext() && !found) {
					Post p2 = it.next();
					
					if (p2.getId() == id && !author.equals(p2.getAuthor())) {
						found = true;
						Set<String> followers;
						
						if (res.get(p2.getAuthor()) == null) {
							followers = new HashSet<String>();
						}
								
						else {
							followers = new HashSet<String>(res.get(p2.getAuthor()));
						}
								
						// Inserisci nella map l'autore del like come follower dell'autore di p2
						followers.add(author);
						res.put(p2.getAuthor(), followers);	
					}
				}
			}
			
			// Se non è già presente, inserisci nella map l'autore del post e associagli un insieme vuoto di followers
			if (!res.keySet().contains(author)) {
				Set<String> newSet = new HashSet<String>();
				res.put(author, newSet);
			}
		}
		
		return res;
	}
	/*
	 * REQUIRES: (ps != null) && (per ogni p1, p2 appartenenti a ps, p1 != p2 -> p1.getId() != p2.getId())
	 * THROWS: se ps == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   se !(per ogni p1, p2 appartenenti a ps, p1 != p2 -> p1.getId() != p2.getId()), solleva una IllegalArgumentException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce una mappa M<Autore, Set<Follower>> tale che:
	 * 			per ogni f appartenente a M.get(a), esistono post p1, p2 appartenenti a ps. p1.getAuthor().equals(f) && p2.getAuthor().equals(a) && !(a.equals(f)) && p1.getText().equals("like:id") con id == p2.getId()
	 */
	
	// Restituisce una rete sociale <Autore, Set<Followed>> derivata dalla lista dei post data come parametro,  utilizzando i "like" come criterio
	public static Map<String, Set<String>> guessFollowed(List<Post> ps) throws NullPointerException, IllegalArgumentException {
		// Se il parametro è null, solleva un'eccezione
		if (ps == null) {
			throw new NullPointerException();
		}
		
		// Se la lista data come parametro rispetta la proprietà di univocità degli id, solleva un'eccezione
		if (!postRepOk(ps)) {
			throw new IllegalArgumentException();
		}

		Map<String, Set<String>> res = new HashMap<String, Set<String>>();
		
		// Itera sulla lista di post data come parametro
		for (Post p : ps) {
			String author = p.getAuthor();
			String text = p.getText();
			
			// Se il post è un like...
			if (text.startsWith("like:")) {
				boolean found = false;
				int id = Integer.parseInt(text.substring(5));
				Iterator<Post> it = ps.iterator();
				
				// ...cerca il post che ha ricevuto il like
				while (it.hasNext() && !found) {
					Post p2 = it.next();
					
					if (p2.getId() == id && !author.equals(p2.getAuthor())) {
						found = true;
						Set<String> followed;
						
						if (res.get(author) == null) {
							followed = new HashSet<String>();
						}
								
						else {
							followed = new HashSet<String>(res.get(author));
						}
								
						// Inserisci nella map l'autore di p2 come utente seguito dall'autore del like
						followed.add(p2.getAuthor());
						res.put(author, followed);	
					}
				}
			}
			
			// Se non è già presente, inserisci nella map l'autore del post e associagli un insieme vuoto di utenti seguiti
			if (!res.keySet().contains(author)) {
				Set<String> newSet = new HashSet<String>();
				res.put(author, newSet);
			}
		}
		
		return res;
	}
	/*
	 * REQUIRES: ps != null && (per ogni p1, p2 appartenenti a ps, p1 != p2 -> p1.getId() != p2.getId())
	 * THROWS: se ps == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   se !(per ogni p1, p2 appartenenti a ps, p1 != p2 -> p1.getId() != p2.getId()), solleva una IllegalArgumentException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce una mappa M<Autore, Set<Followed>> tale che: 
	 * 			per ogni f appartenente a M.get(a), esistono post p1, p2 appartenenti a ps. p1.getAuthor().equals(a) && p2.getAuthor().equals(f) && !(a.equals(f)) && p1.getText().equals("like:id") con id == p2.getId()
	 */
	
	// Restituisce una lista di autori nella rete sociale ordinata in maniera decrescente per numero di followers
	public List<String> influencers() {
		checkRep();
		
		Set<String> set = followersMap.keySet();
		
		if (set == null) {
			return new ArrayList<String>();
		}
		
		// Creo una map e ci inserisco autori e rispettivo numero di followers
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		for (String u : set) {
			int size = followersMap.get(u).size();
			map.put(u, size);
		}
		
		// Trasformo la map precedente in una lista ordinata per valore
		List<Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue());
		
		// Creo la lista finale e la riempio con le chiavi della map, in ordine inverso
		List<String> res = new ArrayList<String>();
		
		for (int i = list.size() - 1; i >= 0; i--) {
			res.add(list.get(i).getKey());
		}
		
		checkRep();
		return res;
	}
	/*
	 * EFFECTS: restituisce una lista L di stringhe s . per ogni s1, s2 appartenente a L, L.indexOf(s1) < L.indexOf(s2) -> l'autore s1 ha più followers dell'autore s2
	 * 			(ovvero la cardinalità del sottoinsieme F1 di this(Followers) delle coppie <s, s1> è maggiore della cardinalità del sottoinsieme F2 di this(Followers) delle coppie <s, s2>)
	 */
	
	// Restituisce l'insieme degli autori menzionati (inclusi) nei post presenti nella rete sociale. Un autore è menzionato se è il creatore di almeno un post nella rete sociale
	public Set<String> getMentionedUsers() {
		checkRep();
		
		Set<String> res = new HashSet<String>();
		
		// Itera su tutti gli autori nella rete sociale
		for (String u : postMap.keySet()) {
			Set<String> set = getMentionedUsers(writtenBy(u));	// Ottieni gli autori menzionati nei post scritti da u
			
			// Aggiungi gli autori menzionati all'insieme da restituire come risultato
			for (String s : set) {
				if (!res.contains(s)) {
					res.add(s);
				}
			}
		}
		
		checkRep();
		return res;
	}
	/*
	 * EFFECTS: restituisce un insieme I di stringhe . per ogni s appartenente a I, esiste p appartenente a this(Post) . p.getAuthor().equals(s)
	 */
	
	// Restituisce l'insieme degli autori menzionati (inclusi) nella lista di post data come parametro. Un autore è menzionato se è il creatore di almeno un post all'interno della lista.
	public static Set<String> getMentionedUsers(List<Post> ps) throws NullPointerException, IllegalArgumentException {	
		// Se il parametro è null, solleva un'eccezione
		if (ps == null) {
			throw new NullPointerException();
		}
		
		// Se la lista data come parametro non rispetta la proprietà di univocità degli id, solleva un'eccezione
		if (!postRepOk(ps)) {
			throw new IllegalArgumentException();
		}
		
		Set<String> res = new HashSet<String>();

		// Itera su ogni post della lista data come parametro
		for (Post p : ps) {
			String author = p.getAuthor();
			
			// Aggiungi l'autore del post all'insieme da restituire come risultato
			res.add(author);	
		}
			
		return res;
	}
	/*
	 * REQUIRES: (ps != null) && (per ogni p1, p2 appartenenti a ps, p1 != p2 -> p1.getId() != p2.getId())
	 * THROWS: se ps == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   se !(per ogni p1, p2 appartenenti a ps, p1 != p2 -> p1.getId() != p2.getId()), solleva una IllegalArgumentException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce un insieme I di stringhe . per ogni s appartenente a I, esiste p appartenente a ps . p.getAuthor().equals(s)
	 */
	
	// Restituisce la lista dei post creati dall'autore nella rete sociale il cui nome è dato dal parametro username
	public List<Post> writtenBy(String username) throws NullPointerException {	
		checkRep();
		
		// Se il parametro è null, solleva un'eccezione
		if (username == null) {
			throw new NullPointerException();
		}
		
		if (postMap.containsKey(username)) {
			return new ArrayList<Post>(postMap.get(username));	// Copy-out per proteggere la rappresentazione interna di postMap
		}
		
		// Se l'autore non è presente nella postMap significa che non ha creato alcun post, quindi restituisci una lista vuota
		else {
			return new ArrayList<Post>();
		}
	}
	/*
	 * REQUIRES: username != null
	 * THROWS: se username == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce una lista L di post . per ogni p appartenente a L, p.getAuthor().equals(username)
	*/
	
	// Restituisce i post all'interno della lista ps creati dall'autore il cui nome è dato dal parametro username
	public static List<Post> writtenBy(List<Post> ps, String username) throws NullPointerException, IllegalArgumentException {
		// Se uno dei parametri è null, solleva un'eccezione
		if (username == null || ps == null) {
			throw new NullPointerException();
		}
		
		// Se la lista di post data come parametro non rispetta la proprietà di univocità degli id, solleva un'eccezione
		if (!postRepOk(ps)) {
			throw new IllegalArgumentException();
		}
		
		List<Post> res = new ArrayList<Post>();
		
		// Itera su ogni post della lista data come parametro
		for (Post p: ps) {
			
			// Se l'autore del post corrisponde al parametro username, aggiungilo alla lista da restituire come risultato
			if (p.getAuthor().equals(username)) {
				res.add(p);
			}
		}
			
		return res;
	}
	/*
	 * REQUIRES: (username != null) && (ps != null) && (per ogni p1, p2 appartenenti a ps, p1 != p2 -> p1.getId() != p2.getId())
	 * THROWS: se username == null o ps == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * 		   se !(per ogni p1, p2 appartenenti a ps, p1 != p2 -> p1.getId() != p2.getId()), solleva una IllegalArgumentException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce una lista L di post . per ogni p appartenente a L, p appartiene a ps && p.getAuthor().equals(username)
	 */
	
	// Restituisce una lista contenente i post presenti nella rete sociale che includono almeno una delle parole presenti nella lista data come argomento (non case-sensitive)
	public List<Post> containing(List<String> words) throws NullPointerException {
		checkRep();
		
		// Se il parametro è null, solleva un'eccezione
		if (words == null) {
			throw new NullPointerException();
		}
		
		List<Post> res = new ArrayList<Post>();
		
		// Itera su tutti i post presenti nella rete sociale
		for (String s : postMap.keySet()) {
			for (Post p : postMap.get(s)) {
				
				// Se il post contiene almeno una delle parole all'interno della lista data come parametro, aggiungilo alla lista da restituire come risultato
				if (contains(p, words)) {
					res.add(p);		
				}
			}
		}
		
		checkRep();
		return res;
	}
	/*
	 * REQUIRES: words != null
	 * THROWS: se words == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce una lista L di post . per ogni p appartenente a L, esiste una stringa w appartenente a words . w è sottostringa di p.getText()
	 */
	
	// Metodo protected che restituisce true se il post dato come parametro contiene almeno una delle parole nella lista data come parametro (non case-sensitive), false altrimenti
	protected static boolean contains(Post p, List<String> words) throws NullPointerException {
		// Se uno dei parametri è null, solleva un'eccezione
		if (p == null || words == null) {
			throw new NullPointerException();
		}
		
		// Itera su tutte le parole all'interno della lista data come parametro
		for (String w : words) {
			if (p.getText().toLowerCase().contains(w.toLowerCase())) {
				return true;
			}
		}
		
		return false;
	}
	/*
	 * REQUIRES: (p != null) && (words != null)
	 * THROWS: se p == null || words == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce true se esiste una stringa w appartenente a words . w è una sottostringa di p.getText(), false altrimenti
	 */
	
	// Metodo protected statico che controlla se la proprietà di univocità degli identificatori è verificata all'interno di una lista di post data come parametro
	protected static boolean postRepOk(List<Post> ps) throws NullPointerException {
		// Se il parametro è null, solleva un'eccezione
		if (ps == null) {
			throw new NullPointerException();
		}
		
		// Se la lista di post data come parametro è vuota, la proprietà è verificata (vacuamente vera)
		if (ps.size() == 0) {
			return true;
		}
		
		ArrayList<Integer> idList = new ArrayList<Integer>();	// Crea una lista in cui inserire gli id di tutti i post presenti all'interno della lista data come parametro
		
		// Itera su ogni post all'interno della lista data come parametro
		for (Post p : ps) {
			int id = p.getId();
			
			// Se l'id del post è già presente nella lista, significa che vi è un id duplicato, quindi la proprietà non è verificata
			if (idList.contains(id)) {
				return false;
			}
				
			else {
				idList.add(id);
			}
		}
			
		// Se non vi sono id duplicati, la proprietà è verificata
		return true;
	}
	/*
	 * REQUIRES: ps != null
	 * THROWS: se ps == null, solleva una NullPointerException (eccezione disponibile in Java, unchecked)
	 * EFFECTS: restituisce true se per ogni p1, p2 appartenenti a ps, p1 != p2 -> p1.getId() != p2.getId(), false altrimenti
	 */
	
	// Metodo protected che verifica che l'invariante di rappresentazione sia rispettata
	protected void checkRep() throws BrokenInvariantException {
		// Verifica che le variabili di istanza abbiano valori legali
		if(postMap == null || followedMap == null || followersMap == null || numPost < 0) {
			throw new BrokenInvariantException();
		};
		
		// Verifica che ogni post nella rete sociale abbia un indice diverso
		for (String s : postMap.keySet()) {
			List<Post> list = new ArrayList<Post>(postMap.get(s));
			
			// Se il metodo ritorna false, significa che vi sono almeno due post con lo stesso id, quindi la RI è stata violata
			if (!postRepOk(list)) {
				throw new BrokenInvariantException();
			}
		}
		
		// Verifica che nessun autore nella rete sociale segua (o sia seguito) da sé stesso
		for (String u : followedMap.keySet()) {
			for (String f : followedMap.get(u)) {
				if (u.equals(f)) {
					throw new BrokenInvariantException();
				}
			}
		}
		
		for (String u : followersMap.keySet()) {
			for (String f : followersMap.get(u)) {
				if (u.equals(f)) {
					throw new BrokenInvariantException();
				}
			}
		}
	}
	/*
	 * REQUIRES: RI
	 * THROWS: se !RI, solleva una BrokenInvariantException (eccezione non disponibile in Java, unchecked)
	 */
}
