package model.mazegame;

import java.awt.Point;

import common.GameState;
import common.ModelElements;
import common.UserCommand;
import model.AbsModel;

/**
 * The Class MazeGameModel.
 */
public class MazeGameModel extends AbsModel {
	
	/** The current game. */
	GameState currentGame; //current game state
	
	/** The cmd. */
	UserCommand cmd; //user command ENUM
	
	/** The Start. */
	final int Start = ModelElements.MazeStart.getElement();
	
	/** The Wall. */
	final int Wall = ModelElements.MazeWall.getElement();
	
	/** The End. */
	final int End = ModelElements.MazeEnd.getElement();
	
	/** The Empty. */
	final int Empty = ModelElements.MazeEmpty.getElement();
	
	/** The Player. */
	final int Player = ModelElements.MazePlayer.getElement();
	
	/** The Horizontal score. */
	final int HorizontalScore = ModelElements.MazeHorizontalScore.getElement();
	
	/** The Diagonal score. */
	final int DiagonalScore = ModelElements.MazeDiagonalScore.getElement();
	
	/** The rows. */
	final int rows=16;
	
	/** The cols. */
	final int cols=16;
	
	/** The win. */
	boolean win;
	
	/** The lose. */
	boolean lose;
		
	/**
	 * Instantiates a new maze game model.
	 */
	public MazeGameModel() {}
	
	//init all values on the game board & set score to 0.
	/* (non-Javadoc)
	 * @see model.AbsModel#boardInit()
	 */
	public void boardInit() {
		currentGame = new GameState(rows,cols);
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
		

	/**
	 * Move hanlde - main move handling function.
	 * 
	 * @param moveX
	 *            the move x
	 * @param moveY
	 *            the move y
	 * @param cmd
	 *            the cmd
	 */
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
			gameStackPush(currentGame.Copy());
			setChanged();
			notifyObservers();			
	}
	
	/*
	 * "-1,-1"	"-1,0"	"-1,1"
	 * "0,-1"	"0,0"	"0,1"
	 * "1,-1"	"1,0"	"1,1"
	 */	

	/* (non-Javadoc)
	 * @see model.AbsModel#moveUp()
	 */
	@Override
	public void moveUp() {
		moveHanlde(-1,0, UserCommand.Up);
	}

	/* (non-Javadoc)
	 * @see model.AbsModel#moveDown()
	 */
	@Override
	public void moveDown() {
		moveHanlde(1, 0, UserCommand.Down);
	}

	/* (non-Javadoc)
	 * @see model.AbsModel#moveLeft()
	 */
	@Override
	public void moveLeft() {
		moveHanlde(0, -1, UserCommand.Left);
	}
	
	/* (non-Javadoc)
	 * @see model.AbsModel#moveRight()
	 */
	@Override
	public void moveRight() {
		moveHanlde(0, 1, UserCommand.Right);
	}
	
	/* (non-Javadoc)
	 * @see model.AbsModel#UpRight()
	 */
	@Override
	public void UpRight() {
		moveHanlde(-1, 1, UserCommand.UpRight);
	}
	
	/* (non-Javadoc)
	 * @see model.AbsModel#UpLeft()
	 */
	@Override
	public void UpLeft() {
		moveHanlde(-1, -1, UserCommand.UpLeft);
	}


	/* (non-Javadoc)
	 * @see model.AbsModel#DownRight()
	 */
	@Override
	public void DownRight() {
		moveHanlde(1, 1, UserCommand.DownRight);
	}


	/* (non-Javadoc)
	 * @see model.AbsModel#DownLeft()
	 */
	@Override
	public void DownLeft() {
		moveHanlde(1, -1, UserCommand.DownLeft);
	}	


	/* (non-Javadoc)
	 * @see model.AbsModel#getCurrentGame()
	 */
	public GameState getCurrentGame() {
		return currentGame;
	}
	
	/* (non-Javadoc)
	 * @see model.AbsModel#setCurrentGame(common.GameState)
	 */
	@Override
	public void setCurrentGame(GameState game) {
		this.currentGame = game;
	}

	/* (non-Javadoc)
	 * @see model.AbsModel#solveGame(int)
	 */
	@Override
	public void solveGame(int treeDepth) {
		// TODO Auto-generated method stub		
	}







	
		
}



