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
	private int vertical; // -1 for up, 0 for nothing, 1 for down  
	private int horizontal; // -1 for left, 0 for nothing, 1 for right 
	private int pressedCount;
	private boolean change;

	
	public MazeGameView(String string) {
		super(string);
	}
	
	public void InitVars () {
		vertical=0;
		horizontal=0;
		pressedCount=0;
		board.setFocus();
	}
	
	//display the board
	@Override
	public void displayBoard(int[][] data) {
		if (isNewGame() && board!=null) {
			board.setFocus();
		}
		if (board == null) { //first init
			//TODO: code cleanup.
			board = new Board(getGameBoard(), SWT.WRAP, data);
			board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,2));
			Color boardColor = new Color(getDisplay(), 0, 0, 0); //set board color
			board.setBackground(boardColor);
			board.setForeground(boardColor);
			InitInput();
			getShell().setMinimumSize(800, 800);			
			board.setFocus();
		} else {
			board.updateBoard(data);
		}
	}
			
	
	public void InitInput () { //init all key listeners and mouse listeners.
		
		getDisplay().addFilter(SWT.KeyUp, new Listener()
	    {
	        @Override
	        public void handleEvent(Event e)
	        {
	        	board.setFocus();
	        	change=true;
	        	
	            //horizontal: -1:left 0:nothing 1:right
            	//vertical:    -1:up   0:nothing 1:right
            	if (horizontal < 0 && vertical < 0) { // up and left  	
            		setUi(UserCommand.UpLeft); 
            		pressedCount--;
            	} else if(horizontal < 0 && vertical > 0) { //down and left
            		setUi(UserCommand.DownLeft); 
            		pressedCount--;
            	}
            	else if(horizontal > 0 && vertical < 0) { //up and right	
            		setUi(UserCommand.UpRight); 
            		pressedCount--;
            	} else if(horizontal > 0 && vertical > 0) { //down and right            		
            		setUi(UserCommand.DownRight); pressedCount--;
            	} else if(horizontal > 0 && vertical == 0 && pressedCount==1) //right
            		setUi(UserCommand.Right);
            	else if(horizontal < 0 && vertical == 0 && pressedCount==1) //left
            		setUi(UserCommand.Left);
            	else if(horizontal == 0 && vertical < 0 && pressedCount==1) //up
            		setUi(UserCommand.Up);
            	else if(horizontal == 0 && vertical > 0 && pressedCount==1) //down
            		setUi(UserCommand.Down);
            	else
            		change=false;
            		            
            	switch (e.keyCode) {
					
	            	case (SWT.ARROW_UP):
	            		vertical++;
	            		break;
					case (SWT.ARROW_DOWN):
					    vertical--;
						break;
					case (SWT.ARROW_LEFT):
						horizontal++;
						break;
					case (SWT.ARROW_RIGHT):
						horizontal--;
						break;
					default:
						break;
				}
            	if ((e.keyCode == SWT.ARROW_UP)
							|| (e.keyCode == SWT.ARROW_DOWN)
							|| (e.keyCode == SWT.ARROW_LEFT)
							|| (e.keyCode == SWT.ARROW_RIGHT) ) {
						
						if(change)
						{
							pressedCount--;								
							setChanged();
							notifyObservers();
						}
					}
	           
	        }
	    });
			
		//key down register.
		getDisplay().addFilter(SWT.KeyDown, new Listener()
	    {
			
			@Override
	        public void handleEvent(Event e)
	        {
				
				if (isNewGame()) { //if a restart was made (the var is in AbsView)
					InitVars();
					setNewGame(false);
				}				
	        		
	        	if (pressedCount<0 || pressedCount>2)
	            		pressedCount=0;
            	switch (e.keyCode) {
	            	case (SWT.ARROW_UP):
	            		vertical--;	
	            		pressedCount++;
	            		break;
					case (SWT.ARROW_DOWN):
					    vertical++;
						pressedCount++;
						break;
					case (SWT.ARROW_LEFT):
						horizontal--;
						pressedCount++;
						break;
					case (SWT.ARROW_RIGHT):
						horizontal++;
						pressedCount++;
						break;
				}		            
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
						if (endX == startX || endY == startY) { //handle horizontal and vertical
							if (endX == startX) {
								if(endY > startY)
									setUi(UserCommand.Down);
								else if(endY<startY)
									setUi(UserCommand.Up);
							} else if (endY == startY) {
								if(endX>startX)
									setUi(UserCommand.Right);
								else if(endX<startX)
									setUi(UserCommand.Left);
							}							 
						} else if(endX < startX){ //handle diagonal direction
							if(endY > startY)
								setUi(UserCommand.DownLeft);
							else
								setUi(UserCommand.UpLeft);
						} else if(endX > startX) {
							if (endY > startY)
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
	
	
	
	
	
	
//	getDisplay().addFilter(SWT.KeyDown, new Listener() {
//		//check if a key was pressed
//		@Override
//		public void handleEvent(Event e) { 
//			board.setFocus();
//			switch (e.keyCode){
//				case SWT.ARROW_DOWN:
//					DownPresses=true;
//				//	Pressed=true;
//					break;
//				case SWT.ARROW_LEFT:
//					LeftPresses=true;
//				//	Pressed=true;
//					break;
//				case SWT.ARROW_RIGHT:
//					RightPresses=true;
//					//Pressed=true;
//					break;
//				case SWT.ARROW_UP:
//					UpPresses=true;
//					//Pressed=true;
//					break;				     
//			}
//		}
//	});
//	
//	getDisplay().addFilter(SWT.KeyUp, new Listener() {
//		
//		@Override
//		public void handleEvent(Event e) { 
//			
//			board.setFocus();
//			//check if a key was released
//			//if the same key was released it's a horiznotal/vertical move
//			switch (e.keyCode){
//				case SWT.ARROW_DOWN:
//					if (DownPresses=true) {
//						setUi(UserCommand.Down);
//						Pressed=true;
//					}
//				    break;
//				case SWT.ARROW_LEFT:
//					if (LeftPresses=true) {
//						setUi(UserCommand.Left);
//						Pressed=true;
//					}					
//				    break;
//				case SWT.ARROW_RIGHT:
//					if (RightPresses=true) {
//						Pressed=true;
//						setUi(UserCommand.Right);
//					};						
//				    break;
//				case SWT.ARROW_UP:
//					if (UpPresses=true) {
//						Pressed=true;
//						setUi(UserCommand.Up);
//					}							
//				    break;
//		}
//			
//				if(UpPresses && LeftPresses) {
//						setUi(UserCommand.UpLeft);
//						Pressed=true;					
//				} else if(UpPresses && RightPresses) {
//						setUi(UserCommand.UpRight);
//						Pressed=true;						
//				} else if (DownPresses && RightPresses) {							
//						setUi(UserCommand.DownRight);
//						Pressed=true;
//				} else if (DownPresses && LeftPresses) {
//						Pressed=true;
//						setUi(UserCommand.DownLeft);
//				}
//			 else if (Pressed) {
//				setKey();
//				setChanged();
//				notifyObservers();
//			}
//		
//		}
//	});





//getDisplay().addFilter(SWT.KeyDown, new Listener() {		
//@Override
//public void handleEvent(Event e) { 
//	board.setFocus();
//	switch (e.keyCode){
//		case SWT.ARROW_DOWN:
//			setUi(UserCommand.Down); 
//		    break;
//		case SWT.ARROW_LEFT:
//			setUi(UserCommand.Left);
//			
//			break;
//		case SWT.ARROW_RIGHT:
//			setUi(UserCommand.Right);
//			
//			break;
//		case SWT.ARROW_UP:
//			setUi(UserCommand.Up);
//			break;				     
//		}	
//		setChanged();
//		notifyObservers();	
//}
//});	

