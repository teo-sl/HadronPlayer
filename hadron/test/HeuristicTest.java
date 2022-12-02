package hadron.test;

import hadron.board.Board;
import hadron.board.ByteBoard;
import hadron.heuristic.Heuristic;
import hadron.heuristic.MyHeuristicV1;
import hadron.heuristic.MyHeuristicV2;

public class HeuristicTest {
    public static void main(String[] args) {
        Board b = new ByteBoard();
        Heuristic h1 = new MyHeuristicV1();
        Heuristic h2 = new MyHeuristicV2();

        b.addPawn(3,2,0);
        b.addPawn(4,3,1);
        b.addPawn(4,1,0);
        int size = b.getSons((byte) 0).size();


        System.out.println(b);
        System.out.println("moves size value: "+size);
        System.out.println("\n\n board heuristic v1 value: " + h1.evaluate(b,0));
        System.out.println("\n\n board heuristic v2 value: " + h2.evaluate(b,0));
    }
}
