package hadron.heuristic;

import hadron.board.Board;


import java.util.LinkedList;
import java.util.List;

public class MyHeuristicV1 implements Heuristic{
    private static final int d = 9;

    @Override
    public double evaluate(Board b, int col) {

        if(b.isFinal()) return -1_000_000D;

        int reservedWhite =0, reservedBlack=0, nonReserved=0;
        for(int i=0;i<d;++i)
            for (int j=0;j<d;++j)
                if(validMove(b,i,j)) {
                    for(int k=j-1;k>=0 && k<d && i-1>=0 && k<=j+1; k++)
                        if(!validMove(b,i-1,k) && b.getCol(i-1,k)==-1) {
                            switch(checkReserved(b,i-1,k,i,j)) {
                                case 1 :
                                    reservedWhite++;
                                    break;
                                case 0:
                                    reservedBlack++;
                                    break;
                                case -1:
                                    nonReserved++;
                                    break;
                                default:
                                    throw new IllegalArgumentException();
                            }

                        }
                    for(int k=j-1;k>=0 && k<d && i+1<d && k<=j+1;k++) {
                        if(!validMove(b,i+1,k) && b.getCol(i+1,k)==-1) {
                            switch(checkReserved(b,i+1,k,i,j)) {
                                case 1 :
                                    reservedWhite++;
                                    break;
                                case 0:
                                    reservedBlack++;
                                    break;
                                case -1:
                                    nonReserved++;
                                    break;
                                default:
                                    throw new IllegalArgumentException();
                            }
                        }
                    }
                    if(j-1>=0 && b.getCol(i,j-1)==-1) {
                        switch(checkReserved(b,i,j-1,i,j)) {
                            case 1 :
                                reservedWhite++;
                                break;
                            case 0:
                                reservedBlack++;
                                break;
                            case -1:
                                nonReserved++;
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    }
                    if(j+1<d && b.getCol(i,j+1)==-1) {
                        switch(checkReserved(b,i,j+1,i,j)) {
                            case 1 :
                                reservedWhite++;
                                break;
                            case 0:
                                reservedBlack++;
                                break;
                            case -1:
                                nonReserved++;
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    }
                }
        double hReserved = (reservedWhite-reservedBlack)*100;

        // col == 1 white; col == 0 black;
        hReserved = (col==1) ? hReserved : -1*hReserved;
        double hNonReserved = (nonReserved%2==1) ? 10 : -10;

        double w1 = 1, w2 = 0;
        return w1*hReserved+w2*hNonReserved;

    }

    private int checkReserved(Board b, int l, int k, int i, int j) {
        List<Integer> closeValues = getClose(b,l,k);
        int nBlack = 0, nWhite = 0, nEmpty=0, nBlocked = 0;
        for(int x : closeValues) {
            switch (x) {
                case 1:
                    nWhite++;
                    break;
                case 0:
                    nBlack++;
                    break;
                case -1:
                    nEmpty++;
                    break;
                case -2:
                    nBlocked++;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        if(closeValues.size()==2) {
            if(Math.abs(nBlack-nWhite)==1) {
                return (nBlack-nWhite == 1) ? 0 : 1;
            }
            return -1;
        }
        if(closeValues.size()==3) {
            if(nBlack+nWhite+nBlocked==2) return (nBlack-nWhite == 1) ? 0 : 1;
            return -1;

        }
        if(nBlack+nWhite+nBlocked == 3 && nBlack<3 && nWhite<3)
            return (nBlack-nWhite==1) ? 0 : 1;
        return -1;

    }

    private List<Integer> getClose(Board b, int l, int k) {
        List<Integer> ret = new LinkedList<>();
        if(k-1>=0)
            ret.add(evaluatePos(b,l,k-1));
        if(k+1<d)
            ret.add(evaluatePos(b,l,k+1));
        if(l+1<d)
            ret.add(evaluatePos(b,l+1,k));

        if(l-1>=0)
            ret.add(evaluatePos(b,l-1,k));
        return ret;
    }

    private Integer evaluatePos(Board b, int i, int j) {
        int v = b.getCol(i,j);
        if(v==-1)
            return validMove(b,i,j) ? -1 : -2;
        return v;
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


        if(nn != np)
            return false;

        return true;
    }


}