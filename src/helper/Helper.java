package helper;

public class Helper {

	private static int row = 6;
	private static int col = 6;
	private static int board_masu_num = 36;

	public static boolean isOnBoard(int y, int x) {
		if (y < 1)
			return false;
		if (y > row)
			return false;
		if (x < 1)
			return false;
		if (x > col)
			return false;
		return true;
	}

}
