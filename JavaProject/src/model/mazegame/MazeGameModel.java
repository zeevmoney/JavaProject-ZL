package model.mazegame;

import java.awt.Point;

import common.GameState;
import common.ModelElements;
import common.UserCommand;
import model.AbsModel;

public class MazeGameModel extends AbsModel {
	GameState currentGame; //current game state
	UserCommand cmd; //user command ENUM
	
	final int Start = ModelElements.MazeStart.getElement();
	final int Wall = ModelElements.MazeWall.getElement();
	final int End = ModelElements.MazeEnd.getElement();
	final int Empty = ModelElements.MazeEmpty.getElement();
	final int Player = ModelElements.MazePlayer.getElement();
	final int HorizontalScore = ModelElements.MazeHorizontalScore.getElement();
	final int DiagonalScore = ModelElements.MazeDiagonalScore.getElement();
	final int rows=16;
	final int cols=16;
	boolean win;
	boolean lose;
		
	public MazeGameModel() {}
	
	//init all values on the game board & set score to 0.
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
			gameStackPush(currentGame.Copy());
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


	public GameState getCurrentGame() {
		return currentGame;
	}
	
	@Override
	public void setCurrentGame(GameState game) {
		this.currentGame = game;
	}

	@Override
	public void solveGame(int treeDepth) {
		// TODO Auto-generated method stub		
	}







	
		
}



