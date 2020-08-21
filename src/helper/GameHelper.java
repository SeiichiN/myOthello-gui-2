// GameHelper.java

package helper;

import java.awt.Color;

import model.MasuData;

public class GameHelper {
    private int row = 6;
    private int col = 6;
    private int board_masu_num = 36;
    private Board board;
    private MasuData masudata;
    private int enemyPoint = 0;

    public GameHelper( Board board ) {
        this.board = board;
    }
    public GameHelper( int row, int col, Board board ) {
        this.row = row;
        this.col = col;
        this.board_masu_num = row * col;
        this.board = board;
    }

    // index位置の x座標 -- 0...6
    public int getX ( int index ) {
        return index % col;
    }

    // index位置の y座標
    public int getY ( int index ) {
        return (int)Math.floor( index / col ) ;
    }

    /**
     * 八方向それぞれの通し番号位置を調べる。
     */
//	    public Direction[] setDirection ( Direction[] direction ) {
//	    for (int i = 0; i < 8; i++) {
//	        direction[i] = new Direction();
//	    }
//	    direction[0].setDir( - col - 1 );
//	    direction[1].setDir( - col );
//	    direction[2].setDir( - col + 1 );
//	    direction[3].setDir( - 1 );
//	    direction[4].setDir( + 1 );
//	    direction[5].setDir( + col - 1 );
//	    direction[6].setDir( + col );
//	    direction[7].setDir( + col + 1 );
//	    return direction;
//	}

    /**
     * そのマスにコマを置けるかどうかを調べる
     * @param:
     *   int serialNum -- 現在のポイント
     *   Color color -- 現在のプレーヤー
     */
    public int canMove ( int serialNum, Color player ) {
        int point = 0;
        // board = new Board ();
        // board.setBoard( this.board );
        MasuData[] neighborData = board.neighbors( serialNum );
        // System.out.println("serialNum:" + serialNum + " player:" + player);
        for (int i = 0; i < 8; i++) {
            // System.out.println("masuData  i:" + i + " color:" + neighborData[i].getColor() );
            point = point + countEnemy( i, neighborData[i], player, 0 );
            // System.out.println("now point:" + point);
        }
        return point;
    }

    /**
     * countEnemy -- その方向に敵コマがどれだけいるかを調べる。
     * @param:
     *   Direction direction -- 方向とポイントを保持する構造体がわりのクラス
     *   int index -- プレーヤーが石を置きたい場所
     *   Color player -- 現在のプレーヤー
     */
    /**
     * @param direction -- 7|0|1
     *                     --+-+--
     *                      6|*|2
     *                     --+-+--
     *                      5|4|3
     *
     * @param masuData  -- 調べたい地点のマスデータ
     * @param player    -- 現在のプレーヤー
     * @return
     */
    public int countEnemy ( int direction, MasuData masuData, Color player, int point ) {
//    	System.out.println("direction:" + direction +
//    			"masu:" + masuData.getColor() +
//    			"player:" + player);

        // 敵プレーヤー
        Color enemy = (player.equals( Color.BLACK )) ? Color.WHITE : Color.BLACK;

        // もしその方向の隣が敵石だったら、ポイントを1プラスして、
        // さらにその方向に調査をすすめる。
        if ( masuData.getColor().equals( enemy ) ) {
            // System.out.println("敵石見っけ!");
            point++;
            // System.out.println("現在:" + masuData.getNum() + " 次の方向:" + direction );
            MasuData nextMasu = board.neighbor(direction,  masuData);
            // System.out.println("次の地点:" + nextMasu.getNum() + " nextMasu:" + nextMasu.getColor());
            return countEnemy( direction, nextMasu, player, point ) ;
        }

        // もしその方向の隣が味方の石だったら、現在のポイントを返す。
        // 敵石が無くてすぐに味方の石だったら、現在のポイントは 0 である。
        if ( masuData.getColor().equals( player )) {
            // System.out.println("味方の石!:" + masuData.getNum() + "point:" + point );
            return point;
        }

        // その方向に石が無い、つまり緑であれば、ゼロである。
        // 仮に敵石が続いてのち緑であれば、ポイントはゼロになる。
        return 0;
    }
}
