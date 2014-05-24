package common;

import controller.UserCommand;


/*
 * This class is shared between client and server.
 * The purpose of this class it to hold a message with all the needed parameters
 * TODO: serialize?
 */
public class SolveMsg {
	private String gameName; //game name: 2048 / Maze / etc...
	private String gameAlgo; //solving algorithm: MiniMax / Astar / BFS / etc..
	private UserCommand cmd; //Solve / Hint / etc...
	private GameState currentState; //current game state
	private Object info; //general info msg (can hold various things, based on the game).
	
	
	public SolveMsg() {}
	
	public SolveMsg(String nGameName, String nGameAlgo, UserCommand nCmd, GameState state, Object nInfo) {
		this.gameName = nGameName;
		this.gameAlgo = nGameAlgo;
		this.cmd = nCmd;
		this.currentState = state;
		this.info = nInfo;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameAlgo() {
		return gameAlgo;
	}

	public void setGameAlgo(String gameAlgo) {
		this.gameAlgo = gameAlgo;
	}

	public UserCommand getCmd() {
		return cmd;
	}

	public void setCmd(UserCommand cmd) {
		this.cmd = cmd;
	}

	public GameState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(GameState currentState) {
		this.currentState = currentState;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}
	
	

	
}
