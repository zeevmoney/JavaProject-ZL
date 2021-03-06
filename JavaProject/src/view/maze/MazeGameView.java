package view.maze;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import view.AbsView;
import view.Board;
import view.Tile;

import common.UserCommand;

/**
 * The Class MazeGameView.
 */
public class MazeGameView extends AbsView implements Runnable {
	
	/** The board. */
	Board board;
	
	/** The vertical. - -1 for up, 0 for nothing, 1 for down   */
	private int vertical;
	
	/** The horizontal. - -1 for left, 0 for nothing, 1 for right  */
	private int horizontal;
	
	/** The pressed count. */
	private int pressedCount;
	
	/** The change. */
	private boolean change;
	
	/** The data. */
	private int[][] data;
	
	/** The key up. - used to control a keyUP event in order to know if an arrow key was pressed. */
	boolean keyUp = false;

	/**
	 * Instantiates a new maze game view.
	 *
	 * @param string the string
	 */
	public MazeGameView(String string) {
		super(string);
	}
	
	/**
	 * Inits the vars.
	 */
	public void InitVars () {
		vertical=0;
		horizontal=0;
		pressedCount=0;
		//board.setFocus();
	}
	
	//display the board
	/* (non-Javadoc)
	 * @see view.AbsView#displayBoard(int[][])
	 */
	@Override
	public void displayBoard(int[][] nData) {
		data = nData;
		getDisplay().syncExec(new Runnable() {
			
			@Override
			public void run() {
				if (isNewGame() && board!=null) {
					board.setFocus();
				}
				if (board == null) { //first init
					//board init
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
		});
	}
			
	/**
	 * Inits the input from user.
	 */
	public void InitInput () { //init all key listeners and mouse listeners.
		
		getDisplay().addFilter(SWT.KeyUp, new Listener()
	    {
	        @Override
	        public void handleEvent(Event e)
	        {
	        	//board.setFocus();
	        	change=true;
	        	
            	if (horizontal < 0 && vertical < 0) {   	
            		setUi(UserCommand.UpLeft); 
            		pressedCount--;
            	} else if(horizontal < 0 && vertical > 0) { 
            		setUi(UserCommand.DownLeft); 
            		pressedCount--;
            	}
            	else if(horizontal > 0 && vertical < 0) { 	
            		setUi(UserCommand.UpRight); 
            		pressedCount--;
            	} else if(horizontal > 0 && vertical > 0) {             		
            		setUi(UserCommand.DownRight); 
            		pressedCount--;
            	} else if(horizontal > 0 && vertical == 0 && pressedCount==1) 
            		setUi(UserCommand.Right);
            	else if(horizontal < 0 && vertical == 0 && pressedCount==1)
            		setUi(UserCommand.Left);
            	else if(horizontal == 0 && vertical < 0 && pressedCount==1)
            		setUi(UserCommand.Up);
            	else if(horizontal == 0 && vertical > 0 && pressedCount==1) 
            		setUi(UserCommand.Down);
            	else
            		change=false;
            	
            	switch (e.keyCode) {
					case (SWT.ARROW_UP):
	            		vertical++;
						keyUp=true;
						break;
					case (SWT.ARROW_DOWN):
					    vertical--;
						keyUp=true;
						break;
					case (SWT.ARROW_LEFT):
						horizontal++;
						keyUp=true;
						break;
					case (SWT.ARROW_RIGHT):
						horizontal--;
						keyUp=true;
						break;
				}
            	
            	if (keyUp && change) { //keyUP = arrow key, change = setUi was set.
            		board.setFocus();
            		pressedCount--;								
					setChanged();
					notifyObservers();					
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
	        		
	        	if (pressedCount<0 || pressedCount>2) //don't change this if!
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
						if (e.widget instanceof Tile) {
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
					}			 
				});
			 flag=true;	
			}		
		});
	}

	
	/* (non-Javadoc)
	 * @see view.AbsView#getUserCommand()
	 */
	@Override
	public UserCommand getUserCommand() {
		return getUi();
	}	

	//the GUI and main loop thread should be in the same THREAD.
	/* (non-Javadoc)
	 * @see view.AbsView#run()
	 */
	@Override
	public void run() {
		initComponents(); //init the game board using the main GUI thread (the function is in the AbsView Class)		
		//while shell is alive
		while (!getShell().isDisposed()) {
			if (isKillThread()) {  //used to switch games
				board = null;
				setKillThread(false);
				return;
			}
			//while there are no events (this is the event handler)
			if(!getDisplay().readAndDispatch()) {
				//the OS will auto wake the display on EVENT (mouse, keyboard, etc).
				if (board == null) {
					setUi(UserCommand.NewGame);
					setChanged();
					notifyObservers();
				}
				getDisplay().sleep();
			}
		}
		//if shell dies -> display dies.
		getDisplay().dispose();			
	}



}