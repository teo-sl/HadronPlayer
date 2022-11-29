package hadron.board;

import hadron.research.Node;

import java.util.ArrayList;

public class ByteBoard extends Board {
    byte[][] board;

    public ByteBoard(){
        board = new byte[9][9];

        for(int i=0; i<nCols; i++)
            for (int j=0; j<nRows; j++)
                board[i][j] = 0;
    }

    // Costruttore di copia, l'array di interi viene clonato
    public ByteBoard(Board board) {
        byte[][] b = ((ByteBoard) board).board;
        this.board = new byte[nRows][nCols];
        for(int i=0; i<nRows; i++)
            this.board[i] = b[i].clone();
    }

    /**
     * Restituisce il numero di pedine alla posizione i, j;
     * segno negativo per il nero, segno positivo per il bianco
     * (in questo caso la pedina è al più 1)
     *
     * @param i
     * @param j
     */
    @Override
    public byte getPawn(int i, int j) {
        return board[i][j];
    }

    /**
     * Restituisce il colore della pedina alla posizione i, j
     * 0 = nero, 1 = bianco, -1 = vuoto
     *
     * @param i colonna
     * @param j riga
     * @return colore pedina (null se vuota)
     */
    @Override
    public byte getCol(int i, int j) {
        if(board[i][j] == 0)
            return -1;
        return (byte) (board[i][j]>0? 1: 0);
    }

    /**
     * Aggiunge n pedine del colore indicato nella posizione i, j
     *
     * @param i      righe
     * @param j      colonne
     * @param colore colore pedina
     */
    @Override
    public void addPawn(int i, int j, int colore) {
        board[i][j] = (byte) (colore==0? -1: 1);
    }

    /**
     * Rimuove n pedine del colore indicato nella posizione i, j
     *
     * @param i      righe
     * @param j      colonne
     * @param colore colore pedina
     */
    @Override
    public void removePawn(int i, int j, int colore) {
        board[i][j] = 0;
    }

    /**
     * Dato il colore, restituisce l'array di tutte le configurazioni possibili a partire da questa
     *
     * @param col colore
     * @return array delle prossime configurazioni possibili per quel colore
     */
    @Override
    public ArrayList<Node> getSons(byte col) {
        ArrayList<Node> sons = new ArrayList<Node>();
        for(int i=0; i<nRows; i++)
            for(int j=0; j<nCols; j++){
                if(this.validMove(i, j))
                    sons.add(new Node(this, i, j, col));
            }
        return sons;
    }

}
