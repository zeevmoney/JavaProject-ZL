package view;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import view.Tile;


/**
 * The Class Board.
 * This class holds the game board. Can be used for any 2D array game
 */
public class Board extends Composite{
	
	/** The rows. */
	int rows;
	
	/** The colums. */
	int colums;
	
	/** The tiles. */
	Tile[][] tiles;
	
	
	/**
	 * Instantiates a new board.
	 *
	 * @param parent the parent
	 * @param style the style
	 * @param data the data
	 */
	public Board(Composite parent, int style, int[][] data) {
		super(parent, style); //Composite constructor
	 	rows=data.length; //number of rows
		colums=data[0].length; //number of columns
		setLayout(new GridLayout(colums,true));		
		tiles=new Tile [colums][rows];
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < colums; y++){
				tiles[x][y]=new Tile(this, SWT.BORDER_SOLID);
				tiles[x][y].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));				
			}
		}
		
	}

	/**
	 * Update board.
	 *
	 * @param data the data
	 */
	public void updateBoard(int[][] data){
		 for (int i = 0; i < rows; i++) {
				for (int j = 0; j < colums; j++) {
					tiles[i][j].setValue(data[i][j]);	
				}
		 }
	 } 
	
}	 	