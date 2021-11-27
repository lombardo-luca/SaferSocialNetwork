// Luca Lombardo, mat. 546688

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Test {
		public static boolean testPost() {
			System.out.println("----- TEST DELLA CLASSE Post -----\n");
			
			// Testo il metodo costruttore
			System.out.println("Test del metodo costruttore...");
			
			int i = 1;
			int j = 5;
			
			// Testo il costruttore con tutti gli argomenti validi
			Post p;
			try {
				p = new Post(1, "Autore di prova", "Contenuto di prova", new Date());
				System.out.printf("%d/%d: OK\n", i, j);
			} catch (NullPointerException | IllegalArgumentException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				e.printStackTrace();
				return false;
			}
			
			// Testo il costruttore con un argomento == null
			i = 2;
			try {
				p = new Post(2, null, "Contenuto di prova", new Date());
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			} catch (IllegalArgumentException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				e.printStackTrace();
				return false;	
			}
			
			// Testo il costruttore con un testo più lungo di 140 caratteri
			i = 3;
			String text = "testolungopiudi140caratteritestolungopiudi140caratteritestolungopiudi140caratteritestolungopiudi140caratteritestolungopiudi140caratteritestolungopiudi140caratteri";
			try {
				p = new Post(3, "Autore di prova", text, new Date());
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				e.printStackTrace();
				return false;
			} catch (IllegalArgumentException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}
			
			// Testo il costruttore con un testo vuoto
			i = 4;
			try {
				p = new Post(4, "Autore di prova", "", new Date());
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				e.printStackTrace();
				return false;
			} catch (IllegalArgumentException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}
			
			// Testo il costruttore con un id illegale
			i = 5;
			try {
				p = new Post(-5, "Autore di prova", "Contenuto di prova", new Date());
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				e.printStackTrace();
				return false;
			} catch (IllegalArgumentException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}		
			
			// Testo i metodi osservatori
			System.out.println("\nTest dei metodi osservatori...");
			
			i = 1;
			j = 4;
			
			Date d = new Date();
			Post p2 = new Post(6, "Autore", "Contenuto", d);
			
			assert(p2.getId() == 6);
			System.out.printf("%d/%d: OK\n", i, j);
			
			i = 2;
			assert(p2.getAuthor().equals("Autore"));
			System.out.printf("%d/%d: OK\n", i, j);
			
			i = 3;
			assert(p2.getText().equals("Contenuto"));
			System.out.printf("%d/%d: OK\n", i, j);
			
			i = 4;
			assert(p2.getTimestamp().equals(d));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo equals e hashCode
			System.out.println("\nTest di equals e hashCode...");
			i = 1;
			j = 2;
			
			p = new Post(1, "Autore1", "Contenuto1", d);
			p2 = new Post(1, "Autore2", "Contenuto2", d);
			Post p3 = new Post(2, "Autore1", "Contenuto1", d);
			assert(p.equals(p2) && !(p.equals(p3)));
			System.out.printf("%d/%d: OK\n", i, j);
			
			i = 2;
			assert(p.hashCode() == p2.hashCode() && p.hashCode() != p3.hashCode());
			System.out.printf("%d/%d: OK\n", i, j);
			
			return true;
		}
		
		public static boolean testSocialNetwork() {
			System.out.println("----- TEST DELLA CLASSE SocialNetwork -----\n");
			SocialNetwork sn = new SocialNetwork();
			
			// Testo createPost
			System.out.println("Test di createPost...");
			
			int i = 1;
			int j = 3;
			
			// Testo createPost con un argomento == null
			try {
				sn.createPost(null, "Contenuto");
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			} 
		
			// Testo createPost con argomenti validi
			i = 2;
			try {
				Post p = sn.createPost("Autore", "Contenuto");
				assert(p.getAuthor().equals("Autore"));
				assert(p.getText().equals("Contenuto"));
				
				Post like1 = sn.createPost("AutoreX", "like:9546");
				assert(like1.getAuthor().equals("AutoreX"));
				assert(like1.getText().equals("like:9546"));
				assert(sn.getFollowed("AutoreX").equals(new HashSet<String>()));
				
				sn.createPost("AutoreS", "Contenuto");
				String textS = "like:" + Integer.toString(p.getId());
				sn.createPost("AutoreS", textS);
				assert(sn.getFollowers("AutoreS").equals(new HashSet<String>()));
				
				System.out.printf("%d/%d: OK\n", i, j);
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				e.printStackTrace();
				return false;
			} 
			
			// Testo un like valido
			i = 3;
			try {
				Post p = sn.createPost("Autore1", "Contenuto");
				String text = "like:" + Integer.toString(p.getId());
				sn.createPost("Autore2", text);
				assert(sn.isFollower("Autore2", "Autore1"));
				
				System.out.printf("%d/%d: OK\n", i, j);
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				e.printStackTrace();
				return false;
			}		

			// Testo i metodi osservatori
			System.out.println("\nTest dei metodi osservatori...");
			i = 1;
			j = 5;
			
			sn = new SocialNetwork();
			
			// Testo i metodi osservatori con un SocialNetwork vuoto
			assert(sn.getNumPost() == 0);
			assert(sn.getPostMap().equals(new HashMap<String, Set<Post>>()));
			assert(sn.getFollowedMap().equals(new HashMap<String, Set<String>>()));
			assert(sn.getFollowersMap().equals(new HashMap<String, Set<String>>()));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo getNumPost con un SocialNetwork non vuoto
			i = 2;
			try {
				sn.createPost("Autore", "Testo");
				sn.createPost("Autore2", "Testo2");
				assert(sn.getNumPost() == 2);
				System.out.printf("%d/%d: OK\n", i, j);
				
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				e.printStackTrace();
				return false;
			} 
			
			// Testo getPostMap con un SocialNetwork non vuoto
			i = 3;
			Post p1 = sn.createPost("Autore", "Testo");
			Post p2 = sn.createPost("Autore2", "Testo2");
			assert(sn.getPostMap().get("Autore").contains(p1));
			assert(sn.getPostMap().get("Autore2").contains(p2));
			assert(!sn.getPostMap().containsKey("Autore3"));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo getFollowedMap con un SocialNetwork non vuoto
			i = 4;
			String text2 = "like:" + p1.getId();
			String text3 = "like:" + p2.getId();
			sn.createPost("Autore3", text2);
			sn.createPost("Autore4", text2);
			sn.createPost("Autore4", text3);
			assert(sn.getFollowedMap().get("Autore3").contains("Autore"));
			assert(sn.getFollowedMap().get("Autore4").contains("Autore"));
			assert(sn.getFollowedMap().get("Autore4").contains("Autore2"));
			assert(!sn.getFollowedMap().containsKey("Autore"));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo getFollowersMap con un SocialNetwork non vuoto
			i = 5;
			assert(sn.getFollowersMap().get("Autore").contains("Autore3"));
			assert(sn.getFollowersMap().get("Autore").contains("Autore4"));
			assert(sn.getFollowersMap().get("Autore2").contains("Autore4"));
			assert(!sn.getFollowersMap().containsKey("Autore3"));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo isFollower
			System.out.println("\nTest di isFollower...");
			i = 1;
			j = 2;
			
			sn = new SocialNetwork();
			
			// Testo isFollower con argomenti validi
			try {
				Post p = sn.createPost("Autore1", "Contenuto");
				String text = "like:" + Integer.toString(p.getId());
				sn.createPost("Autore2", text);
				assert(sn.isFollower("Autore2", "Autore1"));
				assert(!sn.isFollower("Autore1", "Autore2"));
				assert(!sn.isFollower("Autore4", "Autore5"));
				System.out.printf("%d/%d: OK\n", i, j);
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			}
			
			// Testo isFollower con un argomento == null
			i = 2;
			try {
				sn.isFollower(null, "Autore1");
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}
			
			// Testo getFollowers
			System.out.println("\nTest di getFollowers...");
			i = 1;
			j = 2;
			
			sn = new SocialNetwork();
			
			// Testo getFollowers con argomento valido
			Post p;
			try {
				p = sn.createPost("Autore1", "Contenuto");
				String text = "like:" + Integer.toString(p.getId());
				sn.createPost("Autore2", text);
				Set<String> followers = sn.getFollowers("Autore1");
				assert(followers.contains("Autore2"));
				assert(!followers.contains("Autore1"));
				System.out.printf("%d/%d: OK\n", i, j);
			} catch (NullPointerException e) {
				e.printStackTrace();
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			}
			
			// Testo getFollowers con argomento == null
			i = 2;
			try {
				assert(!sn.getFollowers(null).contains("Autore1"));
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}
			
			// Testo getFollowed
			System.out.println("\nTest di getFollowed...");
			i = 1;
			j = 2;
			
			sn = new SocialNetwork();
			
			// Testo getFollowed con argomento valido
			try {
				sn.createPost("Autore1", "Contenuto");
				String text = "like:" + Integer.toString(p.getId());
				sn.createPost("Autore2", text);
				Set<String> followed = sn.getFollowed("Autore2");
				assert(followed.contains("Autore1"));
				assert(!followed.contains("Autore2"));
				System.out.printf("%d/%d: OK\n", i, j);
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				e.printStackTrace();
				return false;
			}
			
			// Testo getFollowed con argomento == null
			i = 2;
			try {
				assert(!sn.getFollowed(null).contains("Autore2"));
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}

			// Testo guessFollowers
			System.out.println("\nTest di guessFollowers...");
			i = 1;
			j = 3;
			sn = new SocialNetwork();
			
			// Testo guessFollowers con argomento valido
			try {
				Post el1 = sn.createPost("Autore1", "Contenuto1");
				String id1 = Integer.toString(el1.getId());
				String contenuto2 = "like:" + id1;
				Post el2 = sn.createPost("Autore2", contenuto2);
				
				List<Post> pl1 = new ArrayList<Post>();
				List<Post> pl2 = new ArrayList<Post>();
				pl1.add(el1);
				pl1.add(el2);
				
				Map<String, Set<String>> map1 = SocialNetwork.guessFollowers(pl1);
				assert(map1.get("Autore1").contains("Autore2"));
				assert(!map1.get("Autore2").contains("Autore1"));
				
				Map<String, Set<String>> map2 = SocialNetwork.guessFollowers(pl2);
				assert(map2.keySet().equals(new HashSet<String>()));
				
				System.out.printf("%d/%d: OK\n", i, j);
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				e.printStackTrace();
				return false;
			}

			// Testo guessFollowers con argomento illegale
			i = 2;
			Post ipost1 = new Post(1, "Autore1", "Contenuto1", new Date());
			Post ipost2 = new Post(1, "Autore1", "Contenuto1", new Date());
			List<Post> ipl = new ArrayList<Post>();
			ipl.add(ipost1);
			ipl.add(ipost2);
			
			try {
				SocialNetwork.guessFollowers(ipl);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (IllegalArgumentException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			} 
			
			// Testo guessFollowers con argomento == null
			i = 3;
			
			try {
				SocialNetwork.guessFollowers(null);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			} 
			
			// Testo guessFollowed
			System.out.println("\nTest di guessFollowed...");
			i = 1;
			j = 3;
			
			// Testo guessFollowed con argomento valido
			try {
				Post el1 = sn.createPost("Autore1", "Contenuto1");
				String id1 = Integer.toString(el1.getId());
				String contenuto2 = "like:" + id1;
				Post el2 = sn.createPost("Autore2", contenuto2);
				
				List<Post> pl1 = new ArrayList<Post>();
				List<Post> pl2 = new ArrayList<Post>();
				pl1.add(el1);
				pl1.add(el2);
				
				Map<String, Set<String>> map1 = SocialNetwork.guessFollowed(pl1);
				assert(map1.get("Autore2").contains("Autore1"));
				assert(!map1.get("Autore1").contains("Autore2"));
				
				Map<String, Set<String>> map2 = SocialNetwork.guessFollowed(pl2);
				assert(map2.keySet().equals(new HashSet<String>()));
				
				System.out.printf("%d/%d: OK\n", i, j);
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: ERRORE\n", i, j);
				e.printStackTrace();
				return false;
			}
			
			// Testo guessFollowed argomento illegale
			i = 2;
			Post ipost3 = new Post(1, "Autore1", "Contenuto1", new Date());
			Post ipost4 = new Post(1, "Autore1", "Contenuto1", new Date());
			List<Post> ipl2 = new ArrayList<Post>();
			ipl2.add(ipost3);
			ipl2.add(ipost4);
			
			try {
				SocialNetwork.guessFollowed(ipl2);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (IllegalArgumentException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			} 
			
			// Testo guessFollowers con argomento == null
			i = 3;
			
			try {
				SocialNetwork.guessFollowed(null);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			} 
			
			// Testo influencers
			System.out.println("\nTest di influencers...");
			i = 1;
			j = 2;
			
			// Testo influencers su una rete sociale vuota
			sn = new SocialNetwork();
			assert(sn.influencers().equals(new ArrayList<String>()));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo influencers su una rete sociale non vuota
			i = 2;
			Post el1 = sn.createPost("Autore1", "Contenuto1");
			String id1 = Integer.toString(el1.getId());
			Post el2 = sn.createPost("Autore2", "like:" + id1);
			String id2 = Integer.toString(el2.getId());
			Post el3 = sn.createPost("Autore3", "like:" + id1);
			String id3 = Integer.toString(el3.getId());
			sn.createPost("Autore4", "like:" + id1);
			sn.createPost("Autore3", "like:" + id2);
			sn.createPost("Autore4", "like:" + id2);
			sn.createPost("Autore4", "like:" + id3);
			
			List<String> list = new ArrayList<String>();
			list.add("Autore1");
			list.add("Autore2");
			list.add("Autore3");
			
			assert(sn.influencers().equals(list));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo getMentionedUsers
			System.out.println("\nTest di getMentionedUsers...");
			i = 1;
			j = 6;
			
			// Testo getMentionedUsers su una rete sociale vuota
			sn = new SocialNetwork();
			assert(sn.getMentionedUsers().equals(new HashSet<String>()));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo getMentionedUsers su una rete sociale non vuota
			i = 2;
			Post post1 = sn.createPost("Autore1", "Contenuto");
			Post post2 = sn.createPost("Autore2", "Contenuto2");
			Set<String> mentioned = new HashSet<String>();
			mentioned.add("Autore1");
			mentioned.add("Autore2");
			assert(sn.getMentionedUsers().equals(mentioned));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo getMentionedUsers su una lista di post vuota
			i = 3;
			List<Post> ps = new ArrayList<Post>();
			assert(SocialNetwork.getMentionedUsers(ps).equals(new HashSet<String>()));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo getMentionedUsers su una lista di post non vuota
			i = 4;
			ps.add(post1);
			ps.add(post2);
			assert(SocialNetwork.getMentionedUsers(ps).equals(mentioned));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo getMentionedUsers con un parametro == null
			i = 5;
			try {
				SocialNetwork.getMentionedUsers(null);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}
			
			// Testo getMentionedUsers con una lista di post illegale
			i = 6;
			try {
				SocialNetwork.getMentionedUsers(ipl2);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (IllegalArgumentException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}

			// Testo writtenBy
			System.out.println("\nTest di writtenBy...");
			i = 1;
			j = 6;
			
			// Testo writtenBy con username valido
			sn = new SocialNetwork();
			Post wpost1 = sn.createPost("Autore1", "Contenuto1");
			Post wpost2 = sn.createPost("Autore1", "Contenuto2");
			assert(sn.writtenBy("Autore2").equals(new ArrayList<Post>()));
			assert(sn.writtenBy("Autore1").contains(wpost1) && sn.writtenBy("Autore1").contains(wpost2));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo writtenBy con un parametro == null
			i = 2;
			try {
				sn.writtenBy(null);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}

			i = 3;
			try {
				SocialNetwork.writtenBy(null, "Autor1");
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}
			
			i = 4;
			List<Post> wlist = new ArrayList<Post>();
			try {
				SocialNetwork.writtenBy(wlist, null);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}
			
			// Testo writtenBy su una lista di post valida
			i = 5;
			wlist.add(wpost1);
			wlist.add(wpost2);
			assert(SocialNetwork.writtenBy(wlist, "Autore1").contains(wpost1) && SocialNetwork.writtenBy(wlist, "Autore1").contains(wpost2));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo writtenBy su una lista di post illegale
			i = 6;
			try {
				SocialNetwork.writtenBy(ipl2, "Autore1");
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (IllegalArgumentException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}
			
			// Testo containing
			System.out.println("\nTest di containing...");
			i = 1;
			j = 2;
			
			// Testo containing con parametro valido
			sn = new SocialNetwork();
			List<String> words = new ArrayList<String>();
			words.add("Parola1");
			words.add("Parola2");
			assert(sn.containing(words).equals(new ArrayList<Post>()));
			Post wordPost1 = sn.createPost("Autore1", "Post con parola1");
			Post wordPost2 = sn.createPost("Autore2", "Post con parola2");
			Post wordPost3 = sn.createPost("Autore3", "Post senza niente");
			List<Post> contained = sn.containing(words);
			assert(contained.contains(wordPost1) && contained.contains(wordPost2) && !contained.contains(wordPost3));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo containing con parametro == null
			i = 2;
			try {
				sn.containing(null);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}
			
			return true;
		}
		
		public static boolean testSaferSocialNetwork() {
			System.out.println("----- TEST DELLA CLASSE SaferSocialNetwork -----\n");
			SaferSocialNetwork ssn = new SaferSocialNetwork();
			
			// Testo reportIfOffensive e getReportedPosts
			System.out.println("Test di reportIfOffensive e getReportedPosts...");
			int i = 1;
			int j = 3;
			
			// Testo reportIfOffensive e getReportedPosts con parametri validi
			assert(ssn.getReportedPosts().equals(new HashSet<Post>()));
			Post rp = ssn.createPost("Autore1", "Parola1");
			Post rp2 = ssn.createPost("Autore2", "Parola2");
			Post rp3 = ssn.createPost("Autore3", "Parola3");
			List<String> words = new ArrayList<String>();
			words.add("Parola1");
			words.add("Parola2");
			assert(ssn.reportIfOffensive(rp.getId(), words));
			assert(ssn.reportIfOffensive(rp2.getId(), words));
			assert(!ssn.reportIfOffensive(rp3.getId(), words));
			
			assert(ssn.getReportedPosts().contains(rp) && ssn.getReportedPosts().contains(rp2) && !ssn.getReportedPosts().contains(rp3));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo reportIfOffensive con lista di parole == null
			i = 2;
			
			try {
				ssn.reportIfOffensive(rp.getId(), null);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}
			
			// Testo reportIfOffensive con id non corrispondente ad alcun post nella rete sociale
			i = 3;
			
			try {
				ssn.reportIfOffensive(987, words);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (IllegalArgumentException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}			
			
			// Testo getSaferPostMap
			System.out.println("\nTest di getSaferPostMap...");
			i = 1;
			j = 2;
			
			// Testo getSaferPostMap su una rete sociale vuota
			ssn = new SaferSocialNetwork();
			assert(ssn.getSaferPostMap().equals(new HashMap<String, Set<String>>()));
			System.out.printf("%d/%d: OK\n", i, j);
			
			// Testo getSaferPostMap su una rete sociale non vuota
			i = 2;
			Post safep1 = ssn.createPost("Autore1", "Parola1");
			Post safep2 = ssn.createPost("Autore2", "Parola2");
			Post safep3 = ssn.createPost("Autore3", "Testo non offensivo");
			ssn.reportIfOffensive(safep1.getId(), words);
			ssn.reportIfOffensive(safep2.getId(), words);
			ssn.reportIfOffensive(safep3.getId(), words);
			
			Map<String, Set<Post>> spmap = ssn.getSaferPostMap();
			assert(!spmap.get("Autore1").contains(safep1));
			assert(!spmap.get("Autore2").contains(safep2));
			assert(spmap.get("Autore3").contains(safep3));
			System.out.printf("%d/%d: OK\n", i, j);
		
			// Testo saferWrittenBy
			System.out.println("\nTest di saferWrittenBy...");
			i = 1;
			j = 2;
						
			// Testo saferWrittenBy con username valido
			ssn = new SaferSocialNetwork();
			Post wpost1 = ssn.createPost("Autore1", "Contenuto1");
			Post wpost2 = ssn.createPost("Autore1", "Parola2");
			ssn.reportIfOffensive(wpost2.getId(), words);
			assert(ssn.saferWrittenBy("Autore2").equals(new ArrayList<Post>()));
			assert(ssn.saferWrittenBy("Autore1").contains(wpost1) && !ssn.saferWrittenBy("Autore1").contains(wpost2));
			System.out.printf("%d/%d: OK\n", i, j);
						
			// Testo saferWrittenBy con parametro == null
			i = 2;
			try {
				ssn.saferWrittenBy(null);
				System.out.printf("%d/%d: ERRORE\n", i, j);
				return false;
			} catch (NullPointerException e) {
				System.out.printf("%d/%d: OK\n", i, j);
			}

			return true;
		}
	}