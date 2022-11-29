package hadron.research;

import java.util.LinkedList;

import hadron.board.Board;

/*
 * Interfaccia che si occupa della gestione dell'algoritmo
 */
public interface GameController {

	/**
	 * Quando arriva YOUR_TURN
	 * faccio partire un Thread che mi cerca  la mossa prossima
	 * hadron.Communication si ferma in attesa della mossa, la invia e poi fa partire la ricerca
	 * a partire dal figlio la cui mossa è stata inviata al server
	 *
	 * @param board game configuration
	 * @return nodo relativo alla configurazione scelta
	 */
	public Node nextMove(Board board);
	
	
	/**
	 * Metodo che riceve la mossa effettuata dall'avversario ed aggiorna la scacchiera corrente
	 *
	 * @param move mossa dell'avversario
	 */
	public void updateGame(String move);
	
	
	/**
	 * Imposta il colore del giocatore
	 *
	 * @param colore stringa che riporta il colore
	 * @return intero che rappresenta il colore 0 = Bianco, 1 = Nero
	 */
	public int setCol(String colore);


	/**
	 * Restituisce la hadron.board di gioco corrente
	 *
	 * @return hadron.board corrente
	 */
	public Board getBoard();

	/**
	 * Restituisce il colore del giocatore
	 *
	 * @return colore giocatore
	 */
	public int getCol();



	
	
}
