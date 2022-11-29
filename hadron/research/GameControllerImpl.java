package hadron.research;

import java.util.LinkedList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import hadron.board.Board;
import hadron.heuristic.Heuristic;

public class GameControllerImpl implements GameController{
	public int TIME_WIND;
	private Research algorithm;
	
	private Board board; 	// configurazione di gioco attuale
	private byte col; 		// colore giocatore
	private Heuristic h;	// euristica
	
	ForkJoinPool f = new ForkJoinPool();
	
	private LinkedList<Board> boards = new LinkedList<Board>();
	
	/**
	 * Costruttore della classe GameController
	 *
	 * @param algorithm algoritmo da utilizzare per la ricerca
	 * @param col colore giocatore
	 * @param time tempo a disposizione per la mossa in ms
	 */
	public GameControllerImpl(Research algorithm, Heuristic h, Board board, byte col, int time) {
		
		this.algorithm = algorithm;
		this.algorithm.setHeuristic(h);
		this.h = h;
		this.col = col;
		this.TIME_WIND = time;
		this.board = board;
	}


	public Node nextMove(Board board) {
		this.board = board;
		double alpha = -100000; // -infinito
		double beta = 100000;   // +infinito
		return iterativeDeepening(alpha,beta);	
	}
	
	
	/**
	 * Metodo che implementa l'algoritmo iterative deepening
	 *
	 * @param alpha valore alpha
	 * @param beta valore beta
	 * 
	 */
	private Node iterativeDeepening(double alpha, double beta) {
		int dep; 			// cutting level dell'iterazione corrente
		Node res = null;	// miglior nodo da restituire
		Node resc = null;	// migliore corrente
		
		long endTimeMillis = System.currentTimeMillis() + TIME_WIND;
		long initialTime = System.currentTimeMillis();
		long timeUsed = 0;
		
		res = algorithm.research(board, 1, col, alpha, beta);
		dep = 2;

		while(true) {
			// Tempo a disposizione terminato
			if(System.currentTimeMillis() > endTimeMillis) {
				break;
			} else if(resc != null) {
				res = resc;
			}
			
			// Goal trovato
			if(res!=null && res.getValue() == 1000000D/*valore del goal*/) {
				timeUsed = System.currentTimeMillis() - initialTime;
				this.board = res.getBoard();
				boards.add(res.getBoard());
				return res;
			}
		
			final int d = dep;
			
			/* Costruisco il task da eseguire in maniera ricorsiva, l'utilizzo della classe RecursiveTask
			 * viene adottato per poter avere tutta una serie di operazioni sulla gestione dell'esecuzione
			 * questo fa iterare la ricerca
			 * */
			 RecursiveTask<Node> mySecTask = new RecursiveTask<Node>() {
					private static final long serialVersionUID = 0L;
					
					@Override 
					protected Node compute() {
						return algorithm.research(board, d, col, alpha, beta);
					}
			};//Task
			
			f.execute(mySecTask);
			
			// Attendiamo la terminazione del task
			while(!mySecTask.isDone()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				
				// Controlliamo se non sia terminato il tempo a disposizione
				// dell'algoritmo
				if(System.currentTimeMillis() > endTimeMillis) {
					timeUsed = System.currentTimeMillis() - initialTime;
					
					// L'argomento della funzione ci dice se il task debba essere
					// interrotto anche se si trova ancora in running
					mySecTask.cancel(true);
				}
			}
			
			resc = mySecTask.getRawResult();
			
			dep++;
		}
		
		this.board = res.getBoard();
		boards.add(res.getBoard());
		return res;
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
	
}
