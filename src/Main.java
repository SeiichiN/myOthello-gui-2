// Main.java

import java.awt.Color;
import java.util.Map;

import agent.Bot;
import gui.NukGui;
import model.Action;
import util.GetConf;

public class Main {

    public static void main (String[] args) {
        // 盤面設定情報を game.conf から読み取る。
        Map<String, String> mapList = new GetConf("conf/game.conf").load();
        int col = Integer.parseInt( mapList.get("col") );
        int row = Integer.parseInt( mapList.get("row") );

        NukGui gui = new NukGui (row, col);
        gui.render ();
        gui.init();
        Color player;
        Action action = null;
        Bot bot = new Bot( row, col, gui.getBoard() );

        while (true){
            player = gui.getPlayer();

            try {
                Thread.sleep( 1000 );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
            // System.out.println("player:" + player);
            // 黒が石を置いたとき、Botの手をうつ。
            if (player.equals( Color.BLACK )) {
                System.out.println("---> Bot!!! <---");
                action = bot.selectMove();
                if (action.getPoint() == 0 ) {
                    // 挟めるコマが無い
                    System.out.println("挟めるコマが無い");
                    if (action.isEnd) {
                        System.out.println("END!");
                        gui.gameEnd();
                        break;
                    } else {
                        gui.move_pass();
                    }
                } else {
                    System.out.println ("Y:" + action.getY() +
                            " X:" + action.getX() +
                            " P:" + action.getPoint() );

                    gui.move( action.getY(), action.getX() );
                }
            }
        }
    }
}

// 修正時刻: Thu Aug  6 06:07:17 2020

