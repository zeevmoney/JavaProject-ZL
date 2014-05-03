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

//TODO: make the movement use a strategy pattern.
//TODO: save/load game with a file select

public class Game2048Model extends Observable implements Model,Runnable {
	GameState currentGame; //current game state
	Stack<GameState> gameStack; //stack of previous games
	final int emptyCell = 0;
	final int winScore = 2048;
	int boardSize;	
	boolean win;
	boolean lose;
	
		
	//Constructor
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
	
	@Override
	public void newGame() {
		currentGame = new GameState(boardSize,boardSize);
		boardInit();
		gameStack.clear();
		gameStack.add(currentGame.Copy());	
		setChanged();
		notifyObservers();	
	}
	
	
	@Override
	public void saveGame() {
		try {
			GameStateXML gXML = new GameStateXML();
			gXML.gameStateToXML(currentGame, "2048Save.xml",gameStack,"2048GameStack.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadGame() {
		try {
			newGame();
			GameStateXML gXML = new GameStateXML();
			currentGame = gXML.gameStateFromXML("2048Save.xml");
			gameStack = gXML.gameStackFromXML("2048GameStack.xml");
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
	private boolean canMove () {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (!currentGame.validXY(i+1, j) || !currentGame.validXY(i, j+1)) //invalid index 
					continue;
				//right cell equals / bottom cell equals
				if (currentGame.getXY(i, j) == currentGame.getXY(i+1, j) || currentGame.getXY(i, j) == currentGame.getXY(i, j+1)) {
					return true;
				}
			}
		}
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
		
	private void moveHanlde(boolean change) {
		setChanged(); //changed in any case
		if (change) { //if there was a change it means that there is an empty space.
			if (win) {
				notifyObservers("Win");
				System.out.println("DEBUG: WIN");				
			} else {
				addNumber();
				gameStack.add(currentGame.Copy());	
				notifyObservers();
			}
		} else if (!change && !canMove() && boardIsFull()) { //no change & can't move & board is full = lost the game.
			lose = true;
			System.out.println("DEBUG: LOSE");
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
	public void UpRight() {}


	@Override
	public void UpLeft() {}


	@Override
	public void DownRight() {}


	@Override
	public void DownLeft() {}	


}
