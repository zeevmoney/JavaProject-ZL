package view;

/*
 * The view is a passive interface that displays data 
 * (the model) and routes user commands (events) to the presenter to act upon that data.
 */

public interface View  {
	
	public void displayData(int[][] data);
	public int getUserCommand();
	
}
