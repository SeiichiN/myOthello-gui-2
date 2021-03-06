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

    public Bot ( Board board ) {
        this.row = board.getRow();
        this.col = board.getCol();
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
        int enemyPoint = 0;
        maxPoint = 0;
        GameHelper gameHelper = new GameHelper( board );
        // Action action = null;
        int greenCount = 0;

        for (int i = 0; i < masu_num; i++) {
            // すでに石が置かれているマスは除外
            if (board.get(i).getColor().equals( Color.GREEN )) {
                greenCount++;

                // pointがあるということは、挟める石があるということ
                point = gameHelper.canMove( i, Color.WHITE );

                enemyPoint = gameHelper.virtualSelectMove( i, board, Color.WHITE );

                if (point > 0) {
                    // もし i が盤面のへんならば、5ポイントをプラス。
                    // if (i % col == 0 || i % col == (col - 1)) {
                    //    point = point + 5;
                    // }
                	if (board.getX(i) == 1 || board.getX(i) == this.col) point = point + 5;
                	if (board.getY(i) == 1 || board.getY(i) == this.row) point = point + 5;

                	// point = point - enemyPoint;
                	// if (point <= 0) point = 1;
                }

                // デバッグ用
                // ポイントの表示
                if (point > 0 && enemyPoint >= 0)
                	System.out.println("Bot-52 Y:" + board.getY(i) + " X:" + board.getX(i) + " point:" + point + " enemyPoint:" + enemyPoint );

                // もし i が盤面のかどならば、10ポイントをプラス。
                // こうすると、よりつよくなるだろう。
                if (point > 0) {

                    if (point > maxPoint) {
                        maxPoint = point;
                    }
                    Action action = new Action();
                    action.setIndexXY( i, this.col );
                    action.setPoint( point );
                    action.setEnemyPoint( enemyPoint );
                    actionList.add( action );
                }
            }
        }
        // System.out.println("Bot-85:-----------------------------------------");

        // for DEBUG
        // actionList.forEach ( ele -> {
        //         System.out.println( "Y:"+ ele.getY() +
        //                             " X:" + ele.getX() +
        //                             " P:" + ele.getPoint() );
        //     });
        // System.exit(1);
        // System.out.println("maxPoint:" + maxPoint);

        // 挟めるコマがない場合
        if (maxPoint == 0) {
            Action action = new Action();
            if (greenCount == 0) {
                action.isEnd = true;
            }
            // System.out.println("挟めるコマが無い!");
            action.setPoint(0);
            action.setY(0);
            action.setX(0);
            action.setIndex(0);
            actionList.add( action );
            System.out.println( "Bot-106 " + action.toString() );
            return action;
        }

        // pointが最も高い action のリストを作成する。
        ArrayList <Action> bestActionList = new ArrayList <> ();

        maxPoint = -100; // expectPoint が -100 になることはない。
        actionList.forEach( ele -> {
        	// ele.getPoint() と ele.getEnemyPoint() とを考慮する。
        	int expectPoint = ele.getPoint() - ele.getEnemyPoint();
        	// ele.getPoint() が maxPoint よりも大きい場合、maxPointを更新して、
        	// bestActionListをクリアしたのち、bestActionListを作り直す。
			if (expectPoint > maxPoint) {
				maxPoint = expectPoint;
				bestActionList.clear();
				bestActionList.add(ele);
			}
			// ele.getPoint() が maxPoint と同じ場合、bestActionList に追加する。
			else if (expectPoint == maxPoint) {
				bestActionList.add(ele);
			}
		});
        int index = new Random().nextInt( bestActionList.size() );
        Action bestAction = bestActionList.get( index );
        // for DEBUG
        // System.out.println( "Y:" + bestAction.getY() +
        //                     " X:" + bestAction.getX() +
        //                     " P:" + bestAction.getPoint() );
        // System.exit(1);
        System.out.println("Bot-136 bestAction Y:" + bestAction.getY() + " X:" + bestAction.getX() + " point:" + maxPoint);
        return bestAction;
    }
}

// 修正時刻: Mon Aug 10 18:36:52 2020
