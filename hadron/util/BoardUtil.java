package hadron.util;

import hadron.board.Board;

public class BoardUtil {
    private static final int d = 9;

    public static boolean validMove(Board b, int i, int j) {
        int nPawn = b.getPawn(i, j);

        if(i<0 || i>=d || j<0 || j>=d)
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

        if(i+1<d && b.getPawn(i+1, j)!=0)
            if(b.getPawn(i+1, j)>0)
                np += 1;
            else
                nn += 1;

        if(j-1>=0 && b.getPawn(i, j-1)!=0)
            if(b.getPawn(i, j-1)>0)
                np += 1;
            else
                nn += 1;

        if(j+1<d && b.getPawn(i, j+1)!=0)
            if(b.getPawn(i, j+1)>0)
                np += 1;
            else
                nn += 1;


        if(nn != np)
            return false;

        return true;
    }
}
