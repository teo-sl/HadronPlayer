package hadron.humanPlayer;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import hadron.board.Board;
import hadron.heuristic.Heuristic;
import hadron.research.GameController;
import hadron.research.Node;

public class HumanGameController implements GameController {
    public int TIME_WIND;

    private Scanner sc = new Scanner(System.in);

    private Board board; 	// configurazione di gioco attuale
    private byte col; 		// colore giocatore
    private Heuristic h;	// euristica

    ForkJoinPool f = new ForkJoinPool();

    private LinkedList<Board> boards = new LinkedList<Board>();

    /**
     * Costruttore della classe GameController
     *
     *
     * @param col colore giocatore
     * @param time tempo a disposizione per la mossa in ms
     */
    public HumanGameController(Heuristic h, Board board, byte col, int time) {
        this.h = h;
        this.col = col;
        this.TIME_WIND = time;
        this.board = board;
    }


    public Node nextMove(Board board) {
        this.board = board;
        int i=-1;
        int j=-1;

        while(true) {
            System.out.println("Inserisci la tua mossa: (e.g. a 3)");
            try {
                i = (sc.next().toLowerCase().charAt(0))-97;
                j = sc.nextInt()-1;
                if(i<0 || i>=9 || j<0 || j>=9 )
                    throw new IllegalArgumentException();
                if(board.getCol(i,j)!=-1)
                    throw new IllegalArgumentException();
                if(!validMove(board,i,j))
                    throw new IllegalArgumentException();
                break;
            }catch(Exception e) {
                System.out.println("Mossa non valida");
            }
        }
        Node move = new Node(board, i, j, col);
        this.board=move.getBoard();
        return move;
    }





    public void updateGame(String move) {
        Board b = Board.getBoardFromMove(move, this.board, (byte) (1 - this.col));
        System.out.println(b);
        this.board = b;
        boards.add(b);
    }

    public int setCol(String colore) {
        if(colore.equalsIgnoreCase("White"))
            this.col = 1;
        else
            this.col = 0;
        return this.col;
    }

    public Board getBoard() { return this.board; }

    public int getCol() { return this.col; }

    public Heuristic getHeuristic() {
        return this.h;
    }

    private boolean validMove(Board b, int i, int j) {
        int nPawn = b.getPawn(i, j);

        if(i<0 || i>=9 || j<0 || j>=9)
            return false;

        if(nPawn!=0)
            return false;

        int np = 0;
        int nn = 0;

        if(i-1>=0 && b.getPawn(i-1, j)!=0)
            if(b.getPawn(i-1, j)>0)
                np += 1;
            else
                nn += 1;

        if(i+1<9 && b.getPawn(i+1, j)!=0)
            if(b.getPawn(i+1, j)>0)
                np += 1;
            else
                nn += 1;

        if(j-1>=0 && b.getPawn(i, j-1)!=0)
            if(b.getPawn(i, j-1)>0)
                np += 1;
            else
                nn += 1;

        if(j+1<9 && b.getPawn(i, j+1)!=0)
            if(b.getPawn(i, j+1)>0)
                np += 1;
            else
                nn += 1;


        if(nn != np)
            return false;

        return true;
    }

}

