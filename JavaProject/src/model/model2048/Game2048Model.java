package model.model2048;



import java.util.ArrayList;
import java.util.Observable;

import model.Model;

import org.eclipse.swt.graphics.Point;

/*
 * TODO (Zeev):
 * TODO: 1. Undo move queue (Singleton Pattern / Object / Static / Private Queue).
 * TODO: 2. Init board
 * TODO: 3. Restart game
 * TODO: 4. Load Game (INI / XML / Other)
 * TODO: 5. Save Game (INI / XML / Other)
 * TODO: 6. Math probability calculations of every new state.
 * NOTE: 2048 Board is N*N
 */



public class Game2048Model extends Observable implements Model,Runnable {
	private int[][] board;
	Point state; //single point on the board
	ArrayList<Point> states; //all the points on the board.
	
	//Constructor
	public Game2048Model() {
		board = new int[4][4];
		states = new ArrayList<Point>();		
	}

	
	@Override
	public void moveUp() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void moveDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[][] getBoard() {
		return board;		
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	

}
