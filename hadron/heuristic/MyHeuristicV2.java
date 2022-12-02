package hadron.heuristic;

import hadron.board.Board;



public class MyHeuristicV2 implements Heuristic{
    private static final int d = 9;

    @Override
    public double evaluate(Board b, int col) {

        //if(b.isFinal()) return -1_000_000D;
        int nMoves = 0;
        int positionValue;
        int reservedWhite=0, reservedBlack=0, nonReserved=0;
        for(int i=0;i<d;++i)
            for(int j=0;j<d;++j)
                if(validMove(b,i,j)) { // potential reserved
                    nMoves++;
                    boolean reserved = false;
                    if(i-1>=0) {
                        positionValue = getPositionValue(b,i-1,j);
                        if((positionValue==-1 || positionValue==1) && closeVerification(b,i-1,j,-1*positionValue)==1) {
                            if (positionValue == 1) reservedWhite++;
                            else reservedBlack++;
                            reserved=true;
                        }
                    }

                    if(i+1<d) {
                        positionValue = getPositionValue(b,i+1,j);
                        if((positionValue==-1 || positionValue==1) && closeVerification(b,i+1,j,-1*positionValue)==1) {
                            if (positionValue == 1) reservedWhite++;
                            else reservedBlack++;
                            reserved=true;
                        }

                    }


                    if(j-1>=0) {
                        positionValue = getPositionValue(b,i,j-1);
                        if((positionValue==-1 || positionValue==1) && closeVerification(b,i,j-1,-1*positionValue)==1) {
                            if (positionValue == 1) reservedWhite++;
                            else reservedBlack++;
                            reserved=true;
                        }

                    }


                    if(j+1<d) {
                        positionValue = getPositionValue(b,i,j+1);
                        if((positionValue==-1 || positionValue==1) && closeVerification(b,i,j+1,-1*positionValue)==1) {
                            if (positionValue == 1) reservedWhite++;
                            else reservedBlack++;
                            reserved = true;
                        }
                    }
                    if(!reserved)
                        nonReserved++;

                }
        if(nMoves==0) return -1_000_000D;
        double hReserved = (reservedWhite-reservedBlack)*100;

        // col == 1 white; col == 0 black;
        hReserved = (col==1) ? hReserved : -1*hReserved;
        double hNonReserved = (nonReserved%2==1) ? 10 : -10;

        double w1 = 0.8, w2 = 0.2;
        return w1*hReserved+w2*hNonReserved;

    }




    // restituisce il numero di adiacenti con valore 0 o pari al positionValue
    public int closeVerification(Board b, int i,int j, int positionValue) {
        int count = 0;
        int closeValue;
        if(i-1>=0 && b.getCol(i-1,j)==-1) {
            closeValue = getPositionValue(b,i-1,j);
            if(closeValue==0 || closeValue==positionValue)
                count++;
        }
        if(i+1<d && b.getCol(i+1,j)==-1) {
            closeValue = getPositionValue(b,i+1,j);
            if(closeValue==0 || closeValue==positionValue)
                count++;
        }

        if(j-1>=0 && b.getCol(i,j-1)==-1) {
            closeValue = getPositionValue(b,i,j-1);
            if(closeValue==0 || closeValue==positionValue)
                count++;
        }


        if(j+1<d && b.getCol(i,j+1)==-1) {
            closeValue = getPositionValue(b,i,j+1);
            if(closeValue==0 || closeValue==positionValue)
                count++;
        }
        return count;
    }



    private int getPositionValue(Board b, int i, int j) {
        int count = 0;
        if(i-1>=0)
            count+=convertPosition(b.getCol(i-1,j));

        if(i+1<d)
            count+=convertPosition(b.getCol(i+1,j));

        if(j-1>=0)
            count+=convertPosition(b.getCol(i,j-1));

        if(j+1<d)
            count+=convertPosition(b.getCol(i,j+1));
        return count;
    }
    private int convertPosition(int v) {
        switch (v) {
            case 1:
                return 1;
            case 0:
                return -1;
            case -1:
                return 0;
            default:
                throw new IllegalArgumentException();
        }
    }

    private boolean validMove(Board b, int i, int j) {
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


        return nn == np;
    }


}