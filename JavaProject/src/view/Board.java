package view;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import view.Tile;


//need to see if needed paint lister
public class Board extends Composite{

	int rows;
	int colums;
	Tile[][] tiles;
	
	//create empty game board	
	public Board(Composite parent, int style, int[][] data) {
	 	super(parent, style);  // call canvas Ctor
	 	
		rows=data.length;//calculate the number of rows
		colums=data[0].length;//calculate the number of columns
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



	 	