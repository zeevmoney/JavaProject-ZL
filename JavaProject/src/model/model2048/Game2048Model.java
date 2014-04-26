package model.model2048;

import java.util.Observable;
import java.util.Stack;
import model.Model;
import model.algoirthms.GameState;
import model.algoirthms.GameStateXML;

/*
 * 2048GameModel Class, responsible for all the game logic.
 *  
 * setChanged(): Marks this Observable object as having been changed
 * 
 * notifyObservers(): If this object has changed, as indicated by the hasChanged method, 
 * then notify all of its observers and then call the clearChanged method to indicate 
 * that this object has no longer changed.
 */

public class Game2048Model extends Observable implements Model,Runnable {
	GameState currentGame; //current game state
	Stack<GameState> gameStack; //stack of previous games
	final int emptyCell = 0;
	final int winScore = 2048;
	int boardSize;	
	boolean win;
	boolean lose;
	
		
	//Constructor
	//TODO: make this run as a thread.
	public Game2048Model(int boardSize) {
		gameStack = new Stack<>();
		this.boardSize=boardSize;
		newGame();
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	//init all values on the game board & set score to 0.
	private void boardInit() {
		win=false;
		lose=false;
		currentGame.setScore(0);
		for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++)
				currentGame.setXY(i, j, emptyCell); 
		//add 2 random numbers (2 or 4)
		addNumber(); 
		addNumber();		
	}

	

	//returns false if the board is full, else adds a number (2 or 4) to a random cell.
	//TODO: change to void.
	private boolean addNumber() {
		boolean flag = false;
		while (!flag) {
			//generate 2 random numbers (x and y coordinates)
			int x = (int) (Math.random() * currentGame.getBoardSize());
			int y = (int) (Math.random() * currentGame.getBoardSize());
			if (!currentGame.validXY(x,y)) //if invalid x,y: continue..
				continue;
			if (currentGame.getXY(x,y) == emptyCell) //check if the cell is empty.
			{
				flag = true;
				//probability of 2 is 0.9, 4 is 0.1
				int tempNum = Math.random() < 0.9 ? 2 : 4;
				currentGame.setXY(x, y, tempNum);
			}			
		}
		setChanged();
		notifyObservers();
		return flag;
	}
	
