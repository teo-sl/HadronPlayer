package hadron.heuristic;

import hadron.board.Board;

public class MyHeuristicV4 implements Heuristic {
    private static final int d = 9;
    @Override
    public double evaluate(Board b, int col) {

        int[] counts = new int[3];
        int diff,nMoves = 0;
        boolean hasGenerativeMoves = false, closeZeros = false,checkpoint;
        double resBlack = 0, resWhite = 0, notGenerative = 0;
        for(int i=0;i<d;++i)
            for(int j=0;j<d;++j) {
                if(b.getCol(i,j)!=-1) continue;

                countPosition(b,i,j,counts);

                if(counts[1]==counts[2]) {
                    checkpoint=true;
                    nMoves++;

                    if(i-1>=0 && b.getCol(i-1,j)==-1) {

                        countPosition(b,i-1,j,counts);

                        diff=counts[2]-counts[1];

                        if(diff==-1 || diff==1) {
                            hasGenerativeMoves=true;
                            if(counts[0]==1)
                                switch (diff) {
                                    case 1:
                                        resWhite++; break;
                                    case -1:
                                        resBlack++; break;
                                }
                        }
                        else {
                            checkpoint=false;
                        }
                        if(diff==0) {
                            closeZeros=true;
                        }

                    }
                    if(i+1<d && b.getCol(i+1,j)==-1) {

                        countPosition(b,i+1,j,counts);

                        diff=counts[2]-counts[1];

                        if(diff==-1 || diff==1) {
                            hasGenerativeMoves=true;
                            if(counts[0]==1)
                                switch (diff) {
                                    case 1:
                                        resWhite++; break;
                                    case -1:
                                        resBlack++; break;
                                }
                        }
                        else {
                            checkpoint=false;
                        }
                        if(diff==0) {
                            closeZeros=true;
                        }
                    }
                    if(j-1>=0 && b.getCol(i,j-1)==-1) {

                        countPosition(b,i,j-1,counts);

                        diff=counts[2]-counts[1];

                        if(diff==-1 || diff==1) {
                            hasGenerativeMoves=true;
                            if(counts[0]==1)
                                switch (diff) {
                                    case 1:
                                        resWhite++; break;
                                    case -1:
                                        resBlack++; break;
                                }
                        }
                        else {
                            checkpoint=false;
                        }
                        if(diff==0) {
                            closeZeros=true;
                        }

                    }
                    if(j+1<d && b.getCol(i,j+1)==-1) {

                        countPosition(b,i,j+1,counts);

                        diff=counts[2]-counts[1];

                        if(diff==-1 || diff==1) {
                            hasGenerativeMoves=true;
                            if(counts[0]==1)
                                switch (diff) {
                                    case 1:
                                        resWhite++; break;
                                    case -1:
                                        resBlack++; break;
                                }
                        }
                        else {
                            checkpoint=false;
                        }
                        if(diff==0) {
                            closeZeros=true;
                        }

                    }

                    if(checkpoint)
                        notGenerative++;
                }
            }
        if(nMoves==0) return -1_000_000;

        if(!hasGenerativeMoves && !closeZeros)
            return (notGenerative%2==1) ? 100_000D : -100_000D;

        double hReserved = resWhite-resBlack;
        hReserved = (col==1) ? hReserved : -1* hReserved;

        return hReserved *100;

    }

    // 0 => empty
    // 1 => black
    // 2 => white
    private void countPosition(Board b, int i, int j, int[] values) {
        values[0]=values[1]=values[2]=0;
        if(i-1>=0)
            values[((int)b.getCol(i-1,j))+1]++;
        if(i+1<d)
            values[((int)b.getCol(i+1,j))+1]++;
        if(j-1>=0)
            values[((int)b.getCol(i,j-1))+1]++;
        if(j+1<d)
            values[((int)b.getCol(i,j+1))+1]++;
    }
}
