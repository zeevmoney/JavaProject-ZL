package model.algoirthms;

import java.awt.Point;

/* 
 * GameState is used to describe the current Game state
 * of any game with a 2D array.
*/

public class GameState {
	int[][] board; //the game board
	int rows; //board rows
	int cols; //board columns
	int score; //current game score
	Point player;
	Point start;
	Point end;
	
	
		
	//Constructor: creates a new board with a score of 0.
	public GameState(int rows,int cols) {
		this.rows = rows;
		this.cols = cols;
		this.board = new int[rows][cols];		
		this.score = 0;
		this.player = new Point();
	}
	
	public GameState() {
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
	public int getBoardRows() {
		return rows;
	}
	
	public int getBoardCols() {
		return cols;
	}
	
	public Point getPlayer() {
		return player;
	}

	public void setPlayer(Point player) {
		this.player = player;
	}
	
	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}
	
	//sets an [x][y] coordinate with <value>
	public void setXY (int x, int y, int value) {
		board[x][y] = value;
	}
	
	//returns the value at a given x,y
	public int getXY(int x,int y) {
		return board[x][y];	
	}
	
	//returns true if the given x and y are valid (not out of the 2D array area)	.	
	public boolean validXY(int x, int y) {
		if (x<0 || y<0 || x>=board.length || y>=board[x].length)
			return false;			
		return true;
	}
	
	//GameState object copy
	public GameState Copy() {
		GameState tempState = new GameState();
		int[][] board = this.board.clone();
		tempState.setBoard(board);
		tempState.setScore(this.score);
		return tempState;		
	}







	


	
	
}
