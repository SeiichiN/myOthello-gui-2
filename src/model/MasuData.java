
package model;

import java.awt.Color;

/**
 * 盤面の情報を保持するクラス
 */
public class MasuData implements Cloneable {
    private int num = 0;	// マスのindex番号。0から始まる。
    private Color color = Color.GREEN;

    public MasuData () {};

    public MasuData ( Color color ) {
    	this.color = color;
    }

    public int getNum () { return this.num; }
    public void setNum ( int num ) { this.num = num; }

    public Color getColor () { return this.color; }
    public void setColor ( Color color ) { this.color = color; }

    @Override
    public MasuData clone () {
    	MasuData _data = new MasuData();
		try {
			_data.num = this.num;
			_data.color = this.color;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return _data;
    }
}

// 修正時刻: Sun Aug  9 09:18:54 2020
