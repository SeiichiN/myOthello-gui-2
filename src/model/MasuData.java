
package model;

import java.awt.Color;

/**
 * 盤面の情報を保持するクラス
 */
public class MasuData {
    private int num = 0;	// マスのindex番号。0から始まる。
    private Color color = Color.GREEN;
    private int row = 0;
    private int col = 0;
    
    public MasuData () {};
    
    public MasuData ( int row, int col ) {
    	this.row = row;
    	this.col = col;
    }
    
    public MasuData ( Color color ) {
    	this.color = color;
    }
    
    public int getNum () { return this.num; }
    public void setNum ( int num ) { this.num = num; }
    
    public Color getColor () { return this.color; }
    public void setColor ( Color color ) { this.color = color; }

    public int getRow () { return this.row; }
    public void setRow ( int row ) { this.row = row; }
    public int getCol () { return this.col; }
    public void setCol ( int col ) { this.col = col; }
}

// 修正時刻: Sun Aug  9 09:18:54 2020
