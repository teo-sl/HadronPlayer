package hadron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import hadron.board.Board;
import hadron.research.GameController;

/**
 * Classe che si occupa della comunicazione con il server
 */
public class Communication {
	
	private String serverIP;		// IP del server
	private int serverPort;			// porta del server
	
	private Socket server;			// socket di comunicazione
	private BufferedReader in;		// buffer in ingresso
	private PrintWriter out;		// buffer in uscita
	
	private Listener listener; 		// Thread che ascolta i nuovi messaggi del server
	private String message; 		// Messaggio appena ricevuto dal server
	
	private GameController game;	// GameController, responsabile della gestione del gioco
	private int col;				// colore giocatore
	
	//Solo per debugging in caso di INVALID_MOVE
	private LinkedList<Board> boards = new LinkedList<>();
	private LinkedList<String> moves = new LinkedList<>();

	/**
	 * Costruttore, apre il socket verso il server e crea gli stream in input ed output
	 * @param ip ip server
	 * @param port porta server
	 * @param game GameController
	 */
	public Communication(String ip, int port, GameController game) {
		this.serverIP = ip;
		this.serverPort = port;
		
		// Collegamento al server
		try{
			server = new Socket( serverIP, serverPort );
			in = new BufferedReader( new InputStreamReader( server.getInputStream() ) );
			out = new PrintWriter( server.getOutputStream(), true );
			System.out.println("Connected to Server");

			listener = new Listener();
			listener.start();

			this.game = game;
			boards.add( game.getBoard() );
		}catch( IOException ioe ){
			System.out.println(ioe);
		}
	}

	public Listener getListener() {
		return this.listener;
	}

	/**
	 * Oggetto che si occupa di leggere e gestire i messaggi del server
	 */
	class Listener extends Thread{
		
		public void run() {
			String myMove = null;

			try {
				while(true) {
					message = in.readLine();
					System.out.println("\tCOL "+col+" - SERVER: "+message);

					if(message.equals("YOUR_TURN")) {
						myMove = game.nextMove(game.getBoard()).getPreviousMove();
						out.println("MOVE "+myMove);
						out.flush();

						System.out.println(game.getBoard());
						moves.add(myMove);
						boards.add( game.getBoard() );

					}else if(message.contains("OPPONENT_MOVE")) {
						String oppMove = message.substring(14);
						game.updateGame(oppMove);

						moves.add(oppMove);
						boards.add( game.getBoard() );

					}else if(message.contains("WELCOME"))
						col = game.setCol(message.substring(8));
					
					else if(message.equals("VALID_MOVE"))
						continue;
						
					else if(message.contains("MESSAGE"))
						continue;

					else if(message.equals("ILLEGAL_MOVE")) {
						System.err.println("ILLEGAL_MOVE "+myMove);
						printGameTrace();
						break;
					}else if(message.equals("TIMEOUT"))
						break;

					else if(message.equals("VICTORY")) {
						break;
					}
					else if(message.equals("TIE") || message.equals("DEFEAT")) {
						break;
					}
						
				}
			}catch(Exception e) {
				System.out.println("-----------ERRORE-----------");
				e.printStackTrace();
				//stayOn();
			}
		}

	}
	
	private void printGameTrace() {
		System.out.println("Configurazione iniziale:");
		System.out.println( boards.removeFirst() );
		if( game.getCol() == 1 ) { //inizio io
			System.out.println("Sono il Bianco, inizio io");
			while( !boards.isEmpty() ) {
				System.out.println("La mia mossa: "+ (moves.isEmpty()?null:moves.removeFirst()) );
				System.out.println("La mia scacchiera aggiornata:\n"+ (boards.isEmpty()?null:boards.removeFirst() ) );
				System.out.println("La sua mossa: "+ (moves.isEmpty()?null:moves.removeFirst()) );
				System.out.println("La mia scacchiera aggiornata:\n"+ (boards.isEmpty()?null:boards.removeFirst() ) );
			}
		}else {
			System.out.println("Sono il Nero");
			while( !boards.isEmpty() ) {
				System.out.println("La sua mossa: "+ (moves.isEmpty()?null:moves.removeFirst()) );
				System.out.println("La mia scacchiera aggiornata:\n"+ (boards.isEmpty()?null:boards.removeFirst() ) );
				System.out.println("La mia mossa: "+ (moves.isEmpty()?null:moves.removeFirst()) );
				System.out.println("La mia scacchiera aggiornata:\n"+ (boards.isEmpty()?null:boards.removeFirst() ) );
			}
		}
	}
}