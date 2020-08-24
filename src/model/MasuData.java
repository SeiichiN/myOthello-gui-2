
package model;

import java.awt.Color;
import java.io.Serializable;

/**
 * 盤面の情報を保持するクラス
 */
public class MasuData implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	private int num = 0;	// マスのindex番号。0から始まる。
    private Color color = Color.GREEN;
    private int xPos = 0;
    private int yPos = 0;

    public MasuData () {};

    public MasuData ( Color color ) {
    	this.color = color;
    }
    
    public int getNum () { return this.num; }
    public void setNum ( int num ) { this.num = num; }

    public Color getColor () { return this.color; }
    public void setColor ( Color color ) { this.color = color; }
    
    public int getXPos () { return this.xPos; }
    public void setXpos (int x) { this.xPos = x; }
    
    public int getYPos () { return this.yPos; }
    public void setYPos (int y) { this.yPos = y; }

    @Override
    public MasuData clone () {
    	MasuData _data = new MasuData();
		try {
			_data.num = this.num;
			_data.color = this.color;
			_data.xPos = this.xPos;
			_data.yPos = this.yPos;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return _data;
    }
}

// 修正時刻: Sun Aug  9 09:18:54 2020
