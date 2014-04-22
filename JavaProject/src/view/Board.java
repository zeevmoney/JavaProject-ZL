package view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class Board extends Canvas {
	int [][] boardData;
	public Board(Composite parent, int style){
		super(parent,style);
		addPaintListener(new PaintListener(){
			@Override
			public void paintControl(PaintEvent e){
				if(boardData!=null){
					// TODO Auto-generated method stub.....
				}
			}

			
			
		});
	//.....	
	}
			

}