	@Override
	public void newGame() {
		currentGame = new GameState(boardSize);
		boardInit();
		gameStack.clear();
		gameStack.add(currentGame);
		setChanged();
		notifyObservers();	
	}
	
	
	@Override
	public void saveGame() {
		try {
			GameStateXML gXML = new GameStateXML();
			gXML.gameStateToXML(currentGame, "2048Save.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadGame() {
		try {
			GameStateXML gXML = new GameStateXML();
			gXML.gameStateToXML(currentGame, "2048Save.xml");			
		} catch (Exception e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void undoMove() {
		currentGame=gameStack.pop();
		setChanged();
		notifyObservers();				
	}
	
	
	@Override
	public int[][] getBoard() {
		return currentGame.getBoard();		
	}

	@Override
	public int getScore() {
		return currentGame.getScore();
	}
	
	@Override
	public boolean getWin() {
		return win;
	}

	@Override
	public boolean getLose() {
		return lose;
	}

	//returns True if any movement is available.
	public boolean canMove () {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (!currentGame.validXY(i+1, j) || !currentGame.validXY(i, j+1)) //invalid index 
					continue;					
				if (currentGame.getXY(i, j) == emptyCell || currentGame.getXY(i, j) == currentGame.getXY(i+1, j) 
					|| currentGame.getXY(i, j) == currentGame.getXY(i, j+1)) {
					//empty / right cell equals / bottom cell equals
					return true;
				}
			}
		}
		return false;		
	}

	
	private void moveHanlde(boolean change) {
		if (change) { //if there was a change it means that there is an empty space.
			setChanged(); //changed in any case
			if (win) {
				notifyObservers("Win");
				return;
			}
			addNumber();
			gameStack.add(currentGame.Copy());			
			notifyObservers();	
		} else if (!change && !canMove()) { //no change & can't move = lost the game.
			lose = true;
			setChanged();
			notifyObservers("Lose");
		}		
	}
	
	
	
	/* *******************
	 * Move up + MoveallUp
	 * ******************* */
	
	@Override
	public void moveUp() {
		boolean change = moveAllUp();
		moveHanlde(change);
	}
	



	private boolean moveAllUp() {
		boolean movement=false; //flag to check if there was any movement
		boolean merge=false; //flag to check if there was a merge
		int score = currentGame.getScore();
		int[][] board = currentGame.getBoard();
		//2,2,2,2 / 2,2,0,2
		//remove all spaces (consolidate everything)
		for (int i = 0; i < boardSize-1; i++) { //i+1 = out of the array
			for (int j = 0; j < boardSize-1; j++) {
				if (board[i][j] == emptyCell) {
					board[i][j] = board[i+1][j];
					board [i+1][j] = emptyCell;
					movement = true;
				}						
			}			
		}
		//scan for all equal cells
		for (int i = 0; i < boardSize-1; i++) { //i+1 = out of the array
			for (int j = 0; j < boardSize-1; j++) {
				if (board[i][j] == board[i+1][j]) {
					score += board[i][j]*2; //add the value to the score
					board[i][j] *=2; //double the current cell value
					if (board[i][j] == winScore)
						win = true;
					board [i+1][j] = emptyCell; //set the lower cell to 0
					merge = true;
				}						
			}			
		}
		if (merge) { //if there was any merge
			//remove all spaces (consolidate everything again) (cause of 2,2,2,0 -> 4,0,2,0 for example)
			for (int i = 0; i < boardSize-1; i++) { //i+1 = out of the array
				for (int j = 0; j < boardSize-1; j++) {
					if (board[i][j] == emptyCell) {
						board[i][j] = board[i+1][j];
						board [i+1][j] = emptyCell;
						movement = true;
					}						
				}			
			}
		}
		currentGame.setBoard(board);
		currentGame.setScore(score);
		if (merge || movement) {
			setChanged();
			notifyObservers();
		}
		return (merge || movement); //returns true if there was a merge or any movement
	}
	
	/* **********************
	 * MoveDown + MoveallDown
	 * ********************** */

	@Override
	public void moveDown() {
		boolean change = moveAllDown();
		moveHanlde(change);
	}
	
	private boolean moveAllDown() {
		boolean movement=false; //flag to check if there was any movement
		boolean merge=false; //flag to check if there was a merge
		int score = currentGame.getScore();
		int[][] board = currentGame.getBoard();
		//2,2,2,2 / 2,2,0,2
		//remove all spaces (consolidate everything)
		for (int i = boardSize-1; i > 0; i--) { //i-1 = out of the array
			for (int j = 0; j < boardSize-1; j++) {
				if (board[i][j] == emptyCell) {
					board[i][j] = board[i-1][j];
					board [i-1][j] = emptyCell;
					movement = true;
				}						
			}			
		}
		//scan for all equal cells
		for (int i = boardSize-1; i > 0; i--) { //i-1 = out of the array
			for (int j = 0; j < boardSize-1; j++) {
				if (board[i][j] == board[i-1][j]) {
					score += board[i][j]*2; //add the value to the score
					board[i][j] *=2; //double the current cell value
					if (board[i][j] == winScore)
						win = true;					
					board [i-1][j] = emptyCell; //set the lower cell to 0
					merge = true;
				}						
			}			
		}
		if (merge) { //if there was any merge
			//remove all spaces (consolidate everything again) (cause of 2,2,2,0 -> 4,0,2,0 for example)
			for (int i = boardSize-1; i > 0; i--) { //i-1 = out of the array
				for (int j = 0; j < boardSize-1; j++) {
					if (board[i][j] == emptyCell) {
						board[i][j] = board[i-1][j];
						board [i-1][j] = emptyCell;
						movement = true;
					}						
				}			
			}
		}
		currentGame.setBoard(board);
		currentGame.setScore(score);
		if (merge || movement) {
			setChanged();
			notifyObservers();
		}
		return (merge || movement); //returns true if there was a merge or any movement		
	}
	
	/* **********************
	 * MoveLeft + MoveallLeft
	 * ********************** */

	@Override
	public void moveLeft() {
		boolean change = moveAllLeft();
		moveHanlde(change);
	}

	private boolean moveAllLeft() {
		boolean movement=false; //flag to check if there was any movement
		boolean merge=false; //flag to check if there was a merge
		int score = currentGame.getScore();
		int[][] board = currentGame.getBoard();
		//2,2,2,2 / 2,2,0,2
		//remove all spaces (consolidate everything)
		for (int i = 0; i < boardSize-1; i++) { 
			for (int j = 0; j < boardSize-1; j++) {
				if (board[i][j] == emptyCell) {
					board[i][j] = board[i][j+1];
					board [i][j+1] = emptyCell;
					movement = true;
				}						
			}			
		}
		//scan for all equal cells
		for (int i = 0; i < boardSize-1; i++) { 
			for (int j = 0; j < boardSize-1; j++) {
				if (board[i][j] == board[i][j+1]) {
					score += board[i][j]*2; //add the value to the score
					board[i][j] *=2; //double the current cell value
					if (board[i][j] == winScore)
						win = true;
					board [i][j+1] = emptyCell; //set the lower cell to 0
					merge = true;
				}						
			}			
		}
		if (merge) { //if there was any merge
			//remove all spaces (consolidate everything again) (cause of 2,2,2,0 -> 4,0,2,0 for example)
			for (int i = 0; i < boardSize-1; i++) { 
				for (int j = 0; j < boardSize-1; j++) {
					if (board[i][j] == emptyCell) {
						board[i][j] = board[i][j+1];
						board [i][j+1] = emptyCell;
						movement = true;
					}						
				}			
			}
		}
		currentGame.setBoard(board);
		currentGame.setScore(score);
		if (merge || movement) {
			setChanged();
			notifyObservers();
		}
		return (merge || movement); //returns true if there was a merge or any movement		
	}

	/* *************************
	 * MoveRight + MoveallRight
	 * ************************* */
	
	@Override
	public void moveRight() {
		boolean change = moveAllRight();
		moveHanlde(change);			
	}
	
	private boolean moveAllRight() {
		boolean movement=false; //flag to check if there was any movement
		boolean merge=false; //flag to check if there was a merge
		int score = currentGame.getScore();
		int[][] board = currentGame.getBoard();
		//2,2,2,2 / 2,2,0,2
		//remove all spaces (consolidate everything)
		for (int i = 0; i < boardSize-1; i++) { 
			for (int j = boardSize-1; j > 0; j--) {
				if (board[i][j] == emptyCell) {
					board[i][j] = board[i][j-1];
					board [i][j-1] = emptyCell;
					movement = true;
				}						
			}			
		}
		//scan for all equal cells
		for (int i = 0; i < boardSize-1; i++) { 
			for (int j = boardSize-1; j > 0; j--) {
				if (board[i][j] == board[i][j-1]) {
					score += board[i][j]*2; //add the value to the score
					board[i][j] *=2; //double the current cell value
					if (board[i][j] == winScore)
						win = true;
					board [i][j-1] = emptyCell; //set the lower cell to 0
					merge = true;
				}						
			}			
		}
		if (merge) { //if there was any merge
			//remove all spaces (consolidate everything again) (cause of 2,2,2,0 -> 4,0,2,0 for example)
			for (int i = 0; i < boardSize-1; i++) { 
				for (int j = boardSize-1; j > 0; j--) {
					if (board[i][j] == emptyCell) {
						board[i][j] = board[i][j-1];
						board [i][j-1] = emptyCell;
						movement = true;
					}						
				}			
			}
		}
		currentGame.setBoard(board);
		currentGame.setScore(score);
		if (merge || movement) {
			setChanged();
			notifyObservers();
		}
		return (merge || movement); //returns true if there was a merge or any movement		
		
	}


	
	


}
