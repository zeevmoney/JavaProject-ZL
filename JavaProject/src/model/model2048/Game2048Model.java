package model.model2048;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import model.AbsModel;
import model.GameClient;
import common.GameState;
import common.ModelElements;
import common.SolveMsg;
import common.UserCommand;

//TODO: in case lost the game: when clicking no make sure lost is set to false and undo 1 move.

/**
 * 2048GameModel Class, responsible for all the game logic.
 *  
 * setChanged(): Marks this Observable object as having been changed
 * 
 * notifyObservers(): If this object has changed, as indicated by the hasChanged method, 
 * then notify all of its observers and then call the clearChanged method to indicate 
 * that this object has no longer changed.
 */
public class Game2048Model extends AbsModel {
	
	/** The current game. */
	GameState currentGame; //current game state
	
	/** The win score. */
	final int winScore;
	
	/** The board size. */
	final int boardSize;
	
	/** The empty cell. */
	int emptyCell = ModelElements.Game2048Empty.getElement();
	
	/** The win. */
	boolean win;
	
	/** The lose. */
	boolean lose;
	
	/** The won. - to make sure the game was only won once. */
	boolean won; 
	
	//static final String gameName = new String("2048");
	
	/**
	 * Instantiates a new game2048 model.
	 * 
	 * @param boardSize
	 *            the board size
	 * @param winScore
	 *            the win score
	 */
	public Game2048Model(int boardSize,int winScore) {
		this.winScore = winScore;
		this.boardSize=boardSize;
		newGame();
	}
	
	
	//init all values on the game board & set score to 0.
	/* (non-Javadoc)
	 * @see model.AbsModel#boardInit()
	 */
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
	/**
	 * Adds the number.
	 */
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
	/**
	 * Can move.
	 * 
	 * @return true, if successful
	 */
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
	/**
	 * Board is full.
	 * 
	 * @return true, if board is full
	 */
	private boolean boardIsFull() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (currentGame.getXY(i, j) == emptyCell) 
					return false;				
			}
		}
		return true;
	}
	
	/**
	 * Move hanlde - checks if the game was won (the game will continue after 2048 was reached)
	 * 
	 * @param change
	 *            the change
	 */
	private void moveHanlde(boolean change) {
		if (change) { //if there was a change it means that there is an empty space.
			if (win && !won) { //if 2048 was reached
				won = true; //can't win twice (win = 2048, when you continue, you can't win again).
				setChanged();
				notifyObservers("Win");				
			} else {
				addNumber();
				gameStackPush(currentGame.Copy());
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
	
	/* (non-Javadoc)
	 * @see model.AbsModel#moveUp()
	 */
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
	/**
	 * Move all up.
	 * 
	 * @return true, if successful
	 */
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

	/* (non-Javadoc)
	 * @see model.AbsModel#moveDown()
	 */
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
	
	/**
	 * Move all down.
	 * 
	 * @return true, if successful
	 */
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

	/* (non-Javadoc)
	 * @see model.AbsModel#moveLeft()
	 */
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
	/**
	 * Move all left.
	 * 
	 * @return true, if successful
	 */
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
	
	/* (non-Javadoc)
	 * @see model.AbsModel#moveRight()
	 */
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
	/**
	 * Move all right.
	 * 
	 * @return true, if successful
	 */
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
	
	
	/* (non-Javadoc)
	 * @see model.AbsModel#setCurrentGame(common.GameState)
	 */
	@Override
	public void setCurrentGame(GameState game) {
		this.currentGame = game;
	}
	
	/* (non-Javadoc)
	 * @see model.AbsModel#getCurrentGame()
	 */
	public GameState getCurrentGame() {
		return currentGame;
	}	

	/* (non-Javadoc)
	 * @see model.AbsModel#UpRight()
	 */
	@Override
	public void UpRight() {}

	/* (non-Javadoc)
	 * @see model.AbsModel#UpLeft()
	 */
	@Override
	public void UpLeft() {}

	/* (non-Javadoc)
	 * @see model.AbsModel#DownRight()
	 */
	@Override
	public void DownRight() {}


	/* (non-Javadoc)
	 * @see model.AbsModel#DownLeft()
	 */
	@Override
	public void DownLeft() {}

	/* (non-Javadoc)
	 * @see model.AbsModel#getHint(int, int)
	 */
	@Override
	public void getHint(int hintsNum,int treeDepth) {
		try {
			while(!(won || lose) && hintsNum > 0) {
				if (!isSolving()) {
					outToServer.writeObject(new SolveMsg("2048","MiniMax",UserCommand.Solve,null,null)); //first msg is used to set the Solver
					setSolving(true);					
				}
				ExecutorService executor = Executors.newSingleThreadExecutor();
				SolveMsg msg = new SolveMsg("2048", "MiniMax", UserCommand.Solve, gameStackPeek(), treeDepth);
				Future<SolveMsg> result = executor.submit(new GameClient(outToServer, inFromServer, msg));
				executor.shutdown();
				doMove(result.get().getCmd());
				Thread.sleep(100);
				hintsNum--;
			}
		} catch (Exception e) {
			setSolving(false);
			System.out.println("[Client]: Lost connection to server.");
			setChanged();
			notifyObservers("Disconnected");
		}		
	}
		
	/**
	 * Do move.
	 * 
	 * @param cmd - UserCommand cmd
	 */
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
