package common;

/*
 * Enum to hold configuration for the different model elements.
 */


public enum ModelElements {
	
	MazeStart(-5),
	MazeWall(-1),
	MazeEnd(-3),
	MazeEmpty(-4),
	MazePlayer(-5),
	MazeHorizontalScore(10),
	MazeDiagonalScore(15),
	Game2048Empty(0);
	
	/* TODO: Delete this.
	static final int Start = -5;
	static final int Wall = -1;
	static final int End = -3;
	static final int Empty = -4;
	static final int Player = -5;
	static final int HorizontalScore = 10;
	static final int DiagonalScore = 15;
	 */
	
	
	private int element;
	
	//default constructor
	private ModelElements(int element) {
		this.element = element;
	}
	public int getElement() {
		return element;
	}
	public void setElemnt(int element) {
		this.element = element;
	}
}
