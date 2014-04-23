package model.model2048;

import java.util.Observable;
import java.util.Stack;

import controller.Presenter;
import model.Model;
import model.algoirthms.GameState;
import model.algoirthms.GameStateXML;


/*
 * TODO (Zeev):
 * TODO: 1. Undo move queue (Singleton Pattern / Object / Static / Private Queue).
 * NOTE: 2048 Board is N*N
 */


public class Game2048Model extends Observable implements Model,Runnable {
	GameState currentGame; //current game state
	Stack<GameState> gameStack; //stack of previous games
	final int mEmpty = 0;
	int boardSize;	
	int win;
	int lose; //TODO: add to gameover
		
	//Constructor
	//TODO: make this run as a thread.
	public Game2048Model(int boardSize) {
		gameStack = new Stack<>();
		this.boardSize=boardSize;
		restartGame();
	}
	

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	//init all values on the game board & set score to 0.
	private void boardInit() {
		currentGame.setScore(0);
		for (int i = 0; i < currentGame.getBoardSize(); i++)
			for (int j = 0; j < currentGame.getBoardSize(); j++)
				currentGame.setXY(i, j, mEmpty); //set [x][y] = empty cell
		//add 2 random numbers (2 or 4)
		addNumber(); 
		addNumber();		
	}

	
	//TODO: check if the board is full
	//Adds a state at a random empty spot with 2(0.9) or 4(0.1)
	private void addNumber() {
		boolean flag = false;
		while (!flag) {
			//generate 2 random numbers (x and y coordinates)
			int x = (int) (Math.random() * currentGame.getBoardSize());
			int y = (int) (Math.random() * currentGame.getBoardSize());
			if (!currentGame.validXY(x,y)) //if invalid x,y: continue..
				continue;
			if (currentGame.getXY(x,y) == mEmpty) //check if the cell is empty.
			{
				flag = true;
				//probability of 2 is 0.9, 4 is 0.1
				int tempNum = Math.random() < 0.9 ? 2 : 4;
				currentGame.setXY(x, y, tempNum);
			}			
		}
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void restartGame() {
		win=0;
		lose=0;
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
	
	private void gameOver() {
		// TODO Auto-generated method stub
		
	}

	//returns True is the board is full
	public boolean boardIsFull () {
		int[][] board = currentGame.getBoard();
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				if (board[i][j]==0)
					return true;
		return false;		
	}

	
	
	/* ******************
	 * Move up + MoveallUp
	 * ****************** */
	
	//TODO: check if there was any movement at all
	@Override
	public void moveUp() {
		moveAllup();
		if (boardIsFull()) {
			gameOver();
			return;
		}		
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
					addNumber();					
			}									
		}
		setChanged();
		notifyObservers();
	}
	
	private void moveAllup() {
		int score = currentGame.getScore();
		int[][] board = currentGame.getBoard();
		for (int i = 1; i < boardSize; i++) { //first row remains unchanged (i=0)
			for (int j = 0; j < boardSize; j++) {
				/*
				 * if upper cell is not empty & has the same value
				 * add the lower cell value to the upper cell (on the same column) 
				 */
				if (board[i-1][j] != mEmpty && board[i-1][j] == board[i][j])
				{
					score += board[i][j]*2;
					board[i-1][j] *=2;
				/*
				 * if the upper cell is empty add the lower cell value and change the lower to empty.
				 * (if the upper cell has value and it doesn't match the lower cell don't need to do anything).
				 */
				} else if (board[i-1][j] == mEmpty) {
					board[i-1][j] = board[i][j];
					board[i][j] = mEmpty; //init the cell the was merged with the upper cell
				}				
			}			
		}
		currentGame.setBoard(board);
		currentGame.setScore(score);
	}
	
	
	/* **********************
	 * MoveDown + MoveallDown
	 * ********************** */

	@Override
	public void moveDown() {
		// TODO Auto-generated method stub
		
	}
	
	public void moveAllDown() {
		
	}
	
	/* **********************
	 * MoveLeft + MoveallLeft
	 * ********************** */

	@Override
	public void moveLeft() {

	}
	
	public void moveAllLeft () {
	}

	
	
	
	/* *************************
	 * MoveRight + MoveallRight
	 * ************************* */
	
	@Override
	public void moveRight() {
		
	}
	
	public void moveAllRight() {
		
	}

}
