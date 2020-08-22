// NukGui.java
//
// L.52 一つ一つの masu に addMouseListener しているパターン

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import helper.Board;
import helper.GameHelper;
import model.MasuData;

public class NukGui implements MouseListener, ActionListener {
	private int board_row_size = 260;
	private int board_col_size = 260;
	private int masu_row = 6;	// 盤面の大きさ・行・コンストラクタで変更可能
	private int masu_col = 6;   // 盤面の大きさ・桁・コンストラクタで変更可能
	private int board_masu_num = masu_row * masu_col;
	private final int STONE_SIZE = 40;
	private final int STONE_MARGIN = 3;

	private int ctrlPanel_row_size = 70;
	private int msg_row_size = 22;
	private int btn_row_size = 22;

	private ImageIcon blackIcon, whiteIcon, boardIcon;

	// マス目の情報を1次元の配列でもつ。
	// 他のコードと情報を共有するためのもの。
	// private ArrayList<MasuData> masuData;

	private Board board; 

	// 盤面に表示するマスの配列。
	// 情報として通し番号とそのマスの色を持つ。
	private Masu[] masu;

	// 現在のプレーヤー。
	// 初期設定は白なので、ゲームは黒の手番から始まる。
	private Color player = Color.WHITE;

	// マスの通し番号
	// 現在のマス番号
	private int serialNum = 0;

	// ラベルと文字
	JLabel msg_teban;
	JLabel msg_alert;
	JLabel msg_info;

	/**
	 * コンストラクター 引数なしの場合は、6x6の盤面である。
	 */
	public NukGui() {
	}

	public NukGui(int row, int col) {
		this.masu_row = row;
		this.masu_col = col;
		this.board_masu_num = row * col;
		this.board_row_size = (STONE_SIZE + STONE_MARGIN) * row;
		this.board_col_size = (STONE_SIZE + STONE_MARGIN) * col;
	}

	public Color getPlayer() {
		return this.player;
	}

	public void setPlayer(Color color) {
		this.player = color;
	}

	public int getSerialNum() {
		return this.serialNum;
	}

	public void setSerialNum(int i) {
		this.serialNum = i;
	}

	// 通し番号位置の x座標 -- 1...6
	public int getX(int index) {
		return index % masu_col + 1;
	}

	// 通し番号位置の y座標 -- 1...6
	public int getY(int index) {
		return (int) (index / masu_col + 1);
	}

	// public ArrayList<MasuData> getMasuData () { return this.masuData; }

	public Board getBoard() {
		return board;
	}

	/**
	 * オセロ盤を描く。 ボタンでマスを作成する。 初期設定では GREEN の色で埋める。
	 */
	public void render() {
		whiteIcon = new ImageIcon("pic/White.jpg");
		blackIcon = new ImageIcon("pic/Black.jpg");
		boardIcon = new ImageIcon("pic/GreenFrame.jpg");

		// フレームを作成
		JFrame frame = new JFrame("Othello");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(board_col_size, board_row_size + ctrlPanel_row_size);

		// オセロ盤をパネルで作成
		JPanel guiBoard = new JPanel();
		// ボードのサイズを設定
		guiBoard.setPreferredSize(new Dimension(board_col_size, board_row_size));
		// オセロ盤をグリッドレイアウトにする。
		guiBoard.setLayout(new GridLayout(masu_col, masu_row));

		// オセロ盤の中のマスを1次元配列で作成。
		masu = new Masu[board_masu_num];

		// オセロ盤データを保持する配列を作成。
		// masuData = new ArrayList<MasuData> ();

		// masu[] の要素それぞれに初期データをセットする。
		for (int i = 0; i < board_masu_num; i++) {
			// マスに枠をつける。
			LineBorder border = new LineBorder(Color.GRAY, 2, true);

			// 一つ一つのマスを作成。
			// 各マスは、index情報と color情報を持っている。
			masu[i] = new Masu();
			masu[i].setNum(i);
			// マスの初期colorは、GREEN である。
			masu[i].setColor(Color.GREEN);
			masu[i].setBorder(border);
			// マスの初期画像は boardIcon すなわち GreenFrame.jpg である。
			masu[i].setIcon(boardIcon);
			masu[i].addMouseListener(this);

			guiBoard.add(masu[i]);
		}
		// masu[] のデータを masuData[] にバックアップする
		masuDataBackup();

		// コントロールパネルの作成
		JPanel ctrlPanel = new JPanel();
		// ctrlPanel のサイズを設定
		ctrlPanel.setPreferredSize(new Dimension(board_col_size, ctrlPanel_row_size));
		ctrlPanel.setLayout(new BoxLayout(ctrlPanel, BoxLayout.Y_AXIS));

		// 文字を表示するラベルの作成
		msg_teban = new JLabel();
		msg_alert = new JLabel();
		msg_info = new JLabel();
		msg_teban.setPreferredSize(new Dimension(board_col_size, msg_row_size));
		msg_alert.setPreferredSize(new Dimension(board_col_size, msg_row_size));
		msg_info.setPreferredSize(new Dimension(board_col_size, msg_row_size));

		// ボタンパネルの作成
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(board_col_size, btn_row_size));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		// パスボタン
		JButton pass_btn = new JButton("パス");
		pass_btn.addActionListener(this);

