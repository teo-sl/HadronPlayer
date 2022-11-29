package hadron.board;

import java.io.Serializable;
import java.util.ArrayList;

import hadron.research.Node;

public abstract class Board implements Serializable{
	private static final long serialVersionUID = -2757025769543616041L;
	public static final int nRows = 9, nCols = 9;


	/** Restituisce il numero di pedine alla posizione i, j;
	 *  segno negativo per il nero, segno positivo per il bianco
	 *  (in questo caso la pedina è al più 1)
	 */
	public abstract byte getPawn(int i, int j);

	/** Restituisce il colore della pedina alla posizione i, j
	 *  0 = nero, 1 = bianco, -1 = vuoto
	 *
	 * @param i colonna
	 * @param j riga
	 * @return colore pedina (null se vuota)
	 */
	public abstract byte getCol(int i, int j);

	/** Aggiunge n pedine del colore indicato nella posizione i, j
	 *
	 * @param i righe
	 * @param j colonne
	 * @param colore colore pedina
	 */
	public abstract void addPawn(int i, int j, int colore);

	/** Rimuove n pedine del colore indicato nella posizione i, j
	 *
	 * @param i righe
	 * @param j colonne
	 * @param colore colore pedina
	 */
	public abstract void removePawn(int i, int j, int colore);

	/** Aggiunge n pedine a partire dalla posizione indicata come specificato nella traccia
	 *
	 * @param riga riga espressa come carattere
	 * @param j colonna
	 * @param colore colore
	 */
	public void addPawn(char riga, int j, char colore) {
		addPawn(Character.toLowerCase(riga) - 97, j - 1, colore == 'b' ? 0 : 1);
	}

	/** Rimuove n pedine a partire dalla posizione indicata come specificato nella traccia
	 *
	 * @param riga riga espressa come carattere
	 * @param j colonna
	 * @param colore colore
	 */
	public void removePawn(char riga, int j, char colore) {
		removePawn(Character.toLowerCase(riga) - 97, j - 1, colore == 'b' ? 0 : 1);
	}

	/** Dato il colore, restituisce l'array di tutte le configurazioni possibili a partire da questa
	 *
	 * @param col colore
	 * @return array delle prossime configurazioni possibili per quel colore
	 */
	public abstract ArrayList<Node> getSons(byte col);

	
	protected boolean validMove(int i, int j) {
		int nPawn = getPawn(i, j);

		if(i<0 || i>=nRows || j<0 || j>=nCols)
			return false;

		if(nPawn!=0)
			return false;

		int np = 0;
		int nn = 0;

		if(i-1>=0 && getPawn(i-1, j)!=0)
			if(getPawn(i-1, j)>0)
				np += 1;
			else
				nn += 1;

		if(i+1<nRows && getPawn(i+1, j)!=0)
			if(getPawn(i+1, j)>0)
				np += 1;
			else
				nn += 1;

		if(j-1>=0 && getPawn(i, j-1)!=0)
			if(getPawn(i, j-1)>0)
				np += 1;
			else
				nn += 1;

		if(j+1<nCols && getPawn(i, j+1)!=0)
			if(getPawn(i, j+1)>0)
				np += 1;
			else
				nn += 1;


		if(nn != np)
			return false;

		return true;
	}

	
	public String toString() {
		StringBuilder sb = new StringBuilder(200);
		sb.append("    1   2   3   4   5   6   7   8   9\n");
		sb.append("  =====================================\n");
		
		for (int i = 0; i < nRows; i++) {
			sb.append((char) (65+i)+"| ");
			for (int j = 0; j < nCols; j++) {
				byte pawn = getPawn(i, j);
				if (pawn == 0)
					sb.append(" - ");
				else if( pawn < 0)
					sb.append(String.format(" B ") );
				else
					sb.append(String.format(" W ") );
				sb.append(" ");
			}
			sb.append("|"+(char) (65+i)+"\n");
		}
		sb.append("  =====================================\n");
		sb.append("    1   2   3   4   5   6   7   8   9\n");

		return sb.toString();
	}
	
	/**
	 * Metodo che ci dice se la scacchiera contenga una configurazione finale
	 *
	 * @return True se la configurazione è finale
	 */
	public boolean isFinal() {
		// il colore del giocatore è indifferente
		return getSons((byte) 0).size()==0;
	}
	
	public static Board getBoardFromMove(String move, Board board, byte col) {
		Board boardRis = new ByteBoard(board);
		int ip = Character.toLowerCase(move.charAt(0)) - 97;
		int jp = Integer.parseInt(move.charAt(1)+"") - 1;

		boardRis.addPawn(ip, jp, col);
		
		return boardRis;
	}
}
