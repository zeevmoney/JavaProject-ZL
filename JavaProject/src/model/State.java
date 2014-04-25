package model;

public class State {
	
	State StartState;
	State fatherState;
	
	State (){
		StartState=null;
		fatherState=null;
	}
	
	
	public State  undoMove(){
		return fatherState;
	}
	public State restartGame(){
		return StartState;
	}
	
	
}
