package hadron.heuristic;

import hadron.board.Board;

import java.util.Random;

public class MyHeuristicV1R implements Heuristic {
    private static final int d = 9;
    private static final Random r = new Random();


    @Override
    public double evaluate(Board b, int col) {

        int nMoves = 0;

        int reservedWhite = 0, reservedBlack = 0, nonReserved = 0;
        for (int i = 0; i < d; ++i)
            for (int j = 0; j < d; ++j)
                if (validMove(b, i, j)) {
                    nMoves++;
                    boolean flag = false;
                    if (i - 1 >= 0 && b.getCol(i - 1, j) == -1 && !validMove(b, i - 1, j)) {
                        switch (checkReserved(b, i - 1, j)) {
                            case 1:
                                reservedWhite++;
                                flag = true;
                                break;
                            case 0:
                                reservedBlack++;
                                flag = true;
                                break;
                            case -1:
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }

                    }

                    if (i + 1 < d && b.getCol(i + 1, j) == -1 && !validMove(b, i + 1, j)) {
                        switch (checkReserved(b, i + 1, j)) {
                            case 1:
                                reservedWhite++;
                                flag = true;
                                break;
                            case 0:
                                reservedBlack++;
                                flag = true;
                                break;
                            case -1:
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    }
                    if (j - 1 >= 0 && b.getCol(i, j - 1) == -1 && !validMove(b,i,j-1)) {
                        switch (checkReserved(b, i, j - 1)) {
                            case 1:
                                reservedWhite++;
                                flag = true;
                                break;
                            case 0:
                                reservedBlack++;
                                flag = true;
                                break;
                            case -1:
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    }
                    if (j + 1 < d && b.getCol(i, j + 1) == -1 && !validMove(b,i,j+1)) {
                        switch (checkReserved(b, i, j + 1)) {
                            case 1:
                                reservedWhite++;
                                flag = true;
                                break;
                            case 0:
                                reservedBlack++;
                                flag = true;
                                break;
                            case -1:

                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    }
                    if (!flag) nonReserved++;
                }

        // final board case
        if (nMoves == 0) return -1_000_000D;


        int hReserved = (reservedWhite - reservedBlack) * 100;
        hReserved = (col == 1) ? hReserved : -1 * hReserved;


        int hNonReserved = (nonReserved % 2 == 1) ? 100 : -100;

        double w1 = 1, w2 = 0;

        if (nMoves < 10) {
            w2 = 0.5;
            w1 = 0.5;
        }
        return w1 * hReserved + w2 * hNonReserved+ (r.nextDouble() * 2 - 1);

    }

    /*
    *   The method verifies if the board's position in (i,j) is a special one.
    *   It returns:
    *   1 if the position is a special for white
    *   0 if the position is a special for black
    *   -1 if the position is nota a special one
     */
    private int checkReserved(Board b, int i, int j) {
        int nBlack = 0, nWhite = 0, nBlocked = 0;
        int nPositions= 0;
        if(i-1>=0) {
            nPositions++;
            switch (evaluatePos(b, i - 1, j)) {
                case 1:
                    nWhite++;
                    break;
                case 0:
                    nBlack++;
                    break;
                case -1:
                    break;
                case -2:
                    nBlocked++;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        if(i+1<d) {
            nPositions++;
            switch (evaluatePos(b, i + 1, j)) {
                case 1:
                    nWhite++;
                    break;
                case 0:
                    nBlack++;
                    break;
                case -1:
                    break;
                case -2:
                    nBlocked++;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        if(j-1>=0) {
            nPositions++;
            switch (evaluatePos(b, i, j - 1)) {
                case 1:
                    nWhite++;
                    break;
                case 0:
                    nBlack++;
                    break;
                case -1:
                    break;
                case -2:
                    nBlocked++;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        if(j+1<d) {
            nPositions++;
            switch (evaluatePos(b, i, j + 1)) {
                case 1:
                    nWhite++;
                    break;
                case 0:
                    nBlack++;
                    break;
                case -1:
                    break;
                case -2:
                    nBlocked++;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }


        switch (nPositions) {
            case 2:
                if(nBlack==1 || nWhite == 1)
                    return (nBlack==1) ? 0 : 1;
                else
                    return -1;
            case 3:
                if(nBlack+nWhite+nBlocked==2)
                    return (nBlack-nWhite==1) ? 0:1;
                else
                    return -1;
            default:
                if(nBlack+nWhite+nBlocked == 3 && nBlack<3 && nWhite<3)
                    return (nBlack-nWhite==1) ? 0 : 1;
                else
                    return -1;

        }
    }


    /*
    *
    *   The method evaluates the position in (i,j) of the board.
    *   It returns:
    *   1 if the position is occupied by a white pawn
    *   0 if the position is occupied by a black pawn
    *   -1 if the position is empty and valid
    *  -2 if the position is empty and not valid
    *
     */
    private Integer evaluatePos(Board b, int i, int j) {
        int v = b.getCol(i, j);
        if (v == -1)
            return validMove(b, i, j) ? -1 : -2;
        return v;
    }



    /*
    *
    *  The method verifies if the position in (i,j) is a valid move for the player
    * It returns true if the position is valid, false otherwise
    *
    */
    private boolean validMove(Board b, int i, int j) {
        int nPawn = b.getPawn(i, j);

        if (i < 0 || i >= d || j < 0 || j >= d)
            return false;

        if (nPawn != 0)
            return false;

        int np = 0;
        int nn = 0;

        if (i - 1 >= 0 && b.getPawn(i - 1, j) != 0)
            if (b.getPawn(i - 1, j) > 0)
                np += 1;
            else
                nn += 1;

        if (i + 1 < d && b.getPawn(i + 1, j) != 0)
            if (b.getPawn(i + 1, j) > 0)
                np += 1;
            else
                nn += 1;

        if (j - 1 >= 0 && b.getPawn(i, j - 1) != 0)
            if (b.getPawn(i, j - 1) > 0)
                np += 1;
            else
                nn += 1;

        if (j + 1 < d && b.getPawn(i, j + 1) != 0)
            if (b.getPawn(i, j + 1) > 0)
                np += 1;
            else
                nn += 1;


        return nn == np;
    }


}