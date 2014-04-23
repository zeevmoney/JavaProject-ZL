package model.model2048;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Stack;

import model.Model;
import model.algoirthms.GameState;


/*
 * TODO (Zeev):
 * TODO: 1. Undo move queue (Singleton Pattern / Object / Static / Private Queue).
 * TODO: 2. Math probability calculations of every new state.
 * NOTE: 2048 Board is N*N
 */


public class Game2048Model extends Observable implements Model,Runnable {
	GameState currentGame; //current game state
	Stack<GameState> gameStack; //stack of previous games
	final int mEmpty = 0;
		
	//Constructor
	public Game2048Model(int boardSize) {
		currentGame = new GameState(boardSize);
		gameStack = new Stack<>();
		
		//TODO: new game?
	}
	
	//start a new game and init everything.
	@Override
	public void newGame() {
		boardInit(); //initialize the board
		this.score = 0;
		//TODO: finish this method.
		
	}
	
	//init all values on the game board.
	private void boardInit() {
		for (int i = 0; i < currentGame.getBoardSize(); i++)
			for (int j = 0; j < currentGame.getBoardSize(); j++)
				board[i][j] = mEmpty;
		addNumber(); //add 2 random states (2 or 4)
		addNumber();
	}

	//Adds a state at a random empty spot
	private void addNumber() {
		int x = (int) (Math.random() * currentGame.getBoardSize());
		int y = (int) (Math.random() * currentGame.getBoardSize());
		tempState.setState(new Point(x,y));
		emptyTime.value = Math.random() < 0.9 ? 2 : 4;
		}
	}	
	
	//returns true if 
	private boolean BoardIsEmpty (ArrayList<Point> availablePoints) {
		return pointList.isEmpty();
	}

	//Available space. returns an ArrayList<State> of empty spaces.
	private ArrayList<Point> availableSpace() {
		ArrayList<Point> availablePoints = new ArrayList<>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == 0)
					availablePoints.add(new Point(i,j));				
			}			
		}
		return availablePoints;
	}

	//returns true if all states are used. 
	private boolean isFull() {
	    return availableSpace().size() == 0;
	 }
	
	@Override
	public void moveUp() {
		// TODO Auto-generated method stub		
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
		return board;		
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void restartGame() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void saveGame() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void loadGame() {
		// TODO Auto-generated method stub
		
	}

	

}
