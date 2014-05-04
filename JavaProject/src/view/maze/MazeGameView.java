package view.maze;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import controller.UserCommand;
import view.AbsView;
import view.Board;


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
			//Color boardColor = new Color(getDisplay(), 187, 173, 160); //set board color 
			//Color boardColor = new Color(getDisplay(), 250, 248, 239); //set board color
			Color boardColor = new Color(getDisplay(), 0, 0, 0); //set board color
			board.setBackground(boardColor);
			board.setForeground(boardColor);
			getShell().setMinimumSize(800, 800);
			getDisplay().addFilter(SWT.KeyUp, new Listener() {
				int UpPresses=0;
				int DownPresses=0;
				int RightPresses=0;
				int LeftPresses=0;
				@Override
				public void handleEvent(Event e) { 
					board.setFocus();
					switch (e.keyCode){
					case SWT.ARROW_DOWN:
						DownPresses=1; 
					    break;
					case SWT.ARROW_LEFT:
						LeftPresses=1;						
						break;
					case SWT.ARROW_RIGHT:
						RightPresses=1;						
						break;
					case SWT.ARROW_UP:
						UpPresses=1;
						break;				     
					}	
					if((DownPresses+LeftPresses+RightPresses+UpPresses)==2){
						if(UpPresses==1&&LeftPresses==1)
							setUi(UserCommand.UpLeft);
						else if(UpPresses==1&&RightPresses==1)
							setUi(UserCommand.UpRight);
						else if (DownPresses==1&&RightPresses==1)
							setUi(UserCommand.DownRight);
						else if (DownPresses==1&&LeftPresses==1)
							setUi(UserCommand.DownLeft);
					}
					else{
						if(UpPresses==1)
							setUi(UserCommand.Up);
						else if(RightPresses==1)
							setUi(UserCommand.Right);
						else if (DownPresses==1)
							setUi(UserCommand.Down);
						else if (LeftPresses==1)
							setUi(UserCommand.Left);						
					}					
					setChanged();
					notifyObservers();	
				}
			});	
			
		//mouse listener	
		getDisplay().addFilter(SWT.MouseDown, new Listener() {
			int startX=0;
			int startY=0;
			Boolean flag=true;
			
			@Override
			public void handleEvent(Event e) {
				startX=e.x;
				startY=e.y;
				getDisplay().addFilter(SWT.MouseUp, new Listener() {
					@Override
					public void handleEvent(Event e) {
						int endX = e.x;
						int endY = e.y;
						System.out.println("-----------------");
						System.out.println("startX value: "+startX+" endX: "+endX);
						System.out.println("startY value: "+startY+" endY: "+endY);
						System.out.println("-----------------");
						if (endX == startX || endY == startY) { //handle horizontal and vertical
							if (endX == startX) {
								if(endY>startY)
									setUi(UserCommand.Down);
								else if(endY<startY)
									setUi(UserCommand.Up);
							} else if (endY == startY) {
								if(endX>startX)
									setUi(UserCommand.Right);
								else if(endX<startX)
									setUi(UserCommand.Left);
							}							 
						} else if(endX<startX){ //handle diagonal direction
							if(endY>startY)
								setUi(UserCommand.DownLeft);
							else
								setUi(UserCommand.UpLeft);
						} else if(endX>startX) {
							if (endY>startY)
							 	setUi(UserCommand.DownRight);
						 	else
						 		setUi(UserCommand.UpRight);
						}
					
						if((endX!=startX||endY!=startY)&&flag==true){
							flag=false;
							setChanged();
							notifyObservers();	
						}
					}			 
				});
			 flag=true;	
			}		
		});
		} else
			board.updateBoard(data);
	
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
