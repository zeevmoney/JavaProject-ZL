package view;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.algoirthms.ObjectXML;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import controller.UserCommand;

/*
 * Abstract Class AbsView:
 * This class defines the common parts for classes that run a 2D board game.
 */

//TODO: fix arrows on combo boxes.

public abstract class AbsView extends Observable implements View,Runnable  {
	UserCommand ui; //user command ENUM
	//SWT Components:
	Display display; 
	Shell shell; 
	Menu menuBar;
	Group gameButtons;
	Group gameBoard;
	Label scoreLabel;
	Combo serverCombo; //server combo box
	ArrayList<String> serverList; //server list after import
	String gameName; //game name String.
	InetSocketAddress socketAddress; //to hold the server address
	Button connectButton; //connect to server
	Button getHintButton; //get hint from server
	Text movesNumber;
	Text treeSize;
	boolean newGame=false; //used to init some vars in maze game.
	boolean killThread=false; //used to kill the current running gui thread (for switching games)
	
	/*
	 * AbsView constructor:
	 * init upper bar
	 * init game buttons
	 * init the group which holds the game board.
	 */
	public AbsView(String string) {
		gameName = string;
		serverList = new ArrayList<String>();
	}
	
	public void initComponents () { //init everything
		killThread = false;
		display=new Display();
		shell=new Shell(display);	
		shell.setText(gameName);//set the game name
		shell.setLayout(new GridLayout(2, false));
		shell.setSize(799,799);
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
	
	public boolean isNewGame() {
		return newGame;
	}

	public void setNewGame(boolean newGame) {
		this.newGame = newGame;
	}
		
	/* ************************
	 * Upper menu bar:
	 * ************************/		
	
	//Menu bar (upper menu):
	private void setMenuToolsBar() {
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
//        new2048Game(fileMenu);
//        newMazeGame(fileMenu);
        newGame(fileMenu);
        loadGame(fileMenu); //Load game option
        saveGame(fileMenu); //Sage game option
        saveAndExitGameOption(fileMenu); //Save & Exit game option.
		exitGameOption(fileMenu); //Exit game option.
	}


	//Edit drop down menu.
	private void setEdit() {
		MenuItem aEditMenu = new MenuItem(menuBar, SWT.CASCADE);
		aEditMenu.setText("Edit");
	    Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
	    aEditMenu.setMenu(editMenu);
	    restartGameDropMenu(editMenu); //Restart game option
	    undoMoveDropDown(editMenu); //Edit game option	       
	}

	/* ************************
	 * File drop down menu:
	 * ************************/		
	
	private void newGame(Menu fileMenu) {
		MenuItem newGame = new MenuItem(fileMenu, SWT.CASCADE);
		newGame.setText("New Game");
		Menu newGameSubMenu = new Menu(shell, SWT.DROP_DOWN);
		newGame.setMenu(newGameSubMenu);
        new2048Game(newGameSubMenu);
        newMazeGame(newGameSubMenu);
	}	
	
	private void new2048Game(Menu menu) {
		MenuItem new2048Game = new MenuItem(menu, SWT.NONE);
		new2048Game.setText("2048");
		
		new2048Game.addSelectionListener(new SelectionAdapter() {
		    
			@Override
	        public void widgetSelected(SelectionEvent e) {
				if (gameName.contains("2048")) {
					restartGame();
				} else {
					ui = UserCommand.SwitchGame;
					setChanged();
					notifyObservers();			
				}
		    }		
		});
	}
	
	
	private void newMazeGame(Menu menu) {
		MenuItem new2048Game = new MenuItem(menu, SWT.NONE);
		new2048Game.setText("Maze");
		
		new2048Game.addSelectionListener(new SelectionAdapter() {
		    
			@Override
	        public void widgetSelected(SelectionEvent e) {
				if (gameName.contains("Maze")) {
					restartGame();
				} else {
					ui = UserCommand.SwitchGame;
					setChanged();
					notifyObservers();
				}		
		    }		
		});		
	}	
	
	//Save game (under the File drop down menu)
	private void saveGame(Menu fileMenu) {
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
	    newItem.setText("Save game");	    
	    newItem.addSelectionListener(new SelectionAdapter() {
	    	
		    @Override
	        public void widgetSelected(SelectionEvent e) {
		    	saveGame();				
		    }
	    });			
	}
	
	private void saveGame() { //save game function
		FileDialog fd = new FileDialog(shell, SWT.SAVE);
		fd.setText("Save Game");
		fd.setFilterPath("resources\\");
		String[] filterExt = { "*.xml" };
		fd.setFilterExtensions(filterExt);
		String fileName = fd.open(); // Store selected file name as string
		if (fileName != null) {
			ui = UserCommand.SaveGame;
			setChanged();
			notifyObservers(fileName);
		}   	
	}

	//Load game (under the File drop down menu)
	private void loadGame(Menu fileMenu) {
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
	    newItem.setText("Load game");	
	    newItem.addSelectionListener(new SelectionAdapter() {
	
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		    	loadGame();   
		    }
	    });		
	}
	
