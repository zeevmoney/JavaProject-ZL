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
	private ObjectOutputStream output;
	
	/** The input. */
	private ObjectInputStream input;
	
	/** The solve msg. */
	private SolveMsg solveMsg;
	
	
	/**
	 * Instantiates a new gameclient.
	 *
	 * @param output the output
	 * @param input the input
	 * @param solveMsg the solve msg
	 */
	public GameClient(ObjectOutputStream output, ObjectInputStream input, SolveMsg solveMsg ) {
		this.output = output;
		this.input = input;
		this.solveMsg = solveMsg;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public SolveMsg call() throws Exception {
		return getHint();		
	}
	
	
	/**
	 * Gets the hint.
	 * 
	 * @return the hint
	 */
	public SolveMsg getHint() {
		try {
		output.writeObject(solveMsg);
		output.flush();
		return (SolveMsg) input.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
