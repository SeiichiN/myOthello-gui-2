package helper;

import java.awt.Color;
import java.util.ArrayList;

import model.MasuData;

public class Board implements Cloneable {
    private int row = 6;
    private int col = 6;
    private ArrayList<MasuData> arrayMasuData;

    public Board () {
        arrayMasuData = new ArrayList <MasuData> ();
        init();
    }
    
    public Board (int row, int col) {
    	arrayMasuData = new ArrayList <MasuData> ();
    	this.row = row;
    	this.col = col;
    	init();
    }
    
    public int getRow () { return this.row; }
    public int getCol () { return this.col; }
    public void setRow (int row) { this.row = row; }
    public void setCol (int col) { this.col = col; }
    
    /**
     * ボード作成時に初期値をもったデータで
     * 埋めておく。
     * 初期値: num -- 0 からの連番
     *         color -- Color.GREEN
     */
    private void init () {
    	int count = 0;
        for (int i = 1; i <= this.row; i++) {
            for (int t = 1; t <= this.col; t++) {
                MasuData masu = new MasuData( i, t );
                // System.out.println( "Board:41:count:" + count );
                masu.setNum( count );
                arrayMasuData.add( masu );
                count++;
            }
        }
    }
    
    @Override
    public Board clone () {
    	Board cloneBoard = new Board( this.row, this.col );
    	// System.out.println("Board-52 size:" + this.arrayMasuData.size());
    	// System.exit(1);
    	try {
    		for (int i = 0; i < this.arrayMasuData.size(); i++) {
    			MasuData _masuData = this.arrayMasuData.get(i).clone();
    			// System.out.println("Board-56 this.arrayMasuData(" + i + ") : player:" + this.arrayMasuData.get(i).getColor() );
    			cloneBoard.arrayMasuData.set( i, _masuData );
    			// System.out.println("Board-62 i:" + i + " _masuData.player:" + _masuData.getColor());
    		}
    	} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR!");
		}
    	
    	return cloneBoard;
    }

    public ArrayList<MasuData> getBoard () { return this.arrayMasuData; }
    public void setBoard( ArrayList<MasuData> _arrayMasuData ) {
        this.arrayMasuData = _arrayMasuData;
    }

    // index位置 i の　MasuData(マス情報)を返す
    public MasuData get (int i) {
        return arrayMasuData.get( i );
    }

    public void add (MasuData masuData) {
    	arrayMasuData.add( masuData );
    }

    /*
     * （デバッグ用）Board の中の要素を一覧する。
     */
    public void printAll () {
    	System.out.println("-----------------------< printAll >---------------------------");
    	this.arrayMasuData.forEach( ele -> {
    		System.out.println("Y:" + getY(ele.getNum()) + " X:" + getX(ele.getNum()) + " " + ele.getColor());
    	});
    	System.out.println("-----------------------< printAll END >---------------------------");
    }
    
    /**
     * (row, col)の位置の index位置を返す
     * 
     * @param:
     *   row -- 1...6
     *   col -- 1...6
     * @return:
     *   0...35
     */
    public int getIndex( int row, int col ) {
//        if (row < 1 || row > this.row) {
//            System.out.println("範囲外");
//            return 0;
//        } else if (col < 1 || col > this.col) {
//            System.out.println("範囲外");
//            return 0;
//        }
        return (row - 1) * this.col + (col - 1);
    }
    
    /**
     * index位置の Y 座標を返す
     * 
     * @param int index
     * @return int Y座標
     */
    public int getY( int index ) {
    	return index / this.col + 1;
    }
    
    /**
     * index 位置の x 座標を返す
     * 
     * @param int index
     * @return int x座標
     */
    public int getX( int index ) {
    	return index % this.col + 1;
    }

    /**
     * 現在地(nowMasu)を基準として、direction の方向にある隣点の
     * 情報を得る
     * @param int direction -- 方向 0...7 0:北 1:北東 2:東
     * @param MasuData nowMasu     -- 現在地のマス
     * @return MasuData masuData -- その地点のマスデータ
     */
    public MasuData neighbor (int direction, MasuData nowMasu) {
    	// System.out.println("nowMasu:" + nowMasu.getNum());
    	MasuData[] neighbors = neighbors( nowMasu.getNum() );
    	// System.out.println("neighbors:" + neighbors[direction].getNum());
    	return neighbors[ direction ];
    }

    public MasuData[] neighbors (int index) {
    	int row = index / this.col + 1;
    	int col = index % this.col + 1;
    	// System.out.println("Board-96: row:" + row + " col:" + col);
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
     *   
     *   隣の地点は全部で8個ある。もしその地点が盤外なら
     *   Color.RED とする。
     *   盤外でなかったら、その地点の情報をセットする。
     */
    public MasuData[] neighbors (int row, int col) {
        MasuData[] masus = new MasuData[8];

        // 0
        if (isOnBoard(row - 1 , col)) {
            masus[0]= arrayMasuData.get( getIndex( row - 1, col ) );
        } else {
            masus[0] = new MasuData( Color.RED );
        }

        // 1
        if (isOnBoard(row - 1 , col + 1)) {
            masus[1]= arrayMasuData.get( getIndex( row - 1, col + 1) );
        } else {
            masus[1] = new MasuData( Color.RED );
        }

        // 2
        if (isOnBoard(row , col + 1)) {
            masus[2]= arrayMasuData.get( getIndex( row, col + 1) );
        } else {
            masus[2] = new MasuData( Color.RED );
        }

        // 3
        if (isOnBoard(row + 1, col + 1)) {
            masus[3]= arrayMasuData.get( getIndex( row + 1, col + 1) );
        } else {
            masus[3] = new MasuData( Color.RED );
        }

        // 4
        if (isOnBoard(row + 1, col)) {
            masus[4]= arrayMasuData.get( getIndex( row + 1, col) );
        } else {
            masus[4] = new MasuData( Color.RED );
        }

        // 5
        if (isOnBoard(row + 1, col - 1)) {
            masus[5]= arrayMasuData.get( getIndex( row + 1, col - 1) );
        } else {
            masus[5] = new MasuData( Color.RED );
        }

        // 6
        if (isOnBoard(row, col - 1)) {
            masus[6]= arrayMasuData.get( getIndex( row, col - 1) );
        } else {
            masus[6] = new MasuData( Color.RED );
        }

        // 7
        if (isOnBoard(row - 1, col - 1)) {
            masus[7]= arrayMasuData.get( getIndex( row - 1, col - 1) );
        } else {
            masus[7] = new MasuData( Color.RED );
        }

        return masus;
    }
    
	public boolean isOnBoard(int y, int x) {
		if (y < 1)
			return false;
		if (y > this.row)
			return false;
		if (x < 1)
			return false;
		if (x > this.col)
			return false;
		return true;
	}
}
