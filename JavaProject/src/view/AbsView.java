package view;

import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import controller.UserCommand;


//TODO: set win+loose screen
//TODO: re-use for load / save etc

/*
 * Abstract Class AbsView:
 * This class defines the common parts for classes that run a 2D board game.
 */

public abstract class AbsView extends Observable implements View,Runnable  {
	UserCommand ui; //user command ENUM
	//SWT Components:
	Display display; 
	Shell shell; 
	Menu menuBar;
	Group gameButtons;
	Group gameBoard;
	//GridLayout gridLayout;
	Label scoreLabel;
	boolean flag;//for set win&lose window
	/*
	 * AbsView constructor:
	 * init upper bar
	 * init game buttons
	 * init the group which holds the game board.
	 */

	public AbsView(String string) {
		display=new Display();
		shell=new Shell(display);	
		shell.setText(string);//set the game name
		shell.setLayout(new GridLayout(2, false));
		shell.setSize(600,600);
		shell.setMinimumSize(600, 600);
		flag=false;
		setMenuToolsBar(); //draw the menu tool bar.
		gameButtonsMenu(); //draw the game buttons
		gameBoardGroup();  //draw the board where the game will be placed.		
		shell.open();		
	}
	
	
	/* ************************
	 * Setters & Getters:
	 * ************************/

	public UserCommand getUi() {
		return ui;
	}

	public void setUi(UserCommand ui) {
		this.ui = ui;
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public Shell getShell() {
		return shell;
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}

	public Menu getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(Menu menuBar) {
		this.menuBar = menuBar;
	}


	public Label getScoreLabel() {
		return scoreLabel;
	}

	public void setScoreLabel(Label scoreLabel) {
		this.scoreLabel = scoreLabel;
	}
	
	public Group getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(Group gameBoard) {
		this.gameBoard = gameBoard;
	}
	
		
	/* ************************
	 * Upper menu bar:
	 * ************************/
		
	
	//Menu bar (upper menu):
	
	protected void setMenuToolsBar() {
		//create the menu bar and add file and edit
		this.menuBar=new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		setFile(); //create the file tools bar
		setEdit(); //create the edit tools bar		
	}

	//File drop down menu
	private void setFile() {	
		MenuItem aFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        aFileMenu.setText("File"); 
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        aFileMenu.setMenu(fileMenu);
        loadGame(fileMenu); //Load game option
        saveGame(fileMenu); //Sage game option
        saveAndExitGameOption(fileMenu); //Save & Exit game option.
		exitGameOption(fileMenu); //Exit game option.
	}

	//Edit drop down menu.
	private void setEdit() {
		MenuItem aEditMenu = new MenuItem(menuBar, SWT.CASCADE);
		aEditMenu.setText("Edit");
	    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    aEditMenu.setMenu(fileMenu);
	    restartGameDropMenu(fileMenu); //Restart game option
	    undoMove(fileMenu); //Edit game option	       
	}

	/* ************************
	 * File drop down menu:
	 * ************************/	
	
	
	//Save game (under the File drop down menu)
	private void saveGame(Menu fileMenu) {
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
	    newItem.setText("Save game");	    
	    newItem.addSelectionListener(new SelectionAdapter() {
	    	
		    @Override
	        public void widgetSelected(SelectionEvent e) {
	        	ui = UserCommand.SaveGame;
				setChanged();
				notifyObservers(); 
				
		    }
	    });			
	}

	//Load game (under the File drop down menu)
	private void loadGame(Menu fileMenu) {
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
	    newItem.setText("Load game");	
	    newItem.addSelectionListener(new SelectionAdapter() {
	
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		    	ui = UserCommand.LoadGame;
				setChanged();
				notifyObservers();    
	    }
	});
		
	}

