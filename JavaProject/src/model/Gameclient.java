package model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;
import common.SolveMsg;

public class Gameclient implements Callable<SolveMsg> {
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private SolveMsg solveMsg;
	
	
	public Gameclient(ObjectOutputStream output, ObjectInputStream input, SolveMsg solveMsg ) {
		this.output = output;
		this.input = input;
		this.solveMsg = solveMsg;
	}
	
	
	
	@Override
	public SolveMsg call() throws Exception {
		return getHint();		
	}
	
	
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
