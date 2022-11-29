package hadron;

import hadron.board.Board;
import hadron.board.ByteBoard;
import hadron.heuristic.GenericHeuristic;
import hadron.heuristic.Heuristic;
import hadron.heuristic.MyHeuristicV1;
import hadron.research.GameController;
import hadron.research.GameControllerImpl;
import hadron.research.NegaSort;
import hadron.research.Research;

public class StupidPlayer {
    private Heuristic heuristic;
    private GameController game;

    public StupidPlayer( Heuristic h) {
        this.heuristic = h;
    }


    public void start(String ip, int port) {
        Board board = new ByteBoard();
        Research algorithm = new NegaSort();
        game = new GameControllerImpl(algorithm, heuristic, board, (byte)0, 930);
        new Communication(ip,port,game);
    }

    public static void main(String[] args) {
        Heuristic h = new GenericHeuristic();
        Player p1 = new Player(h);
        p1.start(args[0], Integer.parseInt(args[1]));
    }

    public GameController getGame() {
        return this.game;
    }
}