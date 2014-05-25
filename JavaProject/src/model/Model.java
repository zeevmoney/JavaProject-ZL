package model;


/*
 * The model is an interface defining the data to be displayed.
 * or otherwise acted upon in the user interface.
 * 
 * Every concrete class that implements model must also extend Observable in order
 * to notify the Presenter that something has changed.   
 */

public interface Model {
	
	//Game control:
	public void newGame();
	public void saveGame(String fileName); //Save Game (XML)
	public void loadGame(String fileName); //Load Game (XML)
	public void undoMove();
	public void connectToServer(String ip,int port);
	public void disconnectFromServer();
	public void getHint(int hintsNum, int treeDepth); //get hint (treeSize = minimax treeSize)
	public void solveGame(int treeDepth); //solve the game
	
	//Movement functions: will change the state on game board.
	public void moveUp();
	public void moveDown();
	public void moveLeft();
	public void moveRight();
	public void UpRight();
	public void UpLeft();
	public void DownRight();
	public void DownLeft();
	
	//returns the current 2D array as represented in the Model.
	public int[][] getBoard();
	
	//returns the current user score.
	public int getScore();
	
	


}