	//Save & Exit (under the File drop down menu)
	private void saveAndExitGameOption(Menu fileMenu) {
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
        newItem.setText("Save and Exit");
        newItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
            	ui = UserCommand.SaveGame;
    			setChanged();
    			notifyObservers(); 
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });
		
	}
	
	//Exit Game (under the File drop down menu)
	private void exitGameOption(Menu fileMenu) {
	 MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText("Exit game");
        exitItem.addSelectionListener(new SelectionAdapter() {
        	
        	@Override
            public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });
		
	}

	/* ************************
	 * Edit drop down menu:
	 * ************************/	
	
	//Restart Game (under the Edit drop down menu)
	private void restartGameDropMenu(Menu fileMenu) {
	
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
	    newItem.setText("Restart Game");		    
	    newItem.addSelectionListener(new SelectionAdapter() {
	    	
	    	@Override
	        public void widgetSelected(SelectionEvent e) {
	        	restartGame();
	        }
	    });
	 }
	
	public void restartGame() {
    	ui = UserCommand.RestartGame;
		setChanged();
		notifyObservers();
	}
	
	

	//Undo Move (under the Edit drop down menu)
	private void undoMove(Menu fileMenu) {
	
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
	    newItem.setText("Undo Move");
	    newItem.addSelectionListener(new SelectionAdapter() {
	
	        @Override
	        public void widgetSelected(SelectionEvent e) {
	        	ui = UserCommand.UndoMove;
				setChanged();
				notifyObservers();   
	        }
	    });
			
	}
	

	/* ************************
	 * Game buttons group
	 * ************************/	
	
	protected void gameButtonsMenu() {
		
		//game buttons group
		gameButtons = new Group(shell, SWT.SHADOW_IN); //check other shadow options
		gameButtons.setText ("Game Menu");
		gameButtons.setLayout(new GridLayout(1, true));
		gameButtons.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1,1));
		
		//score label
		this.scoreLabel = new Label (gameButtons,SWT.NONE);
		scoreLabel.setLayoutData(new GridData(SWT.FILL,  SWT.TOP, false, false, 1, 1));
		scoreLabel.setText ("Score: "); //Score label.	     
	    
	    //init menu buttons:
		menuUndoMoveButton();
		menuRestartButton();
		menuLoadGameButton();
		menuSaveGameButton();		
	}
	
	


	//Undo button (under buttons menu)
	private void menuUndoMoveButton() {
		Button undoMove = new Button(gameButtons, SWT.PUSH);
		undoMove.setText("Undo Move");
		undoMove.setLayoutData(new GridData(SWT.FILL,  SWT.TOP, false, false, 1, 1));
		undoMove.addSelectionListener(new SelectionListener() {
		
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ui = UserCommand.UndoMove;
				setChanged();
				notifyObservers();   		
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}			

		});
	}
	
	//Restart button (under buttons menu)
	private void menuRestartButton() {
		Button restartGame = new Button(gameButtons, SWT.PUSH);
		restartGame.setText("Restart Game");
		restartGame.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		restartGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				restartGame();     		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
	}
	
	
	//Save Game button (under buttons menu)
	//TODO: add file select
	private void menuSaveGameButton() {		
		Button saveGame = new Button(gameButtons, SWT.PUSH);
		saveGame.setText("Save Game");
		saveGame.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		saveGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ui = UserCommand.SaveGame;
				setChanged();
				notifyObservers();     	
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});		
	}

	//Load Game button (under buttons menu)
	//TODO: add file select
	private void menuLoadGameButton() {
		Button loadGame = new Button(gameButtons, SWT.PUSH);
		loadGame.setText("Load Game");
		loadGame.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		loadGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ui = UserCommand.LoadGame;
				setChanged();
				notifyObservers(); 		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {	
			}
		});

	}
	
	/* ************************
	 * Game board group
	 * ************************/
	
	private void gameBoardGroup () { 
		gameBoard = new Group(shell, SWT.SHADOW_OUT);		
		gameBoard.setLayout(new GridLayout(1, true));
		gameBoard.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		//Color color = new Color(shell.getDisplay(), 250, 248, 239);
	    //gameBoard.setBackground(color);
	}
	
	
	/*
	 * 			// Group which wrap game board components
		    boardGroup = new Group(shell, SWT.SHADOW_OUT);
		    boardGroup.setLayout(new GridLayout(2, true));
		    boardGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		    Color shellColor =  new Color(shell.getDisplay(), 250, 248, 239);
		    boardGroup.setBackground(shellColor);
	 */
	

	/* ************************
	 * Other Methods:
	 * ************************/	
	
	@Override
	public void setLose(boolean lose) {
		if(lose == true&&flag==false){
			flag=true;
			final Shell loseWindow= new Shell(this.shell);
			loseWindow.setLayout(new GridLayout(2, false));
			
			loseWindow.setSize(100, 120);
			loseWindow.setText("loser");
			Label winMsg = new Label (loseWindow,SWT.NONE);
			winMsg.setLayoutData(new GridData(SWT.CENTER,  SWT.UP, true, true, 2, 2));
			winMsg.setText ("you lose");	
			Label reStart = new Label (loseWindow,SWT.NONE);
			reStart.setLayoutData(new GridData(SWT.CENTER,  SWT.FILL, true, true, 2, 2));
			reStart.setText ("start new game?");
			
			Button yesButton=new Button(loseWindow,SWT.PUSH);
			yesButton.setText("yes");
			yesButton.setLayoutData(new GridData(SWT.FILL, SWT.RIGHT, true, true,1, 1));
			
			yesButton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					ui = UserCommand.RestartGame;
					setChanged();
					notifyObservers();
					flag=false;
					loseWindow.close(); 		
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});
			Button noButton=new Button(loseWindow,SWT.PUSH);
			noButton.setText("no");
			noButton.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, true,1, 1));
			
			noButton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					loseWindow.close(); 		
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});
			
			loseWindow.open();
		}
		
	}

	@Override
	public void setWin(boolean win) {
		if(win == true&&flag==false){
			flag=true;
			final Shell winWindow= new Shell(this.shell);
			winWindow.setLayout(new GridLayout(2, false));
			
			winWindow.setSize(100, 120);
			winWindow.setText("winner");
			Label winMsg = new Label (winWindow,SWT.NONE);
			winMsg.setLayoutData(new GridData(SWT.CENTER,  SWT.UP, true, true, 2, 2));
			winMsg.setText ("you won");	
			Label reStart = new Label (winWindow,SWT.NONE);
			reStart.setLayoutData(new GridData(SWT.CENTER,  SWT.FILL, true, true, 2, 2));
			reStart.setText ("start new game?");
			
			Button yesButton=new Button(winWindow,SWT.PUSH);
			yesButton.setText("yes");
			yesButton.setLayoutData(new GridData(SWT.FILL, SWT.RIGHT, true, true,1, 1));
			
			yesButton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					ui = UserCommand.RestartGame;
					setChanged();
					notifyObservers();
					flag=false;
					winWindow.close(); 		
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});
			Button noButton=new Button(winWindow,SWT.PUSH);
			noButton.setText("no");
			noButton.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, true,1, 1));
			
			noButton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					winWindow.close(); 		
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {}
			});			
			
			winWindow.open();
		}
				
	}	
	
	@Override
	public abstract void displayBoard(int[][] data);

	@Override
	public abstract UserCommand getUserCommand();

	@Override
	public void displayScore(int score) {
		 scoreLabel.setText (scoreLabel.getText() + score);		
	}

}


