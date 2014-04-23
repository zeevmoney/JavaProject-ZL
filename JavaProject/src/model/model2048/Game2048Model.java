package model.model2048;

import java.util.Observable;
import java.util.Stack;
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
	int lose;
		
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
	public void moveUp() {
		for (int i = 0; i < boardSize; i++) {
			
		}
	}

	@Override
	public void moveDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRight() {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public int[][] getBoard() {
		return currentGame.getBoard();		
	}

	@Override
	public int getScore() {
		return currentGame.getScore();
	}



	
//	//Available space. returns an ArrayList<State> of empty spaces.
//	private ArrayList<Point> availableSpace() {
//		ArrayList<Point> availablePoints = new ArrayList<>();
//		for (int i = 0; i < board.length; i++) {
//			for (int j = 0; j < board[0].length; j++) {
//				if (board[i][j] == 0)
//					availablePoints.add(new Point(i,j));				
//			}			
//		}
//		return availablePoints;
//	}
	
//	//returns true if all states are used. 
//	private boolean isFull() {
//	    return false;
//	 }
	
	
}
