package hadron.test;

import hadron.board.Board;
import hadron.board.ByteBoard;
import hadron.heuristic.Heuristic;
import hadron.heuristic.MyHeuristicV1;
import hadron.heuristic.MyHeuristicV2;
import hadron.heuristic.MyHeuristicV4;

public class HeuristicTest {
    public static void main(String[] args) {
        Board b = new ByteBoard();
        Heuristic h4 = new MyHeuristicV4();

        for(int i=0;i<9;++i)
            for(int j=0;j<9;++j)
                if(i==8 && j==8) continue;
                else b.addPawn(i,j,0);



        b.addPawn(7,8,1);



        System.out.println(b);
        System.out.println("\n\n board heuristic v1 value: " + h4.evaluate(b,1));
    }
}