/*
 * GridLayout
	
	public GridLayout(int numColumns, boolean makeColumnsEqualWidth)
	Constructs a new instance of this class given the number of columns, and whether or not 
	the columns should be forced to have the same width. If numColumns has a value less than 1, 
	the layout will not set the size and position of any controls.
	Parameters:
	numColumns - the number of columns in the grid
	makeColumnsEqualWidth - whether or not the columns will have equal width
	Since:
	2.0
 */

/* 
 * public GridData(int horizontalAlignment,
            int verticalAlignment,
            boolean grabExcessHorizontalSpace,
            boolean grabExcessVerticalSpace,
            int horizontalSpan,
            int verticalSpan)
	Constructs a new instance of GridData according to the parameters.
	Parameters:
	horizontalAlignment - how control will be positioned horizontally within a cell, one of: 
	SWT.BEGINNING (or SWT.LEFT), SWT.CENTER, SWT.END (or SWT.RIGHT), or SWT.FILL
	verticalAlignment - how control will be positioned vertically within a cell, 
	one of: SWT.BEGINNING (or SWT.TOP), SWT.CENTER, SWT.END (or SWT.BOTTOM), or SWT.FILL
	grabExcessHorizontalSpace - whether cell will be made wide enough to fit the remaining horizontal space
	grabExcessVerticalSpace - whether cell will be made high enough to fit the remaining vertical space
	horizontalSpan - the number of column cells that the control will take up
	verticalSpan - the number of row cells that the control will take up
	Since:
	3.0
 */



