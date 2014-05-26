/*
 * The common model for all Model interfaces.
 */
package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.Stack;
import common.GameState;
import common.ObjectXML;
import common.SolveMsg;
import common.UserCommand;

/**
 * The Class AbsModel.
 */

public abstract class AbsModel extends Observable implements Model {
	
	/** The game stack. */
	Stack<GameState> gameStack; //stack of previous games
	
	/** The cmd from user. */
	UserCommand cmd; //user command ENUM
	
	/** The my server. */
	Socket myServer;
	
	/** The out to server. */
	public ObjectOutputStream outToServer;
	
	/** The in from server. */
	public ObjectInputStream inFromServer;
	
	/** The solving. - used to check if the client sent a new request to server. */
	boolean solving;

	/**
	 * Instantiates a new abs model.
	 */
	public AbsModel() {
		gameStack = new Stack<>();
	}	
			
	/* (non-Javadoc)
	 * @see model.Model#newGame()
	 */
	@Override
	public void newGame() {
;		boardInit();
		gameStack.clear();
		gameStack.add(getCurrentGame().Copy());		
		setChanged();
		notifyObservers();
	}
	
	/* (non-Javadoc)
	 * @see model.Model#saveGame(java.lang.String)
	 */
	@Override
	public void saveGame(String fileName) {
		try {
			getCurrentGame().setGameStack(gameStack); //clone the current game stack
			ObjectXML.objectToXML(getCurrentGame(),fileName); //save to xml
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see model.Model#loadGame(java.lang.String)
	 */
	@Override
	public void loadGame(String fileName) {
		try {
			newGame(); //used to eliminate some bugs.
			setCurrentGame((GameState) ObjectXML.ObjectFromXML(fileName));
			gameStack = getCurrentGame().getGameStack();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers();
	}		
		
	/* (non-Javadoc)
	 * @see model.Model#undoMove()
	 */
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
	
	/* (non-Javadoc)
	 * @see model.Model#connectToServer(java.lang.String, int)
	 */
	@Override
	public void connectToServer(String ip, int port) {
		try {
			myServer = new Socket(InetAddress.getByName(ip), port);
			outToServer = new ObjectOutputStream(myServer.getOutputStream());
			inFromServer = new ObjectInputStream(myServer.getInputStream());
			SolveMsg msg = (SolveMsg) inFromServer.readObject();
			System.out.println("[Client]: Recieved status from server: " + msg.getInfo());
			System.out.println("[Client]: Connected to server: "+ip+":"+port);
			setChanged();
			notifyObservers("Connected");
		} catch (Exception e) {
			solving = false;
			System.out.println("[Client]: Can't connect to server.");
			setChanged();
			notifyObservers("Disconnected");
		}
	}
	
	/* (non-Javadoc)
	 * @see model.Model#disconnectFromServer()
	 */
	@Override
	public void disconnectFromServer() {
		try {
			outToServer.writeObject(new SolveMsg(null,null,UserCommand.StopSolving,null,null));
			outToServer.writeObject(UserCommand.Disconnect);
			outToServer.flush();
			outToServer.close();
			inFromServer.close();
			myServer.close(); //close the socket
		} catch (Exception e) {
			System.out.println("[Client]: Not connected.");
		} finally { //happens in any case.
			solving = false;
			System.out.println("[Client]: Colsing Connection");
			setChanged();
			notifyObservers("Disconnected");
		}
	}
	
	//not abstract since it's not needed in every extending class
	/* (non-Javadoc)
	 * @see model.Model#getHint(int, int)
	 */
	@Override
	public void getHint(int hintsNum, int treeDepth) {} 
	
	//not abstract since it's not needed in every extending class
	/* (non-Javadoc)
	 * @see model.Model#solveGame(int)
	 */
	@Override
	public void solveGame(int treeDepth) {}
	
	/**
	 * Checks if is solving.
	 *
	 * @return true, if is solving
	 */
	public boolean isSolving() {
		return solving;
	}

	/**
	 * Sets the solving.
	 *
	 * @param solving - boolean
	 */
	public void setSolving(boolean solving) {
		this.solving = solving;
	}
		
	/**
	 * Gets the game stack.
	 *
	 * @return the game stack
	 */
	public Stack<GameState> getGameStack() {
		return gameStack;
	}
	
	/**
	 * Sets the game stack.
	 *
	 * @param gameStack the new game stack
	 */
	public void setGameStack(Stack<GameState> gameStack) {
		this.gameStack = gameStack;
	}

	/**
	 * Game stack push.
	 *
	 * @param state the state
	 */
	public void gameStackPush(GameState state) {
		this.gameStack.push(state.Copy());
	}
	
	/**
	 * Game stack peek.
	 *
	 * @return the game state
	 */
	public GameState gameStackPeek() {
		return this.gameStack.peek();
	}
		
	/* (non-Javadoc)
	 * @see model.Model#getBoard()
	 */
	@Override
	public int[][] getBoard() {
		return getCurrentGame().getBoard();	
	}

	/* (non-Javadoc)
	 * @see model.Model#getScore()
	 */
	@Override
	public int getScore() {
		return getCurrentGame().getScore();
	}

	/* (non-Javadoc)
	 * @see model.Model#moveUp()
	 */
	@Override
	public abstract void moveUp();

	/* (non-Javadoc)
	 * @see model.Model#moveDown()
	 */
	@Override
	public abstract void moveDown();

	/* (non-Javadoc)
	 * @see model.Model#moveLeft()
	 */
	@Override
	public abstract void moveLeft();

	/* (non-Javadoc)
	 * @see model.Model#moveRight()
	 */
	@Override
	public abstract void moveRight();

	/* (non-Javadoc)
	 * @see model.Model#UpRight()
	 */
	@Override
	public abstract void UpRight();

	/* (non-Javadoc)
	 * @see model.Model#UpLeft()
	 */
	@Override
	public abstract void UpLeft();

	/* (non-Javadoc)
	 * @see model.Model#DownRight()
	 */
	@Override
	public abstract void DownRight();

	/* (non-Javadoc)
	 * @see model.Model#DownLeft()
	 */
	@Override
	public abstract void DownLeft();

	/**
	 * Gets the current game.
	 *
	 * @return the current game
	 */
	public abstract GameState getCurrentGame();
	
	/**
	 * Sets the current game.
	 *
	 * @param game the new current game
	 */
	public abstract void setCurrentGame(GameState game);
	
	/**
	 * Board init.
	 */
	public abstract void boardInit();

}
