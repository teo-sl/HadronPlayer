package hadron.heuristic;

import hadron.board.Board;

import java.util.Random;

public class MyHeuristicV1 implements Heuristic {
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
        if (nMoves == 0) return -1_000_000D;
        double hReserved = (reservedWhite - reservedBlack) * 100;

        // col == 1 white; col == 0 black;
        hReserved = (col == 1) ? hReserved : -1 * hReserved;
        double hNonReserved = (nonReserved % 2 == 1) ? 100 : -100;

        double w1 = 1, w2 = 0;

        // 0.3829 0.9499 51
        if (nMoves < 10) {
            w2 = 0.5;
            w1 = 0.5;
        }
        return w1 * hReserved + w2 * hNonReserved + (r.nextDouble() * 2 - 1);

    }

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

        if (nPositions == 2) {
            if (Math.abs(nBlack - nWhite) == 1) {
                return (nBlack - nWhite == 1) ? 0 : 1;
            }
            return -1;
        }
        if (nPositions == 3) {
            if (nBlack + nWhite + nBlocked == 2) return (nBlack - nWhite == 1) ? 0 : 1;
            return -1;

        }
        if (nBlack + nWhite + nBlocked == 3 && nBlack < 3 && nWhite < 3)
            return (nBlack - nWhite == 1) ? 0 : 1;
        return -1;

    }


    private Integer evaluatePos(Board b, int i, int j) {
        int v = b.getCol(i, j);
        if (v == -1)
            return validMove(b, i, j) ? -1 : -2;
        return v;
    }


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