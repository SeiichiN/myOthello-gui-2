// Direction.java

package model;

/**
 * Cにおける構造体のようなものとしてクラスを使う
 * Direction -- 方向
 * 着手する地点を (0) として、北西方向が -(col + 1)、
 * 南西方向が +(col - 1)、北方向が -col となる。
 * また、その方向にある敵コマの数を point として保持する。
 */
public class Direction {
    private int dir = 0;
    private int point = 0;

    public Direction () {}

    public Direction ( int dir ) {
        this.dir = dir;
    }

    public Direction (int dir, int point) {
        this.dir = dir;
        this.point = point;
    }

    public int getDir() { return this.dir; }
    public int getPoint() { return this.point; }
    public void setDir(int dir) { this.dir = dir; }
    public void setPoint(int point) { this.point = point; }    
    public void addPoint(int point) { this.point = this.point + point; }
}

// 修正時刻: Sun Aug  9 12:29:34 2020
