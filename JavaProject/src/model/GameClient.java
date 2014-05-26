package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;
import common.SolveMsg;

/**
 * The Class GameClient, this class is used for sending msgs to server.
 */
public class GameClient implements Callable<SolveMsg> {
	
	/** The output. */
	private ObjectOutputStream OutToServer;
	
	/** The input. */
	private ObjectInputStream inFromServer;
	
	/** The solve msg. */
	private SolveMsg solveMsg;
	

	/**
	 * Instantiates a new game client.
	 *
	 * @param OutToServer the out to server
	 * @param inFromServer the in from server
	 * @param solveMsg the solve msg
	 */
	public GameClient(ObjectOutputStream OutToServer, ObjectInputStream inFromServer, SolveMsg solveMsg ) {
		this.OutToServer = OutToServer;
		this.inFromServer = inFromServer;
		this.solveMsg = solveMsg;
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public SolveMsg call() throws Exception {
		try {
			OutToServer.writeObject(solveMsg);
			OutToServer.flush();
			return (SolveMsg) inFromServer.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
}