	private void loadGame () { //load game function
		FileDialog fd = new FileDialog(shell, SWT.OPEN);
		fd.setText("Load Game");
		fd.setFilterPath("resouces\\");
		String[] filterExt = { "*.xml" };
		fd.setFilterExtensions(filterExt);
		String fileName = fd.open();
		if (fileName != null) {
			newGame=true;
			ui = UserCommand.LoadGame;
			setChanged();
			notifyObservers(fileName);
		}   
	}

	//Save & Exit (under the File drop down menu)
	private void saveAndExitGameOption(Menu fileMenu) {
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
        newItem.setText("Save and Exit");
        newItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
            	saveGame();
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
	
	private void restartGame() { //restart game main function
		newGame=true;
    	ui = UserCommand.RestartGame;
    	setChanged();
		notifyObservers();
	}
	

	//Undo Move (under the Edit drop down menu)
	private void undoMoveDropDown(Menu fileMenu) {
	
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
	    newItem.setText("Undo Move");
	    newItem.addSelectionListener(new SelectionAdapter() {
	
	        @Override
	        public void widgetSelected(SelectionEvent e) {
	        	undoMove();
	        }
	    });
			
	}
	
	private void undoMove() { //undoMove main function
		newGame=true;
    	ui = UserCommand.UndoMove;
		setChanged();
		notifyObservers();
	}	

	/* ************************
	 * Game buttons group
	 * ************************/	
	
	protected void gameButtonsMenu() {
		
		//game buttons group
		gameButtons = new Group(shell, SWT.SHADOW_IN);
		gameButtons.setText ("Game Menu");
		gameButtons.setLayout(new GridLayout(1, true));
		gameButtons.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1,1));
		
		//score label
		this.scoreLabel = new Label (gameButtons,SWT.NONE);
		scoreLabel.setLayoutData(new GridData(SWT.FILL,  SWT.LEFT, false, false, 1, 1));
		scoreLabel.setText ("Score: "); //Score label.	     
	    
