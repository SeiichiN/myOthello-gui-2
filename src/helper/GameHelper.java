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
        this.row = board.getRow();
        this.col = board.getCol();
        this.board_masu_num = this.row * this.col;
    }
//    public GameHelper( int row, int col, Board board ) {
//        this.row = row;
//        this.col = col;
//        this.board_masu_num = row * col;
//        this.board = board;
//    }

//    // index位置の x座標 -- 0...6
//    public int getX ( int index ) {
//        return index % col;
//    }
//
//    // index位置の y座標
//    public int getY ( int index ) {
//        return (int)Math.floor( index / col ) ;
//    }

    /**
     * そのマスにコマを置けるかどうかを調べる
     * @param:
     *   int serialNum -- 現在の地点。通し番号。0...36 (6x6の場合)
     *   Color color -- 現在のプレーヤー
     * @return:
     *   int point -- そのマスにコマを置いた場合、どれだけの敵コマを
     *            ひっくり返せるか。その数を返す。
     *            ゼロなら、そのマスにはコマを置けない。
     */
    public int canMove ( int serialNum, Color player ) {
        int point = 0;

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
     * @param direction -- 7 | 0 | 1
     *                     ---+---+---
     *                      6 | * | 2
     *                     ---+---+---
     *                      5 | 4 | 3
     *
     * @param neighbor  -- 調べたい地点のマスデータ(neighbors[i])
     * @param player    -- 現在のプレーヤー
     * @param point     -- 敵がいれば point が増える。初期値として 0 が渡される。
     * @return
     */
    public int countEnemy ( int direction, MasuData neighbor, Color player, int point ) {

        // 敵プレーヤー
        Color enemy = (player.equals( Color.BLACK )) ? Color.WHITE : Color.BLACK;

        // もしその方向の隣が敵石だったら、ポイントを1プラスして、
        // さらにその方向に調査をすすめる。
        if ( neighbor.getColor().equals( enemy ) ) {
            // System.out.println("敵石見っけ!");
            point++;
            // System.out.println("現在:" + masuData.getNum() + " 次の方向:" + direction );
            // neighborのそのまたneighborの地点のマスを取得
            MasuData nextNeighbor = board.neighbor( direction,  neighbor );
            // System.out.println("次の地点:" + nextMasu.getNum() + " nextMasu:" + nextMasu.getColor());
            // nextNeighbor を引数にして再帰処理
            return countEnemy( direction, nextNeighbor, player, point ) ;
        }

        // もしその方向の隣が味方の石だったら、現在のポイントを返す。
        // 敵石が無くてすぐに味方の石だったら、現在のポイントは 0 である。
        if ( neighbor.getColor().equals( player )) {
            // System.out.println("味方の石!:" + masuData.getNum() + "point:" + point );
            return point;
        }

        // その方向に石が無い、つまり緑であれば、ゼロである。
        // 仮に敵石が続いてのち緑であれば、ポイントはゼロになる。
        // しかし、この return が処理されることはないだろう。
        return 0;
    }
    

    
    /**
     * 敵の手を考慮に入れた指し手を考える
     * 
     * @param int index -- 検討すべき指し手の index番号
     * @param board     -- 検討土台の盤面
     * @param player    -- 現在のプレーヤー
     * @return int point -- 敵の指し手のポイント（マイナス値になる）
     */
    public int enemySelectMove( int index, Board board, Color player ) {
    	int enemyPoint = 0;
    	
    	Board nextBoard = null;

    	nextBoard = board.clone();    // 仮想の盤面
    	
    	nextBoard.get( index ).setColor( player );
    	
    	Color enemyPlayer = ( player.equals(Color.BLACK) ) ? Color.WHITE : Color.BLACK;
    	enemyPoint = canMoveVertial( index, enemyPlayer, nextBoard );
    	
    	return enemyPoint;
    }

    /**
     * そのマスにコマを置けるかどうかを調べる
     * @param:
     *   int serialNum -- 現在の地点。通し番号。0...36 (6x6の場合)
     *   Color color -- 現在のプレーヤー
     *   Board vBoard -- 仮想の盤面
     * @return:
     *   int point -- そのマスにコマを置いた場合、どれだけの敵コマを
     *            ひっくり返せるか。その数を返す。
     *            ゼロなら、そのマスにはコマを置けない。
     */
    public int canMoveVertial ( int serialNum, Color player, Board vBoard ) {
        int point = 0;

        MasuData[] neighborData = vBoard.neighbors( serialNum );
        // System.out.println("serialNum:" + serialNum + " player:" + player);
        for (int i = 0; i < 8; i++) {
            // System.out.println("masuData  i:" + i + " color:" + neighborData[i].getColor() );
            point = point + countEnemyVirtial( i, neighborData[i], player, 0, vBoard );
            // System.out.println("now point:" + point);
        }
        return point;
    }

    /**
     * countEnemy -- その方向に敵コマがどれだけいるかを調べる。
     * @param direction -- 7 | 0 | 1
     *                     ---+---+---
     *                      6 | * | 2
     *                     ---+---+---
     *                      5 | 4 | 3
     *
     * @param neighbor  -- 調べたい地点のマスデータ(neighbors[i])
     * @param player    -- 現在のプレーヤー
     * @param point     -- 敵がいれば point が増える。初期値として 0 が渡される。
     * @param Board vBoard -- 仮想の盤面
     * @return
     */
    public int countEnemyVirtial ( int direction, MasuData neighbor, Color player, int point, Board vBoard ) {

        // 敵プレーヤー
        Color enemy = (player.equals( Color.BLACK )) ? Color.WHITE : Color.BLACK;

        // もしその方向の隣が敵石だったら、ポイントを1プラスして、
        // さらにその方向に調査をすすめる。
        if ( neighbor.getColor().equals( enemy ) ) {
            // System.out.println("敵石見っけ!");
            point++;
            // System.out.println("現在:" + masuData.getNum() + " 次の方向:" + direction );
            // neighborのそのまたneighborの地点のマスを取得
            MasuData nextNeighbor = vBoard.neighbor( direction,  neighbor );
            // System.out.println("次の地点:" + nextMasu.getNum() + " nextMasu:" + nextMasu.getColor());
            // nextNeighbor を引数にして再帰処理
            return countEnemy( direction, nextNeighbor, player, point ) ;
        }

        // もしその方向の隣が味方の石だったら、現在のポイントを返す。
        // 敵石が無くてすぐに味方の石だったら、現在のポイントは 0 である。
        if ( neighbor.getColor().equals( player )) {
            // System.out.println("味方の石!:" + masuData.getNum() + "point:" + point );
            return point;
        }

        // その方向に石が無い、つまり緑であれば、ゼロである。
        // 仮に敵石が続いてのち緑であれば、ポイントはゼロになる。
        // しかし、この return が処理されることはないだろう。
        return 0;
    }

}
