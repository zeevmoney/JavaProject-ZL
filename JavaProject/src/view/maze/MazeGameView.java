package view.maze;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import controller.UserCommand;
import view.AbsView;
import view.Board;


/*
 * TODO: (Lital)
 * Don't forget to add:
 * setChanged & notifyObservers
 */

public class MazeGameView extends AbsView implements Runnable {
	Board board;
	
	public MazeGameView(String string) {
		super(string);
	}
		
	//display the board
	@Override
	public void displayBoard(int[][] data) {
		if (board == null) {
			//TODO: code cleanup.
			board = new Board(getGameBoard(), SWT.WRAP, data);
			board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,2));
			Color boardColor = new Color(getDisplay(), 187, 173, 160); //set board color 
			board.setBackground(boardColor);
			board.setForeground(boardColor);
			getShell().setMinimumSize(800, 800);
			getDisplay().addFilter(SWT.KeyUp, new Listener() {		
				@Override
				public void handleEvent(Event e) { 
					board.setFocus();
					switch (e.keyCode){
					case SWT.ARROW_DOWN:
						setUi(UserCommand.Down); 
					    break;
					case SWT.ARROW_LEFT:
						setUi(UserCommand.Left);
						
						break;
					case SWT.ARROW_RIGHT:
						setUi(UserCommand.Right);
						
						break;
					case SWT.ARROW_UP:
						setUi(UserCommand.Up);
						break;				     
					}	
					setChanged();
					notifyObservers();	
				}
			});	
		//mouse listener	
		getDisplay().addFilter(SWT.MouseDown, new Listener() {
		int x=0;
		int y=0;
		Boolean flag=true;
			@Override
			public void handleEvent(Event e) {
			 x=e.x;
			 y=e.y;
			
			 getDisplay().addFilter(SWT.MouseUp, new Listener() {
				 @Override
					public void handleEvent(Event e) {
					 if(e.x>x){
						 if(e.y==y)
						 setUi(UserCommand.Right);
						 else if(e.y>y)
							 setUi(UserCommand.DownRight);
						 else
							 setUi(UserCommand.UpRight);
						 }
					 else if(e.x<x ){
						 if(e.y==y)
						 setUi(UserCommand.Left);
						 else if(e.y>y)
							 setUi(UserCommand.DownLeft);
						 else
							 setUi(UserCommand.UpLeft);
						 }
					 else if(e.x==x ){
						 if(e.y>y)
							 setUi(UserCommand.Down);
						 else if(e.y<y)
							 setUi(UserCommand.Up);
						 }
					 if((e.x!=x||e.y!=y)&&flag==true){
					 flag=false;
					 setChanged();
					 notifyObservers();	
					 }
				 }
				 
				 
			 });
			 flag=true;	
			}
		
		});
	} else {
		board.updateBoard(data);
	}
}
	
	//update score 
	@Override
	public void displayScore(int score) {
		 getScoreLabel().setText("Score: "+score);		
	}
		
	@Override
	public UserCommand getUserCommand() {
		return getUi();
	}	

	//the GUI and main loop thread should be in the same THREAD.
	@Override
	public void run() {
		//while shell is alive
		while (!getShell().isDisposed()) {
			//while there are no events (this is the event handler)
			if(!getDisplay().readAndDispatch()) {
				//the OS will auto wake the display on EVENT (mouse, keyboard, etc).
				getDisplay().sleep();
			}
		}
		//if shell dies -> display dies.
		getDisplay().dispose();			
	}

}
