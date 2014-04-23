package view;

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
	public void displayData(int[][] data);
	
	/*
	 * Get user input: mouse / keyboard.
	 */
	public int getUserCommand();
	
}
