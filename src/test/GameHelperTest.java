package test;

import java.awt.Color;
import java.util.ArrayList;

import helper.Board;
import helper.GameHelper;
import model.MasuData;

public class GameHelperTest {
	private static int row = 8;
	private static int col = 8;

	public GameHelperTest () {}

	public static void main (String [] args) {

		Board board = new Board( row, col );

		// 最初に中央に4つのコマを置く。黒白黒白
		init( board );
		// gameHelperインスタンスを作成
		GameHelper gameHelper = new GameHelper( board );

		// 29(Y:4 X:6) の地点に黒を置いたとする。
		board.get( 29 ).setColor( Color.BLACK );
		// 29に黒を置いたので、挟んだ白をひっくり返す
		gameHelper.flipOverReal( 29, Color.BLACK);

		board.get(21).setColor( Color.white );
		gameHelper.flipOverReal( 21, Color.WHITE );
		board.get(13).setColor( Color.BLACK );
		gameHelper.flipOverReal( 13, Color.BLACK );
		// board.get(14).setColor( Color.white );
		// gameHelper.flipOverReal( 14, Color.WHITE );


//		int point = gameHelper.enemySelectMove( 21, board, Color.WHITE );
//		board.get(21).setColor( Color.WHITE );
//		gameHelper.flipOverReal(21, Color.WHITE);
//		System.out.println("point: " + point );
		board.printAll("board");

		// 21(Y:3 X:6) の地点に白を置いた場合、敵（黒）は何ポイント得るだろうか？
		// for (int i = 0; i < 64 ; i++) {
			// if (i == 19 || i == 21 || i == 37) {
				int i = 14;
				int enemyPoint = gameHelper.virtualSelectMove(i, board, Color.WHITE);

				System.out.println("GameHelperTest-50 Y:" + board.getY(i) +
						" X:" + board.getX(i) +
						" enemyPoint:" + enemyPoint);
			// }
		// }
	}

	public static void init (Board board) {

		ArrayList <MasuData> arrayMasuData = new ArrayList <> ();

		for (int i = 0; i < 64; i++) {
			MasuData masuData = new MasuData();
			masuData.setNum( i );
			masuData.setColor( Color.GREEN );
			arrayMasuData.add( masuData );
		}

		int initA = 27; // Black
		int initB = 28; // White
		int initC = 35; // White
		int initD = 36; // Black

		arrayMasuData.get( initA ).setColor( Color.BLACK );
		arrayMasuData.get( initB ).setColor( Color.WHITE );
		arrayMasuData.get( initC ).setColor( Color.WHITE );
		arrayMasuData.get( initD ).setColor( Color.BLACK );

		board.setArrayMasuData( arrayMasuData );
	}


}
