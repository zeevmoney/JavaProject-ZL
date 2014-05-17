package model.mazegame;


import java.awt.Point;
import java.util.Observable;
import java.util.Stack;

import model.Model;
import model.algoirthms.GameState;
import model.algoirthms.GameStateXML;
import controller.UserCommand;

public class MazeGameModel extends Observable implements Model,Runnable {
	GameState currentGame; //current game state
	Stack<GameState> gameStack; //stack of previous games
	UserCommand cmd; //user command ENUM
	
	//Constants:
	static final int Start = -5;
	static final int Wall = -1;
	static final int End = -3;
	static final int Empty = -4;
	static final int Player = -5;
	static final int HorizontalScore = 10;
	static final int DiagonalScore = 15;
	static final int rows=16;
	static final int cols=16;
	boolean win;
	boolean lose;
	
	
	public MazeGameModel() {
		gameStack = new Stack<>();
		newGame();
	}
	
	//init all values on the game board & set score to 0.
	private void boardInit() {
		win=false;
		lose=false;
		PrimMazeGenerator temp = new PrimMazeGenerator();
		currentGame.setScore(0);
		int [][] bigMaze = temp.generateMazeByPrimAlgo(rows,cols);
		outerloop:
		for (int i = 0; i < rows; i++) { 
			for (int j = 0; j < cols; j++) { 
				if(bigMaze[i][j] == Start) {
					currentGame.setPlayer(new Point (i,j));
					break outerloop;
				}
			}
		}
		currentGame.setBoard(bigMaze);
		setChanged();
		notifyObservers();
	}	
	
	@Override
	public void run() {}

	@Override
	public void newGame() {
		currentGame = new GameState(rows,cols);
		boardInit();
		gameStack.clear();
		gameStack.add(currentGame.Copy());		
		setChanged();
		notifyObservers();
	}

	
	//main move handling function.
	private void moveHanlde(int moveX, int moveY,UserCommand cmd) {
			if (win) { return; } //if won? do nothing.
			//x and y are sent by the move[Direction] functions.
			int score=HorizontalScore; //horizontal score
			int x = currentGame.getPlayer().x + moveX; //new coordinates for movement
			int y = currentGame.getPlayer().y + moveY; //new coordinates for movement
			if (!currentGame.validXY(x, y)) return;  //check if index is valid
			if (currentGame.getXY(x, y) == Wall) return; //check if it's a wall.
			if (Math.abs(moveX) == Math.abs(moveY)) score = DiagonalScore; //if it's a diagonal move
			currentGame.setScore(currentGame.getScore()+score); //update the score
			currentGame.setXY(currentGame.getPlayer().x, currentGame.getPlayer().y, Empty); //set previous cell to empty cell
			if (currentGame.getXY(x, y)==End) {
				win = true;
				setChanged();
				notifyObservers("Win");
				return;
			}
			currentGame.setPlayer(new Point (x,y)); //set new x,y for the player
			currentGame.setXY(x, y, Player);
			gameStack.add(currentGame.Copy());
			setChanged();
			notifyObservers();			
	}
	
	/*
	 * "-1,-1"	"-1,0"	"-1,1"
	 * "0,-1"	"0,0"	"0,1"
	 * "1,-1"	"1,0"	"1,1"
	 */	

	@Override
	public void moveUp() {
		moveHanlde(-1,0, UserCommand.Up);
	}

	@Override
	public void moveDown() {
		moveHanlde(1, 0, UserCommand.Down);
	}

	@Override
	public void moveLeft() {
		moveHanlde(0, -1, UserCommand.Left);
	}
	
	@Override
	public void moveRight() {
		moveHanlde(0, 1, UserCommand.Right);
	}
	
	@Override
	public void UpRight() {
		moveHanlde(-1, 1, UserCommand.UpRight);
	}
	
	@Override
	public void UpLeft() {
		moveHanlde(-1, -1, UserCommand.UpLeft);
	}


	@Override
	public void DownRight() {
		moveHanlde(1, 1, UserCommand.DownRight);
	}


	@Override
	public void DownLeft() {
		moveHanlde(1, -1, UserCommand.DownLeft);
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


	@Override
	public void saveGame(String fileName) {
		try {
			GameStateXML gXML = new GameStateXML();
			currentGame.setGameStack(gameStack); //clone the current game stack
			gXML.gameStateToXML(currentGame,fileName); //save to xml
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadGame(String fileName) {
		try {
			newGame(); //used to eliminate some bugs.
			GameStateXML gXML = new GameStateXML();
			currentGame = gXML.gameStateFromXML(fileName);
			gameStack = currentGame.getGameStack();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void undoMove() {
		if (gameStack.isEmpty()) //in case stack is empty: do nothing.
			return;
		GameState tempState = gameStack.pop();
		if (currentGame.equals(tempState)) { 
			if (!gameStack.isEmpty()) {
				currentGame = gameStack.pop();
			} else { 
				gameStack.push(currentGame.Copy());
				return;
			}
		} else 
			currentGame = tempState;
		
		if (gameStack.isEmpty())
			gameStack.push(currentGame.Copy());

		setChanged();
		notifyObservers();	
	}
	
		
}



