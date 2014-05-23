package model;

import java.util.Observable;
import java.util.Stack;

import model.algoirthms.GameStateXML;
import model.algoirthms.GameState;
import controller.UserCommand;

public abstract class AbsModel extends Observable implements Model {
	Stack<GameState> gameStack; //stack of previous games
	UserCommand cmd; //user command ENUM

	public AbsModel() {
		gameStack = new Stack<>();
	}	
	
		
	@Override
	public void newGame() {
;		boardInit();
		gameStack.clear();
		gameStack.add(getCurrentGame().Copy());		
		setChanged();
		notifyObservers();
	}
	

	@Override
	public void saveGame(String fileName) {
		try {
			GameStateXML gXML = new GameStateXML();
			getCurrentGame().setGameStack(gameStack); //clone the current game stack
			gXML.gameStateToXML(getCurrentGame(),fileName); //save to xml
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadGame(String fileName) {
		try {
			newGame(); //used to eliminate some bugs.
			GameStateXML gXML = new GameStateXML();
			setCurrentGame(gXML.gameStateFromXML(fileName));
			gameStack = getCurrentGame().getGameStack();
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
		if (getCurrentGame().equals(tempState)) { 
			if (!gameStack.isEmpty()) {
				setCurrentGame(gameStack.pop()); 
			} else { 
				gameStack.push(getCurrentGame().Copy());
				return;
			}
		} else
			setCurrentGame(tempState);		
		if (gameStack.isEmpty())
			gameStack.push(getCurrentGame().Copy());
		setChanged();
		notifyObservers();	
	}
	
	/*
	 * Methods for handling the game stack.
	 */
	public Stack<GameState> getGameStack() {
		return gameStack;
	}
	
	public void setGameStack(Stack<GameState> gameStack) {
		this.gameStack = gameStack;
	}

	public void GameStackPush(GameState state) {
		this.gameStack.push(state.Copy());
	}	
		
	
	@Override
	public int[][] getBoard() {
		return getCurrentGame().getBoard();	
	}

	@Override
	public int getScore() {
		return getCurrentGame().getScore();
	}

	@Override
	public abstract void moveUp();

	@Override
	public abstract void moveDown();

	@Override
	public abstract void moveLeft();

	@Override
	public abstract void moveRight();

	@Override
	public abstract void UpRight();

	@Override
	public abstract void UpLeft();

	@Override
	public abstract void DownRight();

	@Override
	public abstract void DownLeft();

	public abstract GameState getCurrentGame();
	
	public abstract void setCurrentGame(GameState game);
	
	public abstract void boardInit();


}
