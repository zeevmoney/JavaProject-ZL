package model.algoirthms;

/* 
 * Action interface for implementing different types of actions in order to inject into the Astar class.
 * Action = an Edge in the graph.
*/

public interface Action {
	
	//Move the state to the next state
	State doAction(State state); 
	
	//get the name of the Action
	String getName ();
	
}