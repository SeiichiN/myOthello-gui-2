package test;

import java.awt.Color;
import java.util.ArrayList;

import model.MasuData;
import helper.Board;
import helper.GameHelper;

public class GameHelperTest {
	private static int row = 8;
	private static int col = 8;
	
	public GameHelperTest () {}
	
	public static void main (String [] args) {
		
		Board board = new Board( row, col );
		
		init( board );
		
		GameHelper gameHelper = new GameHelper( board );
		
		int point = gameHelper.enemySelectMove( 39, board, Color.BLACK );
		System.out.println("point: " + point );
		board.printAll();
		
	}
	
	public static void init (Board board) {
		
		ArrayList <MasuData> arrayMasuData = new ArrayList <> ();
		
		for (int i = 0; i < 64; i++) {
			MasuData masuData = new MasuData( row, col );
			masuData.setNum( i );
			masuData.setColor( Color.GREEN );
			arrayMasuData.add( masuData );
		}
		
		int initA = 27;
		int initB = 28;
		int initC = 35;
		int initD = 36;
		
		arrayMasuData.get( initA ).setColor( Color.BLACK );
		arrayMasuData.get( initB ).setColor( Color.white );
		arrayMasuData.get( initC ).setColor( Color.BLACK );
		arrayMasuData.get( initD ).setColor( Color.white );
		
		board.setArrayMasuData( arrayMasuData );
	}
	
}
