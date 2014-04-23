package model.algoirthms;

/* 
 * State is used to describe the current Game state
 * of any game with a 2D array.
*/

public class GameState {
	int[][] board;
	final int boardSize; //final since every board can't be changed while playing.
	int score;
	
	//Constructor: creates a new board with a score of 0.
	public GameState(int boardSize) {
		this.boardSize = boardSize;
		this.board = new int[boardSize][boardSize];		
		this.score = 0;
	}
	
	//Getters & Setters:
	public int[][] getBoard() {
		return board;
	}
	public void setBoard(int[][] board) {
		this.board = board;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getBoardSize() {
		return boardSize;
	}
	
	
	//returns true if the given x and y are valid (not out of the 2D array area)	.	
	public boolean validXY(int x, int y) {
		if (x<0 || y<0 || x>=board.length || y>=board[x].length)
			return false;			
		return true;
	}
	
}
