package view;

import controller.UserCommand;

/*
 * The view is a passive interface that displays data 
 * (the model) and routes user commands (events) to the presenter to act upon that data.
 */


public interface View  {
	
	/*
	 * This method draws the GUI based on given data.
	 * The presenter will use this method whenever the data has changed
	 * and the GUI needs to be re-drawn.
	 */
	public void displayBoard(int[][] data);	
	
	/*
	 * Returns an INT which represents the user input
	 * every INT represents an action.
	 * for example: 
	 * 2 = move straight down
	 * 1 = move left & down (diagonal)
	 */	
	
	//TODO: (Lital) Use UserCommand ENUM
	public UserCommand getUserCommand();
	
	//Displays user score
	public void displayScore(int score);

	//will get true if game was lost.
	public void setLose(boolean lose);
	
	//will get true if game was won.
	public void setWin(boolean win);
	
}
