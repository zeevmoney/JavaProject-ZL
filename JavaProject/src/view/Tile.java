package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/*
 * Tile class is used to represent a tile on the game board.
 */


//TODO: auto change for the font size.

public class Tile extends Canvas{
  	
	private int value; //tile value 
	    
	//Constructor
	public Tile(Composite parent, int style){
		super(parent,style);
		value=0; 
//		int fontNumber=(Math.min(arg0, arg1)/2);
//		Font f= getFont();
//		Font nf= new Font(getDisplay(), f.getFontData()[0].getName(),fontNumber,SWT.BOLD);
//		setFont(nf);
		addPaintListener(new PaintListener() {
		
			@Override
			public void paintControl(PaintEvent e) {
				FontMetrics fm = e.gc.getFontMetrics();
				int width=fm.getAverageCharWidth();
				int mx=getSize().x/2- (""+value).length()*width/2;
				int my=getSize().y/2- fm.getHeight()/2-fm.getDescent();
				if(value>0)
					e.gc.drawString(" "+value, mx, my);			
			}
	   });	   
   }
   
   
	//value & color setter.
	public void setValue(int value) {
		this.value=value;
		changeBackgroundColor(); 
		redraw();
	}   

	//change the tile background color
	private void changeBackgroundColor(){
		Color tileColor;
		switch (value) {		
			case -1:   tileColor = new Color(getDisplay(), 0, 0, 0); break;
			case 0:    tileColor = new Color(getDisplay(), 204, 192, 179); break;
		    case 2:    tileColor = new Color(getDisplay(), 238, 228, 218); break;
		    case 4:    tileColor = new Color(getDisplay(), 237, 224, 200); break;
		    case 8:    tileColor = new Color(getDisplay(), 242, 176, 120); break;
		    case 16:   tileColor = new Color(getDisplay(), 245, 151, 99); break;
		    case 32:   tileColor = new Color(getDisplay(), 245, 125, 95); break;
		    case 64:   tileColor = new Color(getDisplay(), 246, 95, 59); break;
	        case 128:  tileColor = new Color(getDisplay(), 237, 206, 113); break;
		    case 256:  tileColor = new Color(getDisplay(), 237, 204, 97); break;
		    case 512:  tileColor = new Color(getDisplay(), 237, 200, 80); break;
		    case 1024: tileColor = new Color(getDisplay(), 237, 197, 63); break;
		    case 2048: tileColor = new Color(getDisplay(), 237, 194, 46); break;
		    default:   tileColor = new Color(getDisplay(), 220, 186, 49); break;
		}
		setBackground(tileColor);
	}

}

