package view;
import common.UserCommand;

/**
 * The Interface View - passive interface that displays data 
 * (the model) and routes user commands (events) to the presenter to act upon that data.
 */

public interface View  {

	/**
	 * Display board - This method draws the GUI based on given data.
	 * The presenter will use this method whenever the data has changed
	 * and the GUI needs to be re-drawn.
	 *
	 * @param data the data
	 */
	public void displayBoard(int[][] data);	
	
	/**
	 * Gets the user command.
	 *
	 * @return UserCommand ENUM.
	 */
	public UserCommand getUserCommand();
	
	/**
	 * Display score.
	 *
	 * @param score the score
	 */
	public void displayScore(int score);

	/**
	 * Sets the lose.
	 */
	public void setLose();
	
	/**
	 * Sets the win.
	 */
	public void setWin();
	
	/**
	 * Kill thread - kill a running game thread (used for switching games)
	 */
	public void killThread();
	
	/**
	 * Connected - notify the gui that a connection was established
	 *
	 * @param flag the flag
	 */
	public void connected(Boolean flag);

}
