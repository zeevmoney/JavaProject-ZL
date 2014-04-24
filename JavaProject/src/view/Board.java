package view;
import java.awt.Color;
import java.awt.Graphics;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
public class Board extends Canvas{
	int[][] boardData; // the data of the board
	public Canvas canvas;
	double mX;
	double mY;
	int rows;
	int colums;
	public Board(Composite parent, int style, final int[][] data) {
		super(parent, style);  // call canvas Ctor
		// add the paint listener
		addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
					// paint the board...
				if(boardData!=null){
					double maxX = canvas.getSize().x;
					double maxY = canvas.getSize().y;
					rows=data.length;//calculate the number of rows
					colums=data[0].length;//calculate the number of columns
					mX= rows/(maxX+2);//calculate the cell high
					mY= colums/(maxY+2);//calculate the cell width
								//...
				}
			}
		});
		//...
	}
	
//get board data
public int[][] getBoardData() {
	return boardData;
}

//set board data
public void setBoardData(int[][] data) {
	GC gc = new GC(canvas);
	

	for (int x = 0; x < rows; x++) {
		for (int y = 0; y < colums; y++){
			//gc.drawRectangle(get(x),get(y),getDown(x),getDown(y)); 	
		}
			

//	gc.drawRectangle(5,5,90,45); 
//	this.boardData = boardData;}

}




//change the board color
public void setBoardColor(int[][] data){
	for (int i = 0; i < data.length; i++) {
		for (int j = 0; j < data[i].length; j++){
			setColor(i,j);
		}			
	}	
}

//set the background color each cell
private void setColor(int x, int y) {

	e.gc.setBackground(display.getSystemColor(color)); 
	e.gc.fillRectangle(get(x),get(y),getDown(x),getDown(y));
}
//get start point of Rectangle
private double getDown(int x) {
	return (x*mx+
}

//get end point of Rectangle
private Object get(int x) {
	// TODO Auto-generated method stub
	return null;
}
	//get the background color each cell by sending the number of the the cell
	private Color getBackground(int value) {
		switch (value) {
	      case -1:   return new Color(0xe);		
	      case 2:    return new Color(0xeee4da);
	      case 4:    return new Color(0xede0c8);
	      case 8:    return new Color(0xf2b179);
	      case 16:   return new Color(0xf59563);
	      case 32:   return new Color(0xf67c5f);
	      case 64:   return new Color(0xf65e3b);
	      case 128:  return new Color(0xedcf72);
	      case 256:  return new Color(0xedcc61);
	      case 512:  return new Color(0xedc850);
	      case 1024: return new Color(0xedc53f);
	      case 2048: return new Color(0xedc22e);
	    }
	    return new Color(0xcdc1b4);
	}
	
}