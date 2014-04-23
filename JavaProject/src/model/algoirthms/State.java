package model.algoirthms;

/* 
 * State is used to describe the current Game state
 * of any game with a 2D array.
*/

public class State {
	int[][] board;
	int score;
		
	//Consturctor
	
	public State() {
		}
		public State(int boardSize) {
			this.board = new int[boardSize][boardSize];
			this.score = 0; //at the beginning of the game score is 0
		}

		public int getBoardSize()
		{
			return this.board.length;
		}

	}
     


	

	
	
	
	


	
}
