// GameHelper.java

package helper;

import java.awt.Color;
import java.util.ArrayList;

import model.MasuData;

public class GameHelper {
	private Board board;

	public GameHelper(Board board) {
		this.board = board;
	}

	/**
	 * そのマスにコマを置けるかどうかを調べる
	 * 
	 * @param: int serialNum -- 現在の地点。通し番号。0...36 (6x6の場合) Color color -- 現在のプレーヤー
	 * @return: int point -- そのマスにコマを置いた場合、どれだけの敵コマを ひっくり返せるか。その数を返す。
	 *          ゼロなら、そのマスにはコマを置けない。
	 */
	public int canMove(int serialNum, Color player) {
		int point = 0;

		MasuData[] neighborData = board.neighbors(serialNum);
		// System.out.println("serialNum:" + serialNum + " player:" + player);
		for (int i = 0; i < 8; i++) {
			// System.out.println("masuData i:" + i + " color:" + neighborData[i].getColor()
			// );
			point = point + countEnemy(i, neighborData[i], player, 0);
			// System.out.println("now point:" + point);
		}
		return point;
	}

	/**
	 * countEnemy -- その方向に敵コマがどれだけいるかを調べる。
	 * 
	 * @param direction -- 7 | 0 | 1 ---+---+--- 6 | * | 2 ---+---+--- 5 | 4 | 3
	 *
	 * @param neighbor  -- 調べたい地点のマスデータ(neighbors[i])
	 * @param player    -- 現在のプレーヤー
	 * @param point     -- 敵がいれば point が増える。初期値として 0 が渡される。
	 * @return
	 */
	public int countEnemy(int direction, MasuData neighbor, Color player, int point) {

		// 敵プレーヤー
		Color enemy = (player.equals(Color.BLACK)) ? Color.WHITE : Color.BLACK;

		// もしその方向の隣が敵石だったら、ポイントを1プラスして、
		// さらにその方向に調査をすすめる。
		if (neighbor.getColor().equals(enemy)) {
			// System.out.println("敵石見っけ!");
			point++;
			// System.out.println("現在:" + masuData.getNum() + " 次の方向:" + direction );
			// neighborのそのまたneighborの地点のマスを取得
			MasuData nextNeighbor = board.neighbor(direction, neighbor);
			// System.out.println("次の地点:" + nextMasu.getNum() + " nextMasu:" +
			// nextMasu.getColor());
			// nextNeighbor を引数にして再帰処理
			return countEnemy(direction, nextNeighbor, player, point);
		}

		// もしその方向の隣が味方の石だったら、現在のポイントを返す。
		// 敵石が無くてすぐに味方の石だったら、現在のポイントは 0 である。
		if (neighbor.getColor().equals(player)) {
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
	 * 仮に白（ボット）が index の地点に石を置いたとする。
	 *
	 * @param int    index -- 検討すべき指し手の index番号
	 * @param board  -- 検討土台の盤面
	 * @param player -- 現在のプレーヤー
	 * @return int point -- 敵の指し手のポイント（マイナス値になる）
	 */
	public int virtualSelectMove(int index, Board board, Color player) {
		int maxEnemyPoint = 0;

		Board nextBoard = null;

		nextBoard = board.clone(); // 仮想の盤面をつくる。

		// 仮想の盤面に引数で受け取った player(ボット、白） の指し手を適用する。
		nextBoard.get(index).setColor(player);

		// nextBoard.printAll();

		// プレーヤーを敵(黒)に設定する
		Color enemyPlayer = (player.equals(Color.BLACK)) ? Color.WHITE : Color.BLACK;
		// System.out.println("player:" + enemyPlayer);

		// それぞれ、その地点に黒が石を置いたとして、黒は何ポイント得ることができるか？
		for (int i = 0; i < nextBoard.getSerialNum(); i++) {
			ArrayList<MasuData> vMasuData = nextBoard.getBoard();

			// 緑のマス、つまり石が置かれていないマスに限る
			if (vMasuData.get(i).getColor().equals(Color.GREEN)) {

				// System.out.println("GameHelper-114 i:" + neighbors[i].getNum() +
				// " yPos:" + neighbors[i].getYPos() + " xPos:" + neighbors[i].getXPos() );

				// そのマスに敵が石を置いた場合の敵が獲得するだろうポイント
				int enemyPoint = canMoveVirtual(i, enemyPlayer, nextBoard);
				// System.out.println("GameHelper-114 i:" + neighbors[i].getNum() + "
				// enemyPoint:" + enemyPoint );

				if (enemyPoint > 0) {
					// そのマスの行位置
					int enemyYPos = vMasuData.get(i).getYPos();
					// そのマスの桁位置
					int enemyXPos = vMasuData.get(i).getXPos();
					// もしそのマスが角にあれば 50ポイントプラス
					if (nextBoard.onHLine(enemyYPos) && nextBoard.onVLine(enemyXPos))
						enemyPoint += 50;
					// もしそのマスが上辺あるいは下辺であれば 5ポイントプラス。
					else if (nextBoard.onHLine(enemyYPos))
						enemyPoint += 5;
					// もしそのマスが左辺あるいは右辺であれば 5ポイントプラス。
					else if (nextBoard.onVLine(enemyXPos))
						enemyPoint += 5;
					// System.out.println(
					// 		"GameHelper-131 黒 Y:" + enemyYPos + " X:" + enemyXPos + " enemyPoint:" + enemyPoint);

					// enemyPoint と point を比べて、point の方が大きければ、それを enemyPoint とする。
					maxEnemyPoint = (enemyPoint > maxEnemyPoint) ? enemyPoint : maxEnemyPoint;
					// System.out.println("GameHelper-141 方向:" + i +
					// " Point:" + point + "allPoint:" + enemyPoint +
					// " Player:" + enemyPlayer );

				}
			}
		}

		// 挟んでいる石を裏がえす
		flipOverVirtual(index, player, nextBoard);
		// 一覧
		// nextBoard.printAll("nextNextBoard");

		return maxEnemyPoint;
	}

	/**
	 * そのマスにコマを置けるかどうかを調べる
	 * 
	 * @param: int index -- 現在の地点。通し番号。0...36 (6x6の場合) Color color -- 現在のプレーヤー Board
	 *             vBoard -- 仮想の盤面
	 * @return: int point -- そのマスにコマを置いた場合、どれだけの敵コマを ひっくり返せるか。その数を返す。
	 *          ゼロなら、そのマスにはコマを置けない。
	 */
	public int canMoveVirtual(int index, Color player, Board vBoard) {
		int point = 0;

		MasuData[] neighborData = vBoard.neighbors(index);

		// System.out.println("serialNum:" + serialNum + " player:" + player);
		for (int i = 0; i < 8; i++) {
			// System.out.println("GameHelper-162 neighbor[i]:" + i + " color:" +
			// neighborData[i].getColor() );
			int temppoint = countEnemyVirtual(i, neighborData[i], player, 0, vBoard);
			// System.out.println("GameHelper-154 [" + i + "]" + neighborData[i].getYPos()
			// );

			point = point + temppoint;
		}

		return point;
	}

	/**
	 * countEnemy -- その方向に敵コマがどれだけいるかを調べる。
	 * 
	 * @param direction -- 7 | 0 | 1 ---+---+--- 6 | * | 2 ---+---+--- 5 | 4 | 3
	 *
	 * @param neighbor  -- 調べたい地点のマスデータ(neighbors[i])
	 * @param player    -- 現在のプレーヤー
	 * @param point     -- 敵がいれば point が増える。初期値として 0 が渡される。
	 * @param Board     vBoard -- 仮想の盤面
	 * @return
	 */
	public int countEnemyVirtual(int direction, MasuData neighbor, Color player, int point, Board vBoard) {

		// 敵プレーヤー
		Color enemy = (player.equals(Color.BLACK)) ? Color.WHITE : Color.BLACK;

		// System.out.println("GameHelper-184 neighbor yPos:" + neighbor.getYPos() + "
		// xPos:" + neighbor.getXPos() );

		// もしその方向の隣が敵石だったら、ポイントを1プラスして、
		// さらにその方向に調査をすすめる。
		if (neighbor.getColor().equals(enemy)) {
			point++;
			// System.out.println("GameHelper-192 敵石見っけ! point:" + point);
			// System.out.println("現在:" + masuData.getNum() + " 次の方向:" + direction );
			// neighborのそのまたneighborの地点のマスを取得
			MasuData nextNeighbor = vBoard.neighbor(direction, neighbor);
			// System.out.println("次の地点:" + nextMasu.getNum() + " nextMasu:" +
			// nextMasu.getColor());
			// nextNeighbor を引数にして再帰処理
			return countEnemyVirtual(direction, nextNeighbor, player, point, vBoard);
		}

		// もしその方向の隣が味方の石だったら、現在のポイントを返す。
		// 敵石が無くてすぐに味方の石だったら、現在のポイントは 0 である。
		// また、盤面の辺に石があれば、5ポイントプラス。
		if (neighbor.getColor().equals(player)) {
			if (point > 0 && (neighbor.getYPos() == 1 || neighbor.getYPos() == board.getRow()))
				point = point + 5;
			if (point > 0 && (neighbor.getXPos() == 1 || neighbor.getXPos() == board.getCol()))
				point = point + 5;

			// if (point > 0)
			// System.out.println("GameHelper-199 point:" + point);

			return point;
		}

		// その方向に石が無い、つまり緑であれば、ゼロである。
		// 仮に敵石が続いてのち緑であれば、ポイントはゼロになる。
		// しかし、この return が処理されることはないだろう。
		return 0;
	}

	/**
	 * 挟んだ敵の石を裏返す。 仮に 21(Y:3 X:6) の地点に白が石を置いたとする。 serialNum は 21。 player
	 * は、Color.WHITE。 board は仮想ボード
	 *
	 * @param: int serialNum -- 現在地（通し番号） Color player -- その石を置いたプレーヤー（石の色）
	 * @return: Board board -- 仮想の盤面
	 */
	public void flipOverVirtual(int serialNum, Color player, Board board) {
		// System.out.println("GameHelper-242 現在:" + serialNum + "player:" + player );
		// GameHelper gameHelper = new GameHelper( board );

		// 通し番号serialNum の地点の隣の地点を調べる
		MasuData[] neighbors = board.neighbors(serialNum);

		// for (int i = 0; i < 8; i++) {
		// System.out.println("NukGui-385: neighbors[" + i + "]=" +
		// neighbors[i].getNum() + "color=" + neighbors[i].getColor());
		// }

		// 八方向すべてについて検討する。
		// i -- 方向を表す
		// 7 | 0 | 1
		// ---+---+---
		// 6 | * | 2
		// ---+---+---
		// 5 | 4 | 3
		//
		for (int i = 0; i < 8; i++) {
			// 各方向について敵石の数をカウントする。
			int enemyPoint = countEnemyVirtual(i, neighbors[i], player, 0, board);
			// System.out.println("GameHelper-261 方向:" + i + "num:" + neighbors[i].getNum()
			// + " 敵数:" + enemyPoint );
			if (enemyPoint > 0) {
				// System.out.println("敵石の方向:" + i + "数:" + enemyPoint );
				// その次のマス (index と 色) を取得
				MasuData aNeighbor = neighbors[i]; // board.neighbor(i, neighbors[i]);
				// System.out.println("そのマス:" + nextMasu.getNum() + " 色:" +
				// nextMasu.getColor());
				// その次のマスの色がプレーヤーの色と同じでない間は実行
				while (!aNeighbor.getColor().equals(player)) {
					int index = aNeighbor.getNum();
					// System.out.println("NukGui-409:aNeighbor index:" + index + " player:" +
					// aNeighbor.getColor());
					// masuData にも新しいデータを送る。
					board.get(index).setColor(player);
					// System.out.println("NukGui-414:aNeighbor index:" + index + " player:" +
					// aNeighbor.getColor());
					// index = index + direction[i].getDir();
					// その次のマス (index と 色) を取得
					aNeighbor = board.neighbor(i, aNeighbor);
					// System.out.println("その次のマス:" + nextMasu.getNum() + " 色:" +
					// nextMasu.getColor());
				}
			}
		}
		// return board;
	}

	/**
	 * 挟んだ敵の石を裏返す。
	 * 
	 * @param: int serialNum -- 現在地（通し番号） Color player -- その石を置いたプレーヤー（石の色）
	 * @return: none
	 */
	public void flipOverReal(int serialNum, Color player) {
		// System.out.println("現在:" + serialNum + "player:" + player );
		// GameHelper gameHelper = new GameHelper( board );

		// 通し番号serialNum の地点の隣の地点を調べる
		MasuData[] neighbors = board.neighbors(serialNum);

		// for (int i = 0; i < 8; i++) {
		// System.out.println("NukGui-385: neighbors[" + i + "]=" +
		// neighbors[i].getNum() + "color=" + neighbors[i].getColor());
		// }

		// 八方向すべてについて検討する。
		// i -- 方向を表す
		// 7 | 0 | 1
		// ---+---+---
		// 6 | * | 2
		// ---+---+---
		// 5 | 4 | 3
		//
		for (int i = 0; i < 8; i++) {
			// 各方向について敵石の数をカウントする。
			int enemyPoint = countEnemy(i, neighbors[i], player, 0);
			// System.out.println("方向:" + i + "num:" + neighbors[i].getNum() + " 敵数:" +
			// enemyPoint );
			if (enemyPoint > 0) {
				// System.out.println("敵石の方向:" + i + "数:" + enemyPoint );
				// その次のマス (index と 色) を取得
				MasuData aNeighbor = neighbors[i]; // board.neighbor(i, neighbors[i]);
				// System.out.println("そのマス:" + nextMasu.getNum() + " 色:" +
				// nextMasu.getColor());
				// その次のマスの色がプレーヤーの色と同じでない間は実行
				while (!aNeighbor.getColor().equals(player)) {
					int index = aNeighbor.getNum();
					// System.out.println("NukGui-409:aNeighbor index:" + index + " player:" +
					// aNeighbor.getColor());
					// masuData にも新しいデータを送る。
					board.get(index).setColor(player);
					// System.out.println("NukGui-414:aNeighbor index:" + index + " player:" +
					// aNeighbor.getColor());
					// index = index + direction[i].getDir();
					// その次のマス (index と 色) を取得
					aNeighbor = board.neighbor(i, aNeighbor);
					// System.out.println("その次のマス:" + nextMasu.getNum() + " 色:" +
					// nextMasu.getColor());
				}
			}
		}
	}
}
