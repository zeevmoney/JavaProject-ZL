package model.alogirthms;


/* State is used to describe the current state of the algorithm (the graph Vertex/Node).
 * State is comparable in order to keep the priority queue sorted.
*/

public class State implements Comparable <State> {
	private double f;
	private double g;
	private State cameFrom; //used to reconstruct the path after the search is done
    private Action action; //The action that led to the current state.
    private Object state; //Abstract Object to hold the coordinates of a state (Point, String, etc.. depends on the problem).

     
    //Setters and Getters.
	public double getF() {
		return f;
	}
	public void setF(double x) {
		this.f = x;
	}
	public double getG() {
		return g;
	}
	public void setG(double x) {
		this.g = x;
	}
	
	public State getCameFrom() {
		return cameFrom;
	}
	public void setCameFrom(State cameFrom) {
		this.cameFrom = cameFrom;
	}
	
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	
	public Object getState() {
		return state;
	}
	public void setState(Object state) {
		this.state = state;
	}
	
	@Override
	public int compareTo(State o) {
		return (int) (f - o.getF());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}




	

	
	
	
	


	
}
