package view;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;

import view.Tile;

public class Board extends Composite{
	

	int[][] boardData; // the data of the board	
	int rows;
	int colums;
	
		
	public Board(Composite parent, int style, int[][] data) {
	 	super(parent, style);  // call canvas Ctor

		rows=data.length;//calculate the number of rows
		colums=data[0].length;//calculate the number of columns
		boardData=new int [colums][rows];
		Tile tiles[][]=new Tile [colums][rows];
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < colums; y++){
				tiles[x][y]=new Tile(this, SWT.BORDER);
				tiles[x][y].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
				tiles[x][y].setValue(boardData[x][y]);
			}
		}
	}

}

	 	