package model;

/*
 * The model is an interface defining the data to be displayed.
 * or otherwise acted upon in the user interface.
 * 
 * Every class concrete that implements model must also extend Observable in order
 * to notify the Presenter that something has changed.   
 */

public interface Model {
	
	/*
	 * Movement functions: will change the state on game board.
	 */
	void moveUp();
	
	void moveDown();
	
	void moveLeft();
	
	void moveRight();	
	
	
	//returns the current 2D array as represented in the Model.
	int[][] getBoard();
	
	//returns the current user score.
	int getScore();
	
}
