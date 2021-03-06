package view.game2048;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import common.UserCommand;
import view.AbsView;
import view.Board;
import view.Tile;

/**
 * The Class Game2048View.
 */
public class Game2048View extends AbsView {
	
	/** The board. */
	Board board;
	
	/** The data. */
	int [][] data;
	
	/**
	 * Instantiates a new game2048 view.
	 *
	 * @param string the string
	 */
	public Game2048View(String string) {
		super(string);
	}
		
	//display the board
	/* (non-Javadoc)
	 * @see view.AbsView#displayBoard(int[][])
	 */
	@Override
	public void displayBoard(int[][] nData) {
		data = nData;
		getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				if (board == null) {
					board = new Board(getGameBoard(), SWT.WRAP, data);
					board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,2));
					Color boardColor = new Color(getDisplay(), 187, 173, 160); //set board color
					board.setBackground(boardColor);
					board.setForeground(boardColor);
					getShell().setMinimumSize(800, 800);
					InitInput();
				} else {
					board.updateBoard(data);
				}				
			}
		});

	}
	

	/**
	 * Inits the input from user.
	 */
	public void InitInput () {		
		getDisplay().addFilter(SWT.KeyUp, new Listener() {		
			@Override
			public void handleEvent(Event e) {
				switch (e.keyCode) {
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
					default:
						return;
				}	
					if ((e.keyCode == SWT.ARROW_UP) || (e.keyCode == SWT.ARROW_DOWN) || (e.keyCode == SWT.ARROW_LEFT) || (e.keyCode == SWT.ARROW_RIGHT)) {
						board.setFocus();
						setChanged();
						notifyObservers();
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
						if (e.widget instanceof Tile) { //to make sure mouse only works inside the board
							int endX = e.x;
							int endY = e.y;
			            	int diffX=Math.abs(endX-startX);
			            	int diffY=Math.abs(endY-startY);
			            	if (diffY>diffX) {
			            		if(endY>startY)
			            			setUi(UserCommand.Down);
			            		else if(endY<startY)
									setUi(UserCommand.Up);
			            	} else if (diffX>diffY) {
			            			if(endX>startX)
										setUi(UserCommand.Right);
									else if(endX<startX)
										setUi(UserCommand.Left);
							}			 
			            	if((endX!=startX || endY!=startY) && flag){
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
			if (isKillThread()) { //used to switch games DO NOT REMOVE ANYTHING!
				board = null;
				setKillThread(false);
				return;
			}
			//while there are no events (this is the event handler)
			if(!getDisplay().readAndDispatch()) {
				//the OS will auto wake the display on EVENT (mouse, keyboard, etc).
				if (board == null) { //for the game start
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
