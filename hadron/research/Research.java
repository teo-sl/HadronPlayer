package hadron.research;

import hadron.board.Board;
import hadron.heuristic.Heuristic;

public interface Research {
	
	public void setHeuristic(Heuristic h);

	/**
	 * Metodo di interfacciamento per la ricerca
	 *
	 * @param board configurazione corrente
	 * @param d profondità
	 * @param col colore
	 * @param alpha valore di alpha
	 * @param beta valore di beta
	 * @return miglior nodo
	 */
	public Node research(Board board, int d, byte col, double alpha, double beta);

}
