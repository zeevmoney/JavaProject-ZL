package model.model2048;

import java.util.ArrayList;
import java.util.Observable;
import model.Model;
import model.algoirthms.State;


/*
 * TODO (Zeev):
 * TODO: 1. Undo move queue (Singleton Pattern / Object / Static / Private Queue).
 * TODO: 2. Math probability calculations of every new state.
 * NOTE: 2048 Board is N*N
 */


public class Game2048Model extends Observable implements Model,Runnable {
	private int[][] board;
	State state; //single point(state) on the board
	ArrayList<State> states; //all the States on the board.
	
	//Constructor
	public Game2048Model() {
		board = new int[4][4];
		states = new ArrayList<>();		
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


	@Override
	public void newGame() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void restartGame() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void saveGame() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void loadGame() {
		// TODO Auto-generated method stub
		
	}

	

}
