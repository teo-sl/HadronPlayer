package hadron.test;

import hadron.board.Board;
import hadron.board.ByteBoard;
import hadron.heuristic.*;
import hadron.research.Node;

import java.util.List;
import java.util.Random;

public class HeuristicTest {
    public static void main(String[] args) {
        Board b = new ByteBoard();
        Heuristic h1 = new MyHeuristicV1(), h1r = new MyHeuristicV1R();
        Random r = new Random();
        long s1,e1,e2;
        long time1=0,time2=0;
        int nMoves = 60, col = 1,errors=0;
        for(int i=0;i<nMoves && b.getSons((byte)col).size()>0;++i) {
            List<Node> moves = b.getSons((byte)col);
            b=moves.get(r.nextInt(moves.size())).getBoard();
            System.out.println("h1 : "+h1.evaluate(b,col)+" h1r : "+h1r.evaluate(b,col));
            s1 = System.currentTimeMillis();
            h1.evaluate(b,col);
            e1 = System.currentTimeMillis();
            h1r.evaluate(b,col);
            e2 = System.currentTimeMillis();
            time1 += e1-s1;
            time2 += e2-e1;
            if(!equals(h1.evaluate(b,col),h1r.evaluate(b,col)))
                errors++;
            col = 1-col;
        }
        System.out.println("Time h1 : "+time1+" Time h1 r : "+time2);
    }
    // define equals for double
    public static boolean equals(double a, double b) {
        return Math.abs(a-b)<1e-6;
    }
}