	    //init menu buttons:
		menuUndoMoveButton();
		menuRestartButton();
		menuLoadGameButton();
		menuSaveGameButton();	
		menuGameServer();
		menuHint();
	}
	
	
	//Undo button (under buttons menu)
	private void menuUndoMoveButton() {
		Button undoMove = new Button(gameButtons, SWT.PUSH);
		undoMove.setText("Undo Move");
		undoMove.setLayoutData(new GridData(SWT.FILL,  SWT.LEFT, false, false, 1, 1));
		undoMove.addSelectionListener(new SelectionListener() {
		
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				undoMove();  		
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}			

		});
	}
	
	//Restart button (under buttons menu)
	private void menuRestartButton() {
		Button restartGame = new Button(gameButtons, SWT.PUSH);
		restartGame.setText("Restart Game");
		restartGame.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, false, false, 1, 1));
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
	private void menuSaveGameButton() {		
		Button saveGame = new Button(gameButtons, SWT.PUSH);
		saveGame.setText("Save Game");
		saveGame.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		saveGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				saveGame();    	
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});		
	}

	//Load Game button (under buttons menu)
	private void menuLoadGameButton() {
		Button loadGame = new Button(gameButtons, SWT.PUSH);
		loadGame.setText("Load Game");
		loadGame.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		loadGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				loadGame(); 		
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
	}
	
	
	/* ************************
	 * Server Control
	 * ************************/
	
	@SuppressWarnings("unchecked")
	private void menuGameServer() {
		
		//Seperator
		Label seperator = new Label(gameButtons, SWT.SEPARATOR | SWT.HORIZONTAL);
		seperator.setLayoutData(new GridData(SWT.FILL,  SWT.TOP, false, false, 1, 1));
		
		Label serverLabel = new Label(gameButtons, SWT.NONE);
		serverLabel.setText("Enter a server address:");
		
		//Server Add/Select combo box.
		serverCombo = new Combo(gameButtons, SWT.FILL);
		serverCombo.setToolTipText("Syntax: <Server>:<Port>.\nServers are added automaticly on connect\nor by pressing <Enter>");
		//Dynamic load of selections		
		try {
			serverList = (ArrayList<String>) ObjectXML.ObjectFromXML("resources/Ips.xml");
			String[] tempList = new String[serverList.size()];
			tempList = serverList.toArray(tempList);
			serverCombo.setItems(tempList);
			serverCombo.setText("127.0.0.1:1337");
		} catch (Exception e) {
			System.out.println("File Doesn't Exist!");
			e.printStackTrace();
		}
		
		serverCombo.addTraverseListener(new TraverseListener() { //enter key is pressed
			
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) { //enter was pressed
					String newServer = serverCombo.getText();
					addserver(newServer);
				}
			}		
		
		});
		
		//Connect / Disconnect button. Valid server will be automatically added to the combo + file.
		connectButton = new Button(gameButtons, SWT.PUSH);
		connectButton.setText("Connect");
		connectButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		connectButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (connectButton.getText() == "Connect"){
					String temp = serverCombo.getText();
					if ((temp = addserver(temp))!= null) { //after this command temp holds an ip:port (after DNS check)
						ui = UserCommand.Connect;
						System.out.println("DEBUG: Connecting... "+serverCombo.getText());
						connectButton.setText("Connecting...");
						connectButton.setEnabled(false);
						setChanged();
						notifyObservers(temp);
					}
				} else {
					ui = UserCommand.Disconnect;
					setChanged();
					notifyObservers();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {	
			}
		});	
		
		Label seperator2 = new Label(gameButtons, SWT.SEPARATOR | SWT.HORIZONTAL);
		seperator2.setLayoutData(new GridData(SWT.FILL,  SWT.TOP, false, false, 1, 1));
	
	}
	
	//add a new server to file + combo.
	private String addserver (String newServer) { 
		if ((newServer = isValidIP(newServer)) != null) { //if a valid server:port were entered
			if (serverList.contains(newServer)) {
				System.out.println("DEBUG: Server already exists: " + newServer);
				return newServer;
			} else {
				try {
					serverList.add(newServer);
					serverCombo.add(newServer);
					ObjectXML.objectToXML(serverList, "resources/Ips.xml");
					
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				return newServer;
			}
		}
		return null;
	}
	
	//TODO: finish this function
	@Override
	public void connected(Boolean flag) {
		if (flag) { //connected to server
			getHintButton.setEnabled(true);
			connectButton.setText("Disconnect");
			connectButton.setEnabled(true);			
		}
		else { //disconnected from server
			getHintButton.setEnabled(false);
			connectButton.setEnabled(true);
			connectButton.setText("Connect");
		}		
	}
	
	
	private void menuHint() {
		
		Group hintControl = new Group(gameButtons, SWT.NONE);
		hintControl.setText ("Hint Server Control:");
		hintControl.setLayout(new GridLayout(2, false));
		//hintControl.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1,1));
		
		
		getHintButton = new Button(hintControl, SWT.PUSH);
		getHintButton.setText("Get Hint");
		getHintButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
		getHintButton.setEnabled(false);
		getHintButton.addSelectionListener(new SelectionListener() {
		
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (getHintButton.getText() == "Get Hint") { //single / multiple hints.
					int hintsNum = Integer.parseInt(movesNumber.getText());
					int treeDepth = Integer.parseInt(treeSize.getText());
					if (treeDepth > 7 || treeDepth < 1) { treeDepth = 7; } //more than 7 is too much!					
					for (int i = 0; i < hintsNum; i++) {
						ui = UserCommand.Solve;
						setChanged();
						notifyObservers(treeDepth);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} else {
					ui = UserCommand.Solve;
					setChanged();
					notifyObservers(-1);					
				}					
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		Button solveGameRbox = new Button(hintControl, SWT.RADIO);
		solveGameRbox.setText("Solve The Game");
		solveGameRbox.setLayoutData(new GridData(SWT.FILL,  SWT.TOP, false, false, 2, 1));
		
		solveGameRbox.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getHintButton.setText("Solve Game");
				movesNumber.setEnabled(false);
				treeSize.setEnabled(false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		
		Button getHintRbox = new Button(hintControl, SWT.RADIO);
		getHintRbox.setText("Give Hints:");
		getHintRbox.setSelection(true);
		getHintRbox.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
		
		getHintRbox.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getHintButton.setText("Get Hint");
				movesNumber.setEnabled(true);
				treeSize.setEnabled(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		Label movesLabel = new Label (hintControl,SWT.NONE);
		movesLabel.setLayoutData(new GridData(SWT.LEFT,  SWT.LEFT, false, false, 1, 1));
		movesLabel.setText ("Num Of Moves: ");
	
		
		movesNumber = new Text(hintControl, SWT.BORDER);
		movesNumber.setText("1");
		movesNumber.setLayoutData(new GridData(SWT.LEFT,  SWT.LEFT, false, false, 1, 1));
		
		
		Label treeLabel = new Label (hintControl,SWT.NONE);
		treeLabel.setLayoutData(new GridData(SWT.BEGINNING,  SWT.BEGINNING, false, false, 1, 1));
		treeLabel.setText ("Tree Size: ");
		treeLabel.setToolTipText("Must be 1-7");
		
		treeSize = new Text(hintControl, SWT.BORDER);
		treeSize.setText("7");
		treeSize.setSize(new Point (1,1));
		treeSize.setLayoutData(new GridData(SWT.BEGINNING,  SWT.BEGINNING, false, false, 1, 1));

		
	}

	

	/* ************************
	 * Other Methods:
	 * ************************/
	
	//Resolve dns + a regex to make that the string is a valid ip.
	private String isValidIP(String in) {
		String[] temp = in.split(":");
		try {
			String ip = new String (InetAddress.getByName(temp[0]).toString()); //resolve dns if needed
			ip = ip.split("/")[1]; //split google.com/ip = ip only
			in = new String (ip+":"+temp[1]); //return to ip:port format
		} catch (UnknownHostException e) {
			System.out.println("Cant resolve DNS or wrong input. Syntax is: <Ip/Host>:<Port>");
		}		
		//Matcher m = Pattern.compile("((\\d+\\.){3}\\d+):(\\d+)").matcher(in); //NOTE: change m.group(3) if using this.
		Matcher m = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})").matcher(in);
		if (m.find()) {
			String[] p = m.group(1).split("\\.");
		    System.out.println(Arrays.toString(p));
		    for (int i = 0; i < 4; i++)
		      if (Integer.parseInt(p[i]) > 255) return null;
		    if (Integer.parseInt(m.group(2)) > 65535) return null;
		    return in;
		  }
		  return null;
	}
	
	
	
	
	@Override
	public void setLose() {
		MessageBox loseWindow = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		loseWindow.setText("You Lost The Game!");
		loseWindow.setMessage("Restart Game?");			
		int buttonID = loseWindow.open();
		if (buttonID == SWT.YES)
			restartGame();
		else
			System.exit(0);
	}

	@Override
	public void setWin() {
		MessageBox winWindow = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		winWindow.setText("You Won The Game!");
		winWindow.setMessage("Restart Game?");			
		int buttonID = winWindow.open();
		if (buttonID == SWT.YES)
			restartGame();		
	}	
	
	@Override
	public abstract void displayBoard(int[][] data);

	@Override
	public abstract UserCommand getUserCommand();
	
	@Override
	public abstract void run(); //the run is inside the concrete implementation

	@Override
	public void displayScore(int score) {
		 scoreLabel.setText("Score: "+score);
	}

	@Override
	public void killThread() {
		killThread = true;
	}
	
	public boolean isKillThread() {
		return killThread;
	}
	
	
	public void setKillThread(boolean flag) {
		killThread = flag;
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

//if(lose && !lost){
//lost=true;
//final Shell loseWindow= new Shell(this.shell);
//loseWindow.setLayout(new GridLayout(2, false));
//
//loseWindow.setSize(250, 150);
//loseWindow.setText("You lost the game");
//Label reStart = new Label (loseWindow,SWT.NONE);
//reStart.setLayoutData(new GridData(SWT.CENTER,  SWT.FILL, true, true, 2, 2));
//reStart.setText ("Restart Game?");
//
//Button yesButton=new Button(loseWindow,SWT.PUSH);
//yesButton.setText("Yes");
//yesButton.setLayoutData(new GridData(SWT.FILL, SWT.RIGHT, true, true,1, 1));
//
//yesButton.addSelectionListener(new SelectionListener() {
//	
//	@Override
//	public void widgetSelected(SelectionEvent arg0) {
//		restartGame();
//		lost=false;
//		loseWindow.close(); 		
//	}
//	
//	@Override
//	public void widgetDefaultSelected(SelectionEvent arg0) {}
//});
//
//Button noButton=new Button(loseWindow,SWT.PUSH);
//noButton.setText("No");
//noButton.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, true,1, 1));
//
//noButton.addSelectionListener(new SelectionListener() {
//	
//	@Override
//	public void widgetSelected(SelectionEvent arg0) {
//		loseWindow.close();
//		System.exit(0);
//	}
//	
//	@Override
//	public void widgetDefaultSelected(SelectionEvent arg0) {}
//});
//
//loseWindow.setFocus();
//loseWindow.open();
//
//}


//if(win && !won){
//won=true;
////final Shell winWindow = new Shell(this.shell);
//
//winWindow.setLayout(new GridLayout(2, false));
//winWindow.setSize(250, 150);
//winWindow.setText("You Won The Game");
//Label winMsg = new Label (winWindow,SWT.NONE);
//winMsg.setLayoutData(new GridData(SWT.CENTER,  SWT.UP, true, true, 2, 2));
//winMsg.setText ("Restart Game?");
//Button yesButton=new Button(winWindow,SWT.PUSH);
//yesButton.setText("Yes");
//yesButton.setLayoutData(new GridData(SWT.FILL, SWT.RIGHT, true, true,1, 1));
//yesButton.addSelectionListener(new SelectionListener() {
//	
//	@Override
//	public void widgetSelected(SelectionEvent arg0) {
//		restartGame();
//		won=false;
//		winWindow.close(); 		
//	}
//	
//	@Override
//	public void widgetDefaultSelected(SelectionEvent arg0) {}
//});
//
//Button noButton=new Button(winWindow,SWT.PUSH);
//noButton.setText("No");
//noButton.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, true,1, 1));
//noButton.addSelectionListener(new SelectionListener() {
//	
//	@Override
//	public void widgetSelected(SelectionEvent arg0) {
//		winWindow.close();
//		//System.exit(0);
//	}
//	
//	@Override
//	public void widgetDefaultSelected(SelectionEvent arg0) {}
//});			
//
//
//winWindow.open();
//}
//


/*
 * 
 * 		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event)
			{
		        int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		        MessageBox messageBox = new MessageBox(shell, style);
		        messageBox.setText("Information");
		        messageBox.setMessage("Close the shell?");
		        event.doit = messageBox.open() == SWT.YES;
		        }
		    });
 */

