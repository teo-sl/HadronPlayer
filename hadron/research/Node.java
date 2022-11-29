package hadron.research;

import java.util.Comparator;

import hadron.board.Board;
import hadron.board.ByteBoard;

/**
 *  Classe che concretizza il concetto di nodo dell'albero di gioco.
 *  Riporta al suo interno le seguenti informazioni
 *  	- Tipo di nodo (massimizzatore/minimizzatore)
 *  	- Configurazione attuale del gioco
 *  	- Mossa che ha portato a questa configurazione (serve per
 *  	  la successiva comunicazione al server)
 *  N.B. Gli algoritmi restituiscono una configurazione
 *  
 */
public class Node implements Comparable<Node>{
	// Colore del giocatore
	private byte col;
	// Valore euristica
	private double value;
	// Mossa che ha generato la configurazione (dei primi figli)
	private String move;
	// Configurazione generata
	private Board board;

	
	 /** Costruttore
	  * @param board configurazione di gioco di partenza
	  * @param ip indice riga di partenza
	  * @param jp indice colonna di partenza
	  * @param col colore delle pedine da spostare
	  */
	public Node(Board board, int ip, int jp, byte col) {
		this.board = new ByteBoard(board);
		this.board.addPawn(ip, jp, col);

		this.move = (char)('A'+ip)+""+(jp+1);
		this.col = col;
	}

	/**
	 * Restituisce il valore dell'euristica per il nodo
	 *
	 * @return valore euristico nodo
	 */
	public double getValue() {
		return this.value;
	}

	private static Comparator<Node> c1 = new Comparator<Node>() {
		@Override
		public int compare(Node arg0, Node arg1) {
			if(arg0.value < arg1.value)
				return -1;
			return 1;
		}
	};
	
	private static Comparator<Node> c2 = new Comparator<Node>() {
		@Override
		public int compare(Node arg0, Node arg1) {
			if(arg1.value < arg0.value) 
				return -1;
			return 1;
		}
	};

	/**
	 * Metodo che restituisce il comparator del nodo, effettua la distinzione
	 * tra nodi massimizzatori e minimizzatori
	 *
	 * @param col colore giocatore
	 * @return oggetto Comparator
	 */
	public Comparator<Node> getComparator(int col) {
		return col == this.col ? c2 : c1;
	}
	
	/**
	 * Restituisce la mossa che ha generato la configurazione del nodo
	 *
	 * @return mossa
	 */
	public String getPreviousMove() {
		return this.move;
	}
	
	/** Restituisce la hadron.board associata al nodo
	 *
	 *  @return hadron.board
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Metodo che imposta il valore del nodo all'interno dell'algoritmo di ricerca
	 *
	 * @param value valore euristico
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
	public String toString() {
		return this.move;//+" -> "+this.value;
	}

	@Override
	public int compareTo(Node n) {
		return (int) (this.value - n.value);
	}
	
	@Override
	public int hashCode() {
		return this.board.hashCode();
	}
	
}
