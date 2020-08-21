// Bot.java

package agent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import helper.Board;
import helper.GameHelper;
import model.Action;

public class Bot {

    private int row;
    private int col;
    private Board board;
    private int maxPoint = 0;
    private int masu_num;

    public Bot ( int row, int col, Board board ) {
        this.row = row;
        this.col = col;
        this.board = board;
        this.masu_num = row * col;
    }

    /**
     * 最善手を求める
     * ここでは挟める敵石が最も多い手を最善手とする
     */
    public Action selectMove () {
        ArrayList <Action> actionList = new ArrayList <> ();
        int point = 0;
        maxPoint = 0;
        GameHelper gameHelper = new GameHelper( row, col, board );
        Action action = null;
        int greenCount = 0;

        for (int i = 0; i < masu_num; i++) {
            // すでに石が置かれているマスは除外
            if (board.get(i).getColor().equals( Color.GREEN )) {
                greenCount++;

                // pointがあるということは、挟める石があるということ
                point = gameHelper.canMove( i, Color.WHITE );

                // もし i が盤面のかどならば、10ポイントをプラス。
                // こうすると、よりつよくなるだろう。
                if (point > 0) {
                    // もし i が盤面のへんならば、5ポイントをプラス。
                    if (i % col == 0 || i % col == (col - 1)) {
                        point = point + 5;
                    }
                    int _row = (int)Math.floor( i / col );
                    if ( _row == 0 || _row == (row - 1) ) {
                        point = point + 5;
                    }

                    if (point > maxPoint) {
                        maxPoint = point;
                    }
                    action = new Action();
                    action.index2xy( i, col );
                    action.setPoint( point );
                    actionList.add( action );
                }
            }
        }
        // for DEBUG
        // actionList.forEach ( ele -> {
        //         System.out.println( "Y:"+ ele.getY() +
        //                             " X:" + ele.getX() +
        //                             " P:" + ele.getPoint() );
        //     });
        // System.exit(1);
        System.out.println("maxPoint:" + maxPoint);

        // 挟めるコマがない場合
        if (maxPoint == 0) {
            action = new Action();
            if (greenCount == 0) {
                action.isEnd = true;
            }
            // System.out.println("挟めるコマが無い!");
            action.setPoint(0);
            action.setY(0);
            action.setX(0);
            action.setIndex(0);
            actionList.add( action );
            System.out.println( action.toString() );
            return action;
        }


        ArrayList <Action> bestActionList = new ArrayList <> ();

        maxPoint = 0;
        actionList.forEach( ele -> {
                if (ele.getPoint() > maxPoint) {
                    maxPoint = ele.getPoint();
                    bestActionList.add( ele );
                }
            });
        int index = new Random().nextInt( bestActionList.size() );
        Action bestAction = bestActionList.get( index );
        // for DEBUG
        // System.out.println( "Y:" + bestAction.getY() +
        //                     " X:" + bestAction.getX() +
        //                     " P:" + bestAction.getPoint() );
        // System.exit(1);
        System.out.println("bestActionPoint:" + bestAction.getPoint() );
        return bestAction;
    }
}

// 修正時刻: Mon Aug 10 18:36:52 2020
