package view;
import common.UserCommand;

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
	
	
	//returns a UserCommand ENUM.
	public UserCommand getUserCommand();
	
	//Displays user score
	public void displayScore(int score);

	//will get true if game was lost.
	public void setLose();
	
	//will get true if game was won.
	public void setWin();
	
	//kill a running game thread (used for switching games)
	public void killThread();
	
	//notify the gui that a connection was established
	public void connected(Boolean flag);

}
