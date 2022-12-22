package hadron.heuristic;

import hadron.board.Board;

public class MyHeuristicV5 implements Heuristic{
    private static final int d=9;
    @Override
    public double evaluate(Board b, int col) {
        int nMoves = 0,nReserved,whiteReserved=0,blackReserved=0,notReserved=0;
        for(int i=0;i<d;++i)
            for(int j=0;j<d;++j)
                if(isValid(b,i,j)) {
                    nMoves++;
                    nReserved=0;
                    nReserved+=checkReserved(b,i-1,j);
                    nReserved+=checkReserved(b,i+1,j);
                    nReserved+=checkReserved(b,i,j-1);
                    nReserved+=checkReserved(b,i,j+1);
                    if(nReserved==0)
                        notReserved++;
                    else if(nReserved>0)
                        whiteReserved+=nReserved;
                    else
                        blackReserved+=(-1*nReserved);
                }
        if(nMoves==0) return -1_000_000D;

        double hReserved = whiteReserved-blackReserved;
        hReserved = (col==1) ? hReserved : -1*hReserved;
        hReserved*=100;
        double hOther = (notReserved%2 == 1) ? 100 : -100;
        hOther*=(1/nMoves);
        return hReserved+hOther;
    }

    private int checkReserved(Board b, int i, int j) {
        int nWhite=0,nBlack=0,nBlocked=0,nFree=0;
        if(i<0 || i>=d || j<0 || j>=d || isValid(b,i,j))
            return 0;
        if(i>0) {
            switch (b.getCol(i-1,j)) {
                case -1:
                    if(isValid(b,i-1,j))
                        nFree++;
                    else
                        nBlocked++;
                    break;
                case 1:
                    nWhite++; break;
                case 0:
                    nBlack++; break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        if(i+1<d) {
            switch (b.getCol(i+1,j)) {
                case -1:
                    if(isValid(b,i+1,j))
                        nFree++;
                    else
                        nBlocked++;
                    break;
                case 1:
                    nWhite++; break;
                case 0:
                    nBlack++; break;
                default:
                    throw new IllegalArgumentException();
            }

        }
        if(j>0) {
            switch (b.getCol(i,j-1)) {
                case -1:
                    if(isValid(b,i,j-1))
                        nFree++;
                    else
                        nBlocked++;
                    break;
                case 1:
                    nWhite++; break;
                case 0:
                    nBlack++; break;
                default:
                    throw new IllegalArgumentException();
            }

        }
        if(j+1<d) {
            switch (b.getCol(i,j+1)) {
                case -1:
                    if(isValid(b,i,j+1))
                        nFree++;
                    else
                        nBlocked++;
                    break;
                case 1:
                    nWhite++; break;
                case 0:
                    nBlack++; break;
                default:
                    throw new IllegalArgumentException();
            }

        }
        if(nFree>1) return 0;
        if(nWhite==0 && nBlack==0) return 0;
        return (nWhite-nBlack>0) ? 1 : -1;
    }

    private boolean isValid(Board b, int i, int j) {
        return b.getCol(i,j)==-1 && evaluatePosition(b,i,j)==0;
    }
    private int evaluatePosition(Board b, int i, int j) {
        int nWhite = 0, nBlack = 0;
        if(i-1>=0) {
            if(b.getCol(i-1,j)==0) nBlack++;
            else if(b.getCol(i-1,j)==1) nWhite++;
        }
        if(i+1<9) {
            if(b.getCol(i+1,j)==0) nBlack++;
            else if(b.getCol(i+1,j)==1) nWhite++;
        }
        if(j-1>=0) {
            if(b.getCol(i,j-1)==0) nBlack++;
            else if(b.getCol(i,j-1)==1) nWhite++;
        }
        if(j+1<9) {
            if(b.getCol(i,j+1)==0) nBlack++;
            else if(b.getCol(i,j+1)==1) nWhite++;
        }
        return nWhite-nBlack;
    }



}
