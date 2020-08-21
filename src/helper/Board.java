package helper;

import java.awt.Color;
import java.util.ArrayList;

import model.MasuData;

public class Board {
    private static final int ROW = 6;
    private static final int COL = 6;
    private ArrayList<MasuData> board;

    public Board () {
        board = new ArrayList<MasuData>();

        for (int i = 1; i <= ROW; i++) {
            for (int t = 1; t <= COL; t++) {
                MasuData masu = new MasuData( i, t );
                board.add( masu );
            }
        }
    }

    public ArrayList<MasuData> getBoard () { return this.board; }
    public void setBoard( ArrayList<MasuData> board ) {
        this.board = board;
    }

    public MasuData get (int i) {
        return board.get( i );
    }

    public void add (MasuData masuData) {
    	board.add( masuData );
    }

    /**
     * @param:
     *   row -- 1...6
     *   col -- 1...6
     * @return:
     *   0...35
     */
    public int getIndex( int row, int col ) {
        if (row < 1 || row > ROW) {
            System.out.println("範囲外");
            return 0;
        } else if (col < 1 || col > COL) {
            System.out.println("範囲外");
            return 0;
        }
        return (row - 1) * COL + (col - 1);
    }

    /**
     *
     * @param direction -- 方向 0...7 0:北 1:北東 2:東
     * @param nowMasu     -- 現在地のマス
     * @return MasuData masuData -- その地点のマスデータ
     */
    public MasuData neighbor (int direction, MasuData nowMasu) {
    	// System.out.println("nowMasu:" + nowMasu.getNum());
    	MasuData[] neighbors = neighbors( nowMasu.getNum() );
    	// System.out.println("neighbors:" + neighbors[direction].getNum());
    	return neighbors[ direction ];
    }

    public MasuData[] neighbors (int index) {
    	int row = index / COL + 1;
    	int col = index % COL + 1;
    	// System.out.println("row:" + row + " col:" + col);
    	return neighbors( row, col );
    }

    /**
     *
     *   (row, col)のまわりのマスのじょうほうを返す
     *   @param: int row, int col
     *   @return:
     *     MasuData[] -- MasuData[0]...MasuData[7]のじょうほう
     *
     *   7 | 0 | 1
     *  ---+---+---
     *   6 | * | 2
     *  ---+---+---
     *   5 | 4 | 3
     */
    public MasuData[] neighbors (int row, int col) {
        MasuData[] masus = new MasuData[8];

        if (Helper.isOnBoard(row - 1 , col)) {
            masus[0]= board.get( getIndex( row - 1, col ) );
        } else {
            masus[0] = new MasuData( Color.RED );
        }

        if (Helper.isOnBoard(row - 1 , col + 1)) {
            masus[1]= board.get( getIndex( row - 1, col + 1) );
        } else {
            masus[1] = new MasuData( Color.RED );
        }

        if (Helper.isOnBoard(row , col + 1)) {
            masus[2]= board.get( getIndex( row, col + 1) );
        } else {
            masus[2] = new MasuData( Color.RED );
        }

        if (Helper.isOnBoard(row + 1, col + 1)) {
            masus[3]= board.get( getIndex( row + 1, col + 1) );
        } else {
            masus[3] = new MasuData( Color.RED );
        }

        if (Helper.isOnBoard(row + 1, col)) {
            masus[4]= board.get( getIndex( row + 1, col) );
        } else {
            masus[4] = new MasuData( Color.RED );
        }

        if (Helper.isOnBoard(row + 1, col - 1)) {
            masus[5]= board.get( getIndex( row + 1, col - 1) );
        } else {
            masus[5] = new MasuData( Color.RED );
        }

        if (Helper.isOnBoard(row, col - 1)) {
            masus[6]= board.get( getIndex( row, col - 1) );
        } else {
            masus[6] = new MasuData( Color.RED );
        }

        if (Helper.isOnBoard(row - 1, col - 1)) {
            masus[7]= board.get( getIndex( row - 1, col - 1) );
        } else {
            masus[7] = new MasuData( Color.RED );
        }

        return masus;
    }
}
