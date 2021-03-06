package model;

/**
 * The Interface Model.
 * 
 * The model is an interface defining the data to be displayed.
 * or otherwise acted upon in the user interface.
 * 
 * Every concrete class that implements model must also extend Observable in order
 * to notify the Presenter that something has changed.   
 *
 */
public interface Model {
	
	/**
	 * New game.
	 */
	public void newGame();
	
	/**
	 * Save game.
	 *
	 * @param fileName the file name
	 */
	public void saveGame(String fileName); //Save Game (XML)
	
	/**
	 * Load game.
	 *
	 * @param fileName the file name
	 */
	public void loadGame(String fileName); //Load Game (XML)
	
	/**
	 * Undo move.
	 */
	public void undoMove();
	
	/**
	 * Connect to server.
	 *
	 * @param ip the ip
	 * @param port the port
	 */
	public void connectToServer(String ip,int port);
	
	/**
	 * Disconnect from server.
	 */
	public void disconnectFromServer();
	
	/**
	 * Gets the hint.
	 *
	 * @param hintsNum the hints num
	 * @param treeDepth the MiniMax tree depth
	 * @return the hint
	 */
	public void getHint(int hintsNum, int treeDepth);
	
	/**
	 * Solve game -solve whole the game
	 *
	 * @param treeDepth the tree depth
	 */
	public void solveGame(int treeDepth); 
	
	//Movement functions: will change the state on game board.
	
	/**
	 * Move up.
	 */
	public void moveUp();
	
	/**
	 * Move down.
	 */
	public void moveDown();
	
	/**
	 * Move left.
	 */
	public void moveLeft();
	
	/**
	 * Move right.
	 */
	public void moveRight();
	
	/**
	 * Up right.
	 */
	public void UpRight();
	
	/**
	 * Up left.
	 */
	public void UpLeft();
	
	/**
	 * Down right.
	 */
	public void DownRight();
	
	/**
	 * Down left.
	 */
	public void DownLeft();
	
	/**
	 * Gets the board.
	 *
	 * @return the current 2D array as represented in the Model.
	 */
	public int[][] getBoard();
	
	/**
	 * Gets the score.
	 *
	 * @return the current user score.
	 */
	public int getScore();


}
