package model;

/*
 * The model is an interface defining the data to be displayed.
 * or otherwise acted upon in the user interface.
 * 
 * Every class concrete that implements model must also extend Observable in order
 * to notify the Presenter that something has changed.   
 */

public interface Model {
	
	//Game control:
	public void newGame (); 
	public void restartGame();
	public void saveGame(); //Save Game (INI / XML / Other)
	public void loadGame(); //Load Game (INI / XML / Other)
	
	//Movement functions: will change the state on game board.
	public void moveUp();
	public void moveDown();
	public void moveLeft();
	public void moveRight();	
	
	//returns the current 2D array as represented in the Model.
	public int[][] getBoard();
	
	//returns the current user score.
	public int getScore();
	
}
