// Action.java

package model;

import java.io.Serializable;

/**
 * Actionクラス -- Cの構造体のように使うためのクラス
 */
public class Action implements Serializable {

	private static final long serialVersionUID = 1L;
	private int x = 0;
    private int y = 0;
    private int index = 0;
    private int point = 0;
    private int enemyPoint = 0;
    public boolean isEnd = false;

    public Action () {}
    public Action ( int index ) {
        this.index = index;
    }
    public Action ( int x, int y ) {
        this.x = x;
        this.y = y;
    }

    public int getX () { return this.x; }
    public int getY () { return this.y; }
    public int getIndex () { return this.index; }
    public int getPoint () { return this.point; }
    public int getEnemyPoint () { return this.enemyPoint; }

    public void setX ( int x ) { this.x = x; }
    public void setY ( int y ) { this.y = y; }
    public void setIndex ( int index ) { this.index = index; }
    public void setPoint ( int point ) { this.point = point; }
    public void setEnemyPoint ( int enemyPoint ) { this.enemyPoint = enemyPoint; }

    public void setIndexXY( int index, int col ) {
        this.index = index;
        this.y = (int) Math.floor( index / col ) + 1;
        this.x = index % col + 1;
    }

    public String toString () {
        String text = "x=" + Integer.toString( getX() ) +
            " y=" + Integer.toString( getY() ) +
            " index=" + Integer.toString( getIndex() ) +
            " point=" + Integer.toString( getPoint() ) +
            " enemyPoint=" + Integer.toBinaryString( getEnemyPoint() );
        return text;
    }
}

// 修正時刻: Mon Aug 10 17:39:03 2020
