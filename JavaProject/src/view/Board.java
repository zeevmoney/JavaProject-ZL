package view;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import view.Tile;


//need to tell it where need to "paint"
public class Board extends Composite{
//	int[][] boardData; // the data of the board	
	int rows;
	int colums;
	Tile[][] tiles;
	
		
	public Board(Composite parent, int style, int[][] data) {
	 	super(parent, style);  // call canvas Ctor
	 	
		rows=data.length;//calculate the number of rows
		colums=data[0].length;//calculate the number of columns
		setLayout(new GridLayout(colums,true));
		//boardData=new int [colums][rows];
		tiles=new Tile [colums][rows];
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < colums; y++){
				tiles[x][y]=new Tile(this, SWT.BORDER);
				tiles[x][y].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
				//tiles[x][y].setValue(data[x][y]);
			}
		}
		
	}
	// add the paint listener 
//	 addPaintListener(new PaintListener() { 
//	 
//	 @Override 
//	 public void paintControl(PaintEvent e) { 
//	 setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
//	 if(boardData!=null){ 
//	 //... 
//		 
//	 }
//	 }
//	 }); 
//	 
//	 
//	 }

	 public void updateBoard(int[][] data){
		 for (int i = 0; i < rows; i++) {
				for (int j = 0; j < colums; j++) {
					tiles[i][j].setValue(data[i][j]);	
				}
		 }
	 }
	 
	 

//	public int[][] getBoardData() {
//		return boardData;
//	}
//
//	public void setBoardData(int[][] boardData) {
//		this.boardData = boardData;
//	}

	

	
}



	 	