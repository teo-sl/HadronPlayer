package hadron.humanPlayer;

import hadron.Communication;
import hadron.PlayerV1;
import hadron.board.Board;
import hadron.board.ByteBoard;
import hadron.heuristic.Heuristic;
import hadron.heuristic.MyHeuristicV1;
import hadron.research.GameController;
import hadron.research.GameControllerImpl;
import hadron.research.NegaSort;
import hadron.research.Research;

public class HumanPlayer {
    private GameController game;


    public void start(String ip, int port) {
        Board board = new ByteBoard();
        game = new HumanGameController(null,  board, (byte)1, 930);
        new Communication(ip,port,game);
    }
    public static void main(String[] args) {//Heuristic h = new GenericHeuristic();
        HumanPlayer p1 = new HumanPlayer();
        p1.start(args[0], Integer.parseInt(args[1]));


    }
}
