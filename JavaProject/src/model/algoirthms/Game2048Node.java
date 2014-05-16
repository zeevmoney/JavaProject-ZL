package model.algoirthms;



import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import AbsMinimax.AbsNode;
import view.Tile;
import controller.UserCommand;

public class Game2048Node implements AbsNode{

	public static final Integer BOARD_SIZE = null;
	private Object boardArray;
	private int[][] dataArray;
    
    /**
     * Checks whether the game is terminated
     * 
     * @return 
     * @throws java.lang.CloneNotSupportedException 
     */
    public boolean isGameTerminated() throws CloneNotSupportedException {
        boolean terminated=false;
        
        if(hasWon()==true||hasLose()) {
            terminated=true;
        }
        return terminated;
    }

	public int getScore() {		
		return model.getScore();
	}

	public int getNumberOfEmptyCells() {
		int counter=0;
        
        int rows=dataArray.length; //number of rows
		int colums=dataArray[0].length; //number of columns
		
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < colums; y++){
                if(dataArray[x][y]==0) {
                	counter++;
                }
            }
        }
        
        return counter;
	}

	public int move(String direction, Board board) {
		board.setFocus();
		switch (direction){
		case "DOWN":
			setUi(UserCommand.Down); 
		    break;
		case "LEFT":
			setUi(UserCommand.Left);
			
			break;
		case "RIGHT":
			setUi(UserCommand.Right);
			
			break;
		case "UP":
			setUi(UserCommand.Up);
			break;				     
		}	
		setChanged();
		notifyObservers();	
		return 0;
	}
//return list of the empty tiles
	public List<Integer> getEmptyCellIds() {

        List<Integer> cellList = new ArrayList<Integer>();
        
        int rows=dataArray.length; //number of rows
		int colums=dataArray[0].length; //number of columns
		
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < colums; y++){
                if(dataArray[x][y]==0) {
                    cellList.add(rows*x+y);
                }
            }
        }
        
        return cellList;
	}
//
	public void setEmptyCell(int i, int j, int value,int [][] data) {
		if(dataArray[i][j]==0)
			dataArray[i][j]=value;
		
	}

	public boolean hasWon() {
		return UserCommand.GetWin;
	}
	
	@Override
    public Object clone() throws CloneNotSupportedException {
        Board copy = (Board)super.clone();
        copy.boardArray = clone2dArray(boardArray);
        return copy;
    }

	private Object clone2dArray(Object boardArray2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	 private int[][] clone2dArray(int[][] original) { 
	        int[][] copy = new int[original.length][];
	        for(int i=0;i<original.length;++i) {
	            copy[i] = original[i].clone();
	        }
	        return copy;
	    }


}
