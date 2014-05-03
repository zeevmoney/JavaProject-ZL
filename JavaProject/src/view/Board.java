package view;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import view.Tile;


/*
 * This class holds the game board. Can be used for any 2D array game
 */


//TODO: check if this class needs a paint listener
public class Board extends Composite{
	int rows;
	int colums;
	Tile[][] tiles;
	
	
	//Board Constructor
	public Board(Composite parent, int style, int[][] data) {
		super(parent, style); //Composite constructor
	 	rows=data.length; //number of rows
		colums=data[0].length; //number of columns
		setLayout(new GridLayout(colums,true));		
		tiles=new Tile [colums][rows];
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < colums; y++){
				tiles[x][y]=new Tile(this, SWT.BORDER);
				tiles[x][y].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));				
			}
		}
		
	}

	public void updateBoard(int[][] data){
		 for (int i = 0; i < rows; i++) {
				for (int j = 0; j < colums; j++) {
					tiles[i][j].setValue(data[i][j]);	
				}
		 }
	 }
	 
	
}



	 	