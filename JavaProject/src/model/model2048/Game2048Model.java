package model.model2048;

import java.io.IOException;

import model.AbsModel;
import common.GameState;
import common.ModelElements;
import common.SolveMsg;
import controller.UserCommand;



/*
 * 2048GameModel Class, responsible for all the game logic.
 *  
 * setChanged(): Marks this Observable object as having been changed
 * 
 * notifyObservers(): If this object has changed, as indicated by the hasChanged method, 
 * then notify all of its observers and then call the clearChanged method to indicate 
 * that this object has no longer changed.
 */


//TODO: in case lost the game: when clicking no make sure lost is set to false and undo 1 move.

public class Game2048Model extends AbsModel {
	GameState currentGame; //current game state
	final int winScore;
	final int boardSize;
	int emptyCell = ModelElements.Game2048Empty.getElement();
	boolean win;
	boolean lose;
	boolean won; //to make sure the game was only won once.
	static final String gameName = new String("2048");
	
		
	//Constructor
	public Game2048Model(int boardSize,int winScore) {
		this.winScore = winScore;
		this.boardSize=boardSize;
		newGame();
	}
	
	
	//init all values on the game board & set score to 0.
	public void boardInit() {
		currentGame = new GameState(this.boardSize,this.boardSize);
		win=false;
		lose=false;
		won=false;
		currentGame.setScore(0);
		for (int i = 0; i < boardSize; i++)
			for (int j = 0; j < boardSize; j++)
				currentGame.setXY(i, j, emptyCell); 
		//add 2 random numbers (2 or 4)
		addNumber(); 
		addNumber();
		setChanged();
		notifyObservers();
	}

	

	//adds a number (2 or 4) to a random cell.
	private void addNumber() {
		boolean flag = false;
		while (!flag) {
			//generate 2 random numbers (x and y coordinates)
			int x = (int) (Math.random() * boardSize);
			int y = (int) (Math.random() * boardSize);
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
	}	
	
	//returns True if any movement is available.	
	private boolean canMove () {
		//check if bottom cell equals 
		for (int i = 0; i < boardSize-1; i++) 
			for (int j = 0; j < boardSize; j++)
				if (currentGame.getXY(i, j) == currentGame.getXY(i+1, j)) 
					return true;		
		
		//check if right cell equals
		for (int i = 0; i < boardSize-1; i++) 
			for (int j = 0; j < boardSize-1; j++)
				if (currentGame.getXY(i, j) == currentGame.getXY(i, j+1)) 
					return true;		
		return false;		
	}
	
	//returns true if board is full
	private boolean boardIsFull() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (currentGame.getXY(i, j) == emptyCell) 
					return false;				
			}
		}
		return true;
	}
	
	//checks if the game was won (the game will continue after 2048 was reached)
	private void moveHanlde(boolean change) {
		if (change) { //if there was a change it means that there is an empty space.
			if (win && !won) { //if 2048 was reached
				won = true; //can't win twice (win = 2048, when you continue, you can't win again).
				setChanged();
				notifyObservers("Win");				
			} else {
				addNumber();
				GameStackPush(currentGame.Copy());
				setChanged();
				notifyObservers();
			}
		} else if (!change && !canMove() && boardIsFull()) { //no change & can't move & board is full = lost the game.
			if (lose) return; //can't lose twice :)
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
		int x=0;
		boolean merge = false; //flag to check if there was a merge
		boolean movement = false; //flag to check if there was any movement
		//remove all spaces (consolidate everything)
		while (x<boardSize-1) {
			x++;
			if (!movement) //if there was no movement then update movement boolean
				movement = moveAllUp(); 
			else //if there was no movement then there is no need to generate a random number.				
				moveAllUp();			
		}		
		//scan for all equal cells
		for (int i = 0; i < boardSize-1; i++) { //i+1 = out of the array
			for (int j = 0; j < boardSize; j++) {
				if (currentGame.getXY(i, j) == currentGame.getXY(i+1, j) && currentGame.getXY(i, j) != emptyCell) { //if cells are equal
					currentGame.setScore(currentGame.getScore() + currentGame.getXY(i,j) * 2); //add the value to the score
					currentGame.setXY(i, j, currentGame.getXY(i, j) * 2); //double the current cell value
					if (currentGame.getXY(i, j) == winScore) 
						win = true;
					currentGame.setXY(i+1,j,emptyCell); //set the lower cell to 0
					merge = true;
				}						
			}			
		}		
		moveAllUp(); //remove all spaces (consolidate everything again) => 2,2,2,2 / 2,2,0,2 OR 2,2,2,0 -> 4,0,2,0 
		boolean change = (movement || merge);		
		moveHanlde(change);
	}
	
	
	//move everything up
	private boolean moveAllUp() {
		boolean movement=false; //flag to check if there was any movement
		for (int i = 0; i < boardSize-1; i++) { //i+1 = out of the array
			for (int j = 0; j < boardSize; j++) {
				if (currentGame.getXY(i, j) == emptyCell) {
					if (currentGame.getXY(i+1,j) != emptyCell) 
						//if a number which is not an empty cell was moved
						movement = true;
					currentGame.setXY(i, j, currentGame.getXY(i+1, j));
					currentGame.setXY(i+1, j, emptyCell);					
				}						
			}			
		}
		if (movement) {
			setChanged();
			notifyObservers();
		}
		return (movement); //returns true if there was a movement
	}
	
	/* **********************
	 * MoveDown + MoveallDown
	 * ********************** */

	@Override
	public void moveDown() {
		int x=0;
		boolean merge = false; //flag to check if there was a merge
		boolean movement = false; //flag to check if there was any movement
		//remove all spaces (consolidate everything)
		while (x<boardSize-1) {
			x++;
			if (!movement) //if there was no movement then update movement boolean
				movement = moveAllDown(); 
			else //if there was no movement then there is no need to generate a random number.				
				moveAllDown();			
		}		
		//scan for all equal cells
		for (int i = boardSize-1; i > 0; i--) { //i+1 = out of the array
			for (int j = 0; j < boardSize; j++) {
				if (currentGame.getXY(i, j) == currentGame.getXY(i-1, j) && currentGame.getXY(i, j) != emptyCell) { //if cells are equal
					currentGame.setScore(currentGame.getScore() + currentGame.getXY(i,j) * 2); //add the value to the score
					currentGame.setXY(i, j, currentGame.getXY(i, j) * 2); //double the current cell value
					if (currentGame.getXY(i, j) == winScore) 
						win = true;
					currentGame.setXY(i-1,j,emptyCell); //set the lower cell to 0
					merge = true;
				}						
			}			
		}		
		moveAllDown(); //remove all spaces (consolidate everything again) => 2,2,2,2 / 2,2,0,2 OR 2,2,2,0 -> 4,0,2,0 
		boolean change = (movement || merge);		
		moveHanlde(change);
	}	
	
	private boolean moveAllDown() {
		boolean movement=false; //flag to check if there was any movement
		for (int i = boardSize-1; i > 0; i--) { //i+1 = out of the array
			for (int j = 0; j < boardSize; j++) {
				if (currentGame.getXY(i, j) == emptyCell) {
					if (currentGame.getXY(i-1,j) != emptyCell) 
						//if a number which is not an empty cell was moved
						movement = true;
					currentGame.setXY(i, j, currentGame.getXY(i-1, j));
					currentGame.setXY(i-1, j, emptyCell);					
				}						
			}			
		}
		if (movement) {
			setChanged();
			notifyObservers();
		}
		return (movement); //returns true if there was a movement
	}
	
	/* **********************
	 * MoveLeft + MoveallLeft
	 * ********************** */

	@Override
	public void moveLeft() {
		int x=0;
		boolean merge = false; //flag to check if there was a merge
		boolean movement = false; //flag to check if there was any movement
		//remove all spaces (consolidate everything)
		while (x<boardSize-1) {
			x++;
			if (!movement) //if there was no movement then update movement boolean
				movement = moveAllLeft(); 
			else //if there was no movement then there is no need to generate a random number.				
				moveAllLeft();			
		}		
		//scan for all equal cells
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize-1; j++) {
				if (currentGame.getXY(i, j) == currentGame.getXY(i, j+1) && currentGame.getXY(i, j) != emptyCell) { //if cells are equal
					currentGame.setScore(currentGame.getScore() + currentGame.getXY(i,j) * 2); //add the value to the score
					currentGame.setXY(i, j, currentGame.getXY(i, j) * 2); //double the current cell value
					if (currentGame.getXY(i, j) == winScore) 
						win = true;
					currentGame.setXY(i,j+1,emptyCell); //set the lower cell to 0
					merge = true;
				}						
			}			
		}		
		moveAllLeft(); //remove all spaces (consolidate everything again) => 2,2,2,2 / 2,2,0,2 OR 2,2,2,0 -> 4,0,2,0 
		boolean change = (movement || merge);		
		moveHanlde(change);
	}
	
	
	//move everything left
	private boolean moveAllLeft() {
		boolean movement=false; //flag to check if there was any movement
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize-1; j++) {
				if (currentGame.getXY(i, j) == emptyCell) {
					if (currentGame.getXY(i,j+1) != emptyCell) 
						//if a number which is not an empty cell was moved
						movement = true;
					currentGame.setXY(i, j, currentGame.getXY(i, j+1));
					currentGame.setXY(i, j+1, emptyCell);					
				}						
			}			
		}
		if (movement) {
			setChanged();
			notifyObservers();
		}
		return (movement); //returns true if there was a movement
	}
	

	/* *************************
	 * MoveRight + MoveallRight
	 * ************************* */
	
	@Override
	public void moveRight() {
		int x=0;
		boolean merge = false; //flag to check if there was a merge
		boolean movement = false; //flag to check if there was any movement
		//remove all spaces (consolidate everything)
		while (x<boardSize-1) {
			x++;
			if (!movement) //if there was no movement then update movement boolean
				movement = moveAllRight(); 
			else //if there was no movement then there is no need to generate a random number.				
				moveAllRight();			
		}		
		//scan for all equal cells
		for (int i = 0; i < boardSize; i++) {
			for (int j = boardSize-1; j > 0; j--) {
				if (currentGame.getXY(i, j) == currentGame.getXY(i, j-1) && currentGame.getXY(i, j) != emptyCell) { //if cells are equal
					currentGame.setScore(currentGame.getScore() + currentGame.getXY(i,j) * 2); //add the value to the score
					currentGame.setXY(i, j, currentGame.getXY(i, j) * 2); //double the current cell value
					if (currentGame.getXY(i, j) == winScore) 
						win = true;
					currentGame.setXY(i,j-1,emptyCell); //set the left cell to 0
					merge = true;
				}						
			}			
		}		
		moveAllRight(); //remove all spaces (consolidate everything again) => 2,2,2,2 / 2,2,0,2 OR 2,2,2,0 -> 4,0,2,0 
		boolean change = (movement || merge);		
		moveHanlde(change);
	}
	
	
	//move everything left
	private boolean moveAllRight() {
		boolean movement=false; //flag to check if there was any movement
		for (int i = 0; i < boardSize; i++) {
			for (int j = boardSize-1; j > 0; j--) {
				if (currentGame.getXY(i, j) == emptyCell) {
					if (currentGame.getXY(i,j-1) != emptyCell) 
						//if a number which is not an empty cell was moved
						movement = true;
					currentGame.setXY(i, j, currentGame.getXY(i, j-1));
					currentGame.setXY(i, j-1, emptyCell);					
				}						
			}			
		}
		if (movement) {
			setChanged();
			notifyObservers();
		}
		return (movement); //returns true if there was a movement
	}
	
	
	@Override
	public void setCurrentGame(GameState game) {
		this.currentGame = game;
	}
	
	public GameState getCurrentGame() {
		return currentGame;
	}	


	@Override
	public void UpRight() {}


	@Override
	public void UpLeft() {}


	@Override
	public void DownRight() {}


	@Override
	public void DownLeft() {}


	@Override
	public void getHint(int treeSize) { //get single hint
		try {
				outToServer.writeObject(new SolveMsg("2048","MiniMax",UserCommand.Solve,currentGame,treeSize));
				outToServer.flush();
				SolveMsg solveMsg = (SolveMsg) inFromServer.readObject();
				doMove(solveMsg.getCmd());				
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void solveGame() { //solve the whole game
		while (!won || lose) {
			getHint(-1);			
			/* 
			 * TODO	
			 * System.out.println("sleep");
				Thread.sleep(600);
				System.out.println("wake up");
			 */
		}
		
	}	
	
	private void doMove(UserCommand cmd) {
		switch (cmd) {
			case Up:
				moveUp();
				break;
			case Down:
				moveDown();
				break;
			case Left:
				moveLeft();
				break;
			case Right:
				moveRight();
				break;
			default:
		}

	}

}
