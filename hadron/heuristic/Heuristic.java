package hadron.heuristic;

import java.io.Serializable;

import hadron.board.Board;

public interface Heuristic extends Serializable{

	/**
	 * Metodo che calcola il valore euristico della configurazione
	 *
	 * @param b hadron.board
	 * @param col colore giocatore
	 * @return valore euristico configurazione
	 */
	public double evaluate(Board b,int col);

}
