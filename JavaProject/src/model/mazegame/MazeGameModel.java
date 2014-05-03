package model.mazegame;

//http://www.jonathanzong.com/blog/2012/11/06/maze-generation-with-prims-algorithm

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
	private final int Start = 1;
	private final int Wall = -1;
	private final int End = 2;
	private final int Empty = 0;
	private final int Player = 1;
	private final int HorizontalScore = 10;
	private final int DiagonalScore = 15;
	private final int rows=10;
	private final int cols=10;
	boolean win;
	boolean lose;
	
	
	public MazeGameModel() {
		gameStack = new Stack<>();
		newGame();
	}
	
	
	//init all values on the game board & set score to 0.
	//TODO: (Zeev): random generated maze.
	private void boardInit() {
		win=false;
		lose=false;
		PrimMazeGenerator temp = new PrimMazeGenerator();
		currentGame.setScore(0);
		int [][] bigMaze = temp.generateMazeByPrimAlgo(rows,cols);
		for (int i = 0; i < rows; i++) 
			for (int j = 0; j < cols; j++) { 
				if(bigMaze[i][j] == Start) 
					currentGame.setPlayer(new Point (i,j));
			}
			
		currentGame.setBoard(bigMaze);
		setChanged();
		notifyObservers();
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

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
			//x and y are sent by the move[Direction] functions.
			int score=HorizontalScore; //horizontal score
			int x = currentGame.getPlayer().x + moveX; //new coordinates for movement
			int y = currentGame.getPlayer().y + moveY; //new coordinates for movement
			if (!currentGame.validXY(x, y)) return;  //check if index is valid
			if (currentGame.getXY(x, y) == -1) return; //check if it's a wall.
			if (Math.abs(moveX) == Math.abs(moveY)) score = DiagonalScore; //if it's a diagonal move
			currentGame.setScore(currentGame.getScore()+score); //update the score
			currentGame.setXY(currentGame.getPlayer().x, currentGame.getPlayer().y, Empty); //set previoues cell to empty cell
			if (currentGame.getXY(x, y)==End) {
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
	public void saveGame() {
		try {
			GameStateXML gXML = new GameStateXML();
			gXML.gameStateToXML(currentGame, "MazeSave.xml",gameStack,"MazeGameStack.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadGame() {
		try {
			newGame();
			GameStateXML gXML = new GameStateXML();
			currentGame = gXML.gameStateFromXML("MazeSave.xml");
			gameStack = gXML.gameStackFromXML("MazeGameStack.xml");
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
				//if (gameStack.size() == 0)
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

//for (int i = 0; i < rows; i++) {
//for (int j = 0; j < cols; j++) {
//	if (i==0 && j==0) {
//		bigMaze[i][j] = End;
//		currentGame.setEnd(new Point (i,j));
//	} else if (i==rows-1 && j==cols-1) {
//		bigMaze[i][j] = Start;
//		currentGame.setStart(new Point (i,j));
//		currentGame.setPlayer(new Point (i,j));
//	} else if (i==1 && (j>0 && j<cols-1) || i>0 && j==1)
//		bigMaze[i][j] = Wall;	
//	else bigMaze[i][j] = Empty;
//}
//}



