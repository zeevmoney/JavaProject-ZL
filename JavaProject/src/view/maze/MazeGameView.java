package view.maze;

import java.util.Observable;

import controller.UserCommand;
import view.View;



/*
 * TODO: (Lital)
 * Don't forget to add:
 * setChanged & notifyObservers
 */

public class MazeGameView extends Observable implements View, Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayBoard(int[][] data) {
		// TODO Auto-generated method stub

	}




	@Override
	public void displayScore(int score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLose(boolean lose) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWin(boolean win) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserCommand getUserCommand() {
		// TODO Auto-generated method stub
		return null;
	}

}
