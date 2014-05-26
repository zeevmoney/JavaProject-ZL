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

public abstract class AbsModel extends Observable implements Model {
	Stack<GameState> gameStack; //stack of previous games
	UserCommand cmd; //user command ENUM
	Socket myServer;
	public ObjectOutputStream outToServer;
	public ObjectInputStream inFromServer;
	boolean solving; //used to check if the client sent a new request to server.

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
			getCurrentGame().setGameStack(gameStack); //clone the current game stack
			ObjectXML.objectToXML(getCurrentGame(),fileName); //save to xml
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
	
	@Override
	public void connectToServer(String ip, int port) {
		try {
			myServer = new Socket(InetAddress.getByName(ip), port);
			outToServer = new ObjectOutputStream(myServer.getOutputStream());
			inFromServer = new ObjectInputStream(myServer.getInputStream());
			SolveMsg msg = (SolveMsg) inFromServer.readObject();
			System.out.println("[Client]: Recieved Status: " + msg.getInfo());
			System.out.println("[Client]: Connected to server: "+ip+":"+port);
			setChanged();
			notifyObservers("Connected");
		} catch (Exception e) {
			//e.printStackTrace();
			solving = false;
			System.out.println("[Client]: Can't connect to server.");
			setChanged();
			notifyObservers("Disconnected");
		}
	}
	
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
		} finally {
			solving = false;
			System.out.println("[Client]: Colsing Connection");
			setChanged();
			notifyObservers("Disconnected");
		}
	}
	
	@Override
	public void getHint(int hintsNum, int treeDepth) {}
	
	@Override
	public void solveGame(int treeDepth) {}
		
	
	/*
	 * Methods for handling the game stack.
	 */
	public Stack<GameState> getGameStack() {
		return gameStack;
	}
	
	public void setGameStack(Stack<GameState> gameStack) {
		this.gameStack = gameStack;
	}

	public void gameStackPush(GameState state) {
		this.gameStack.push(state.Copy());
	}
	
	public GameState gameStackPeek() {
		return this.gameStack.peek();
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


	public boolean isSolving() {
		return solving;
	}

	public void setSolving(boolean solving) {
		this.solving = solving;
	}


}
