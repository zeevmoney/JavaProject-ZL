package model;

/*
 * The model is an interface defining the data to be displayed.
 * or otherwise acted upon in the user interface.
 */

public interface Model {
	
	/*
	 * Movement functions: will change the state on game board.
	 */
	void moveUp();
	
	void moveDown();
	
	void moveLeft();
	
	void moveRight();	
	
	//
	
	int[][] getData();
	
	int getScore();
	
}