		// 終了ボタン
		JButton end_btn = new JButton("終了");
		end_btn.addActionListener(this);

		buttonPanel.add(pass_btn);
		buttonPanel.add(end_btn);

		ctrlPanel.add(msg_teban);
		ctrlPanel.add(msg_alert);
		ctrlPanel.add(msg_info);
		ctrlPanel.add(buttonPanel);

		// フレームにパネルを貼り付ける。
		JPanel contentPane = (JPanel) frame.getContentPane();
		contentPane.add(guiBoard, BorderLayout.CENTER);
		contentPane.add(ctrlPanel, BorderLayout.SOUTH);

		frame.setVisible(true);
	}

	/**
	 * 盤の中央に石を4つ置く。
	 */
	public void init() {
		move(masu_row / 2, masu_col / 2);
		move(masu_row / 2, masu_col / 2 + 1);
		move(masu_row / 2 + 1, masu_col / 2 + 1);
		move(masu_row / 2 + 1, masu_col / 2);
	}

	/**
	 * masu[] のデータを board にバックアップする。
	 */
	public void masuDataBackup() {
		board = new Board( masu_row, masu_col );
		for (int i = 0; i < board_masu_num; i++) {
			board.get(i).setNum( masu[i].getNum() );
			board.get(i).setColor( masu[i].getColor() );
		}
	}

	/**
	 * 次の手番のプレーヤーを指示する。 常に this.player を参照する。 次のプレーヤーを指示すると同時に this.player を更新する。
	 * 
	 * @param: none
	 * @return: Color nextPlayer
	 */
	public Color nextPlayer() {
		Color nextPlayer;
		String teban_txt;
		if (getPlayer() == Color.WHITE) {
			nextPlayer = Color.BLACK;
			teban_txt = "白の手番です";
		} else {
			nextPlayer = Color.WHITE;
			teban_txt = "黒の手番です";
		}
		msg_teban.setText(teban_txt);
		setPlayer(nextPlayer);
		return nextPlayer;
	}

	/**
	 * 石を置く。 引数に row と col を受け取り、それを index に変換して、 setStone に処理を渡す。
	 * 
	 * @param: int row -- 1...6 (盤面が6x6の場合) int col -- 1...6 ( " )
	 */
	public void move(int row, int col) {
		// 手番を次のプレーヤーにセットする
		Color nextPlayer = nextPlayer();
		int i = board.getIndex(row, col);
		setStone(i, nextPlayer);
	}

	/**
	 * setStone -- 指定したマスに石を置く
	 * 
	 * @param: int i -- 0...board_masu_num Color player -- このメソッドを呼ぶ時点で this.player
	 *             には 手番としてその color がセットされている。
	 * @return: none ただし、this.serialNum の値を更新する。
	 *
	 *          row と col で指定した位置を (0, 0)の位置をゼロとして、 i の連続数で表す。
	 *
	 *             1  2  3  4  5  6 
	 *          +-----------------------
	 *        1 |  0  1  2  3  4  5
	 *        2 |  6  7  8  9 10 11
	 *        3 | 12 13 14 15 16 17
	 *        4 | 18 19 20 21 22 23
	 *        5 | 24 25 26 27 28 29
	 *        6 | 30 31 32 33 34 35
	 */
	public void setStone(int i, Color player) {
		// for DEBUG
		// System.out.println( "setStone: i:" + i );
		// System.out.println( "setStone: player:" + player );

		masu[i].removeAll();    // ます空にする

		ImageIcon icon = (player == Color.WHITE) ? whiteIcon : blackIcon;

		masu[i].setLayout(new GridLayout(1, 1));
		masu[i].setIcon(icon);
		masu[i].setColor(player);
		// board -- ボードじょうほうにもデータを送る。
		board.get(i).setColor(player);
		// this.serialNum を更新する
		setSerialNum(i);
		// System.out.println("i:" + i);
		// 現在地 i を player色にすることで、相手の石を裏返す
		flipOver(i, player);
	}

	/**
	 * 1次元配列 masu の内容一覧 現在のところ未使用
	 */
	public void showAllMasu() {
		for (int i = 0; i < board_masu_num; i++) {
			System.out.println("masu[" + i + "]:" + masu[i].getGraphics());
		}
	}

	/**
	 * row と col を通し番号に変換する
	 * 
	 * @param: int row -- 1...6 int col -- 1...6
	 * @return: int i -- 0...35 (row=6, col=6 の場合)
	 */
	// public int getIndex ( int row, int col ) {
	// int i = masu_col * ( row - 1) + col - 1;
	// return i;
	// }

	/**
	 * this.player に影響を与えずに 仮に次のプレーヤーを設定する。一時利用。
	 * mouseClickdでこのメソッドを使う。 そのマスに石を置けるかどうかを調べるため。
	 */
	public Color tempNextPlayer() {
		Color player = (this.player.equals(Color.BLACK)) ? Color.WHITE : Color.BLACK;
		return player;
	}

	/**
	 * マウスをクリックしたところにあるマスを取得し、 そのマスの index を取得する。
	 * そのマスが GREEN ならまだ石が置かれていないマスなので
	 * 次のプレイヤー(クリックしたプレーヤー)の色の石を置く。
	 */
	public void mouseClicked(MouseEvent e) {
		String alert_txt = "";
		String info_txt = "";
		Masu ms = (Masu) e.getComponent();
		int i = ms.getNum();
		// info_txt = Integer.toString(i) + ":" + ms.getColor().toString();
		// System.out.println("mouse:" + i + " Color:" + ms.getColor() );
		if (ms.getColor().equals(Color.GREEN)) {
			GameHelper gameHelper = new GameHelper( board );
			// そこに石を置けるかどうかを調べるため
			// 仮のプレーヤーを設定する。
			Color tempPlayer = tempNextPlayer();
			// System.out.println("ボード情報:" + i + ":" + board.get(i).getColor() );
			int point = gameHelper.canMove(i, tempPlayer);
			// System.out.println("point:" + point);
			if (point > 0) {
				Color player = nextPlayer();
				// System.out.println("i:" + i + " player:" + player );
				setStone(i, player);
			} else {
				alert_txt = "その場所には石を置けません。";
			}
		} else {
			alert_txt = "そこにはすでに石が置かれています。";
		}
		msg_alert.setText( alert_txt );
		msg_info.setText( info_txt );
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	/**
	 * 挟んだ敵の石を裏返す。
	 * @param:
	 *   int serialNum -- 現在地（通し番号）
	 *   Color player -- その石を置いたプレーヤー（石の色）
	 * @return: none
	 */
	public void flipOver(int serialNum, Color player) {
		// System.out.println("現在:" + serialNum + "player:" + player );
		GameHelper gameHelper = new GameHelper( board );

		// 通し番号serialNum の地点の隣の地点を調べる
		MasuData[] neighbors = board.neighbors( serialNum );

//		 for (int i = 0; i < 8; i++) {
//			 System.out.println("NukGui-385: neighbors[" + i + "]=" + neighbors[i].getNum() + "color=" + neighbors[i].getColor());
//		 }

		// 八方向すべてについて検討する。
		// i -- 方向を表す
		//    7 | 0 | 1
		//   ---+---+---
		//    6 | * | 2
		//   ---+---+---
		//    5 | 4 | 3
		//
		for (int i = 0; i < 8; i++) {
			// 各方向について敵石の数をカウントする。
			int enemyPoint = gameHelper.countEnemy(i, neighbors[i], player, 0);
			// System.out.println("方向:" + i + "num:" + neighbors[i].getNum() + " 敵数:" + enemyPoint );
			if (enemyPoint > 0) {
				// System.out.println("敵石の方向:" + i + "数:" + enemyPoint );
				// その次のマス (index と 色) を取得
				MasuData aNeighbor = neighbors[i]; // board.neighbor(i, neighbors[i]);
				// System.out.println("そのマス:" + nextMasu.getNum() + " 色:" + nextMasu.getColor());
				// その次のマスの色がプレーヤーの色と同じでない間は実行
				while (! aNeighbor.getColor().equals(player)) {
					int index = aNeighbor.getNum();
					// System.out.println("NukGui-409:aNeighbor index:" + index + " player:" + aNeighbor.getColor());
					ImageIcon icon;
					if (player == Color.WHITE) {
						icon = whiteIcon;
					} else {
						icon = blackIcon;
					}
					// masu[i].setLayout( new GridLayout( 1, 1) );
					masu[index].setIcon(icon);
					masu[index].setColor(player);
					// masuData にも新しいデータを送る。
					board.get(index).setColor(player);
					// System.out.println("NukGui-414:aNeighbor index:" + index + " player:" + aNeighbor.getColor());
					// index = index + direction[i].getDir();
					// その次のマス (index と 色) を取得
					aNeighbor = board.neighbor(i, aNeighbor);
					// System.out.println("その次のマス:" + nextMasu.getNum() + " 色:" + nextMasu.getColor());
				}
			}
		}
	}

	/**
	 * パスボタンをクリックしたときの処理 GREEN のマスが残っていれば、手番を次に渡す。 残っていなければ、終了処理。
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("パス")) {
			move_pass();
			// int greenCount = 0;
			// for (int i = 0; i < board_masu_num; i++) {
			// if (masu[i].getColor().equals(Color.GREEN)) {
			// greenCount++;
			// }
			// }
			// if (greenCount > 0) {
			// nextPlayer();
			// } else {
			// gameEnd();
			// }
		}
		if (e.getActionCommand().equals("終了")) {
			gameEnd();
		}
	}

	public void move_pass() {
		int greenCount = 0;
		for (int i = 0; i < board_masu_num; i++) {
			if (masu[i].getColor().equals(Color.GREEN)) {
				greenCount++;
			}
		}
		if (greenCount > 0) {
			nextPlayer();
		} else {
			gameEnd();
		}
	}

	/**
	 * 終了処理
	 */
	public void gameEnd() {
		int blackCount = 0;
		int whiteCount = 0;
		String win_text = "";

		for (int i = 0; i < board_masu_num; i++) {
			if (masu[i].getColor().equals(Color.WHITE)) {
				whiteCount++;
			}
			if (masu[i].getColor().equals(Color.BLACK)) {
				blackCount++;
			}
			if (blackCount > whiteCount) {
				win_text = " 黒の勝ちです。";
			} else if (whiteCount > blackCount) {
				win_text = " 白の勝ちです。";
			} else {
				win_text = " 引き分けです。";
			}
			String count_text = "黒:" + Integer.toString(blackCount) + " 白:" + Integer.toString(whiteCount);
			String alert_txt = count_text + win_text;
			msg_alert.setText(alert_txt);
		}
	}

	/**
	 * 内部クラス Masu
	 */
	class Masu extends JButton {
		private int num;
		private Color color;

		public int getNum() {
			return this.num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public Color getColor() {
			return this.color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		// public void paintComponent( Graphics g) {
		// g.setColor( Color.GREEN );
		// g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		// // g.fillRect( 0, 0, 60, 60 );
		// }
	}

}

// 修正時刻: Sun Aug  9 09:17:52 2020
