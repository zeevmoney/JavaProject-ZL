package view;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
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

import common.ObjectXML;
import common.UserCommand;


//TODO: disable hint / solve.

/**
 * The Class AbsView.
 * Abstract Class AbsView:
 * This class defines the common parts for classes that run a 2D board game.
 */

public abstract class AbsView extends Observable implements View,Runnable  {
	
	/** The ui - user command ENUM */
	UserCommand ui; 
	
	//SWT Components:
	/** The display. */
	Display display; 
	
	/** The shell. */
	Shell shell; 
	
	/** The menu bar. */
	Menu menuBar;
	
	/** The game buttons. */
	Group gameButtons;
	
	/** The game board. */
	Group gameBoard;
	
	/** The direction label. */
	Label scoreLabel,directionLabel;
	
	/** The server combo. */
	Combo serverCombo; //server combo box
	
	/** The server list. */
	ArrayList<String> serverList; //server list after import
	
	/** The game name. */
	String gameName; //game name String.
	
	/** The socket address. */
	InetSocketAddress socketAddress; //to hold the server address
	
	/** The connect button. */
	Button connectButton; //connect to server
	
	/** The get hint button. */
	Button getHintButton; //get hint from server
	
	/** The moves number. */
	Text movesNumber;
	
	/** The tree size. */
	Text treeSize;
	
	/** The new game. */
	boolean newGame=false; //used to init some vars in maze game.
	
	/** The kill thread. */
	boolean killThread=false; //used to kill the current running gui thread (for switching games)
	
	/**
	 * Instantiates a new abs view.
	 * init upper bar
	 * init game buttons
	 * init the group which holds the game board.
	 * @param gameName - the gameName
	 */
	public AbsView(String gameName) {
		this.gameName = gameName;
		this.serverList = new ArrayList<String>();
	}
	
	/**
	 * Inits the components.
	 */
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

	/**
	 * Gets the ui.
	 *
	 * @return the ui
	 */
	public UserCommand getUi() {
		return ui;
	}

	/**
	 * Sets the ui.
	 *
	 * @param ui the new ui
	 */
	public void setUi(UserCommand ui) {
		this.ui = ui;
	}

	/**
	 * Gets the display.
	 *
	 * @return the display
	 */
	public Display getDisplay() {
		return display;
	}

	/**
	 * Sets the display.
	 *
	 * @param display the new display
	 */
	public void setDisplay(Display display) {
		this.display = display;
	}

	/**
	 * Gets the shell.
	 *
	 * @return the shell
	 */
	public Shell getShell() {
		return shell;
	}

	/**
	 * Sets the shell.
	 *
	 * @param shell the new shell
	 */
	public void setShell(Shell shell) {
		this.shell = shell;
	}

	/**
	 * Gets the menu bar.
	 *
	 * @return the menu bar
	 */
	public Menu getMenuBar() {
		return menuBar;
	}

	/**
	 * Sets the menu bar.
	 *
	 * @param menuBar the new menu bar
	 */
	public void setMenuBar(Menu menuBar) {
		this.menuBar = menuBar;
	}


	/**
	 * Gets the score label.
	 *
	 * @return the score label
	 */
	public Label getScoreLabel() {
		return scoreLabel;
	}

	/**
	 * Sets the score label.
	 *
	 * @param scoreLabel the new score label
	 */
	public void setScoreLabel(Label scoreLabel) {
		this.scoreLabel = scoreLabel;
	}
	
	/**
	 * Gets the game board.
	 *
	 * @return the game board
	 */
	public Group getGameBoard() {
		return gameBoard;
	}

	/**
	 * Sets the game board.
	 *
	 * @param gameBoard the new game board
	 */
	public void setGameBoard(Group gameBoard) {
		this.gameBoard = gameBoard;
	}
	
	/**
	 * Checks if is new game.
	 *
	 * @return true, if is new game
	 */
	public boolean isNewGame() {
		return newGame;
	}

	/**
	 * Sets the new game.
	 *
	 * @param newGame the new new game
	 */
	public void setNewGame(boolean newGame) {
		this.newGame = newGame;
	}
		
	/* ************************
	 * Upper menu bar:
	 * ************************/		
	
	//Menu bar (upper menu):
	/**
	 * Sets the menu tools bar.
	 */
	private void setMenuToolsBar() {
		//create the menu bar and add file and edit
		this.menuBar=new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		setFile(); //create the file tools bar
		setEdit(); //create the edit tools bar		
	}

	//File drop down menu
	/**
	 * Sets the file.
	 */
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
	/**
	 * Sets the edit.
	 */
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
	
	/**
	 * New game.
	 *
	 * @param fileMenu the file menu
	 */
	private void newGame(Menu fileMenu) {
		MenuItem newGame = new MenuItem(fileMenu, SWT.CASCADE);
		newGame.setText("New Game");
		Menu newGameSubMenu = new Menu(shell, SWT.DROP_DOWN);
		newGame.setMenu(newGameSubMenu);
        new2048Game(newGameSubMenu);
        newMazeGame(newGameSubMenu);
	}	
	
	/**
	 * New2048 game.
	 *
	 * @param menu the menu
	 */
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
	
	
	/**
	 * New maze game.
	 *
	 * @param menu the menu
	 */
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
	/**
	 * Save game.
	 *
	 * @param fileMenu the file menu
	 */
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
	
	/**
	 * Save game.
	 */
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
	/**
	 * Load game.
	 *
	 * @param fileMenu the file menu
	 */
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
	
	/**
	 * Load game.
	 */
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
	/**
	 * Save and exit game option.
	 *
	 * @param fileMenu the file menu
	 */
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
	/**
	 * Exit game option.
	 *
	 * @param fileMenu the file menu
	 */
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
	/**
	 * Restart game drop menu.
	 *
	 * @param fileMenu the file menu
	 */
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
	
	/**
	 * Restart game.
	 */
	private void restartGame() { //restart game main function
		newGame=true;
    	ui = UserCommand.RestartGame;
    	setChanged();
		notifyObservers();
	}
	
	/**
	 * Undo move drop down (under the Edit drop down menu)
	 *
	 * @param fileMenu the file menu
	 */
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
	
	/**
	 * Undo move.
	 */
	private void undoMove() { //undoMove main function
		newGame=true;
    	ui = UserCommand.UndoMove;
		setChanged();
		notifyObservers();
	}	

	/* ************************
	 * Game buttons group
	 * ************************/	
	
	/**
	 * Game buttons menu.
	 */
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
		hintControlGruop();
	}
	
	/**
	 * Menu undo move button (under buttons menu)
	 */
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
	
	/**
	 * Menu restart button (under buttons menu)
	 */
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
	
	/**
	 * Menu save game button (under buttons menu)
	 */
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

	/**
	 * Menu load game button (under buttons menu)
	 */
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
	
	/**
	 * Game board group.
	 */
	private void gameBoardGroup () { 
		gameBoard = new Group(shell, SWT.SHADOW_OUT);		
		gameBoard.setLayout(new GridLayout(1, true));
		gameBoard.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
	}
	
	
	/* ************************
	 * Server Control Widgets
	 * ************************/
	
	/**
	 * Menu game server.
	 */
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
			serverCombo.setText(tempList[0]);
		} catch (Exception e) {
			System.out.println("[Client]: File Doesn't Exist!");
			serverCombo.setText("127.0.0.1:1337");
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
						System.out.println("[Client]: Connecting To: "+serverCombo.getText());
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
	
	/**
	 * Addserver - add a new server to file + combo.
	 *
	 * @param newServer the new server
	 * @return the string
	 */
	private String addserver (String newServer) { 
		if ((newServer = isValidIP(newServer)) != null) { //if a valid server:port were entered
			if (serverList.contains(newServer)) {
				return newServer; //if server already exists.
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
	
	/* (non-Javadoc)
	 * @see view.View#connected(java.lang.Boolean)
	 */
	@Override
	public void connected(final Boolean flag) {
		getDisplay().syncExec(new Runnable() {
			
			@Override
			public void run() {
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
		});
	
	}
	
	
	/**
	 * Hint control group & widgets.
	 */
	private void hintControlGruop() {
		
		Group hintControl = new Group(gameButtons, SWT.NONE);
		hintControl.setText ("Hint Server Control:");
		hintControl.setLayout(new GridLayout(2, false));
		hintControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1,1));
		
		
		getHintButton = new Button(hintControl, SWT.PUSH);
		getHintButton.setText("Get Hint");
		getHintButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 2, 1));
		getHintButton.setEnabled(false);
		getHintButton.addSelectionListener(new SelectionListener() {
		
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					int treeDepth = Integer.parseInt(treeSize.getText());
					int hintsNum;
					ui = UserCommand.Solve;
					if (getHintButton.getText().equals("Get Hint")) { //single / multiple hints.
						hintsNum = Integer.parseInt(movesNumber.getText());
					} else {
						hintsNum = 2000; //solve the whole game.
					}
					int [] arr = {hintsNum,treeDepth};
					setChanged();
					notifyObservers(arr);
				} catch (NumberFormatException e) {
						errorBox("Invalid Input, TreeSize & Moves must be numeric.");
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
		movesNumber.setText("10");
		movesNumber.setLayoutData(new GridData(SWT.LEFT,  SWT.LEFT, false, false, 1, 1));
		
		
		Label treeLabel = new Label (hintControl,SWT.NONE);
		treeLabel.setLayoutData(new GridData(SWT.BEGINNING,  SWT.BEGINNING, false, false, 1, 1));
		treeLabel.setText ("Tree Size: ");
		treeLabel.setToolTipText("Must be 1-7");
		
		treeSize = new Text(hintControl, SWT.BORDER);
		treeSize.setText("7");
		treeSize.setLayoutData(new GridData(SWT.BEGINNING,  SWT.BEGINNING, false, false, 1, 1));
		
		directionLabel = new Label (hintControl,SWT.NONE);
		directionLabel.setLayoutData(new GridData(SWT.CENTER,  SWT.CENTER, false, false, 1, 1));
		//directionLabel.setText ("");
		directionLabel.setEnabled(false);

		
	}

	

	/* ************************
	 * Other Methods:
	 * ************************/
	
	//Resolve dns + a regex to make that the string is a valid ip.
	/**
	 * Checks if is valid ip.
	 *
	 * @param in the ip:port
	 * @return valid ip:port, else null.
	 */
	private String isValidIP(String in) {
		String[] temp = in.split(":");
		try {
			String ip = new String (InetAddress.getByName(temp[0]).toString()); //resolve dns if needed
			ip = ip.split("/")[1]; //split google.com/ip = ip only
			in = new String (ip+":"+temp[1]); //return to ip:port format
		} catch (UnknownHostException e) {
			System.out.println("[Client]: Cant resolve DNS or wrong input. Syntax is: <Ip/Host>:<Port>");
		}		
		//Matcher m = Pattern.compile("((\\d+\\.){3}\\d+):(\\d+)").matcher(in); //NOTE: change m.group(3) if using this.
		Matcher m = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})").matcher(in);
		if (m.find()) {
			String[] p = m.group(1).split("\\.");
			for (int i = 0; i < 4; i++)
		      if (Integer.parseInt(p[i]) > 255) return null;
		    if (Integer.parseInt(m.group(2)) > 65535) return null;
		    return in;
		  }
		  return null;
	}
	
	/**
	 * Error box - general error box.
	 *
	 * @param string the string do display inside.
	 */
	public void errorBox(String string) {
		MessageBox errorBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK | SWT.CANCEL);
		errorBox.setText("Error!");
		errorBox.setMessage(string);			
		errorBox.open();
	}
	
	
	/* (non-Javadoc)
	 * @see view.View#setLose()
	 */
	@Override
	public void setLose() {
		getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageBox loseWindow = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				loseWindow.setText("You Lost The Game!");
				loseWindow.setMessage("Restart Game?");			
				int buttonID = loseWindow.open();
				if (buttonID == SWT.YES)
					restartGame();
				else
					System.exit(0);				
			}
		});

	}

	/* (non-Javadoc)
	 * @see view.View#setWin()
	 */
	@Override
	public void setWin() {
		getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageBox winWindow = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				winWindow.setText("You Won The Game!");
				winWindow.setMessage("Restart Game?");			
				int buttonID = winWindow.open();
				if (buttonID == SWT.YES)
					restartGame();	
				
			}
		});
	
	}	
	
	/* (non-Javadoc)
	 * @see view.View#displayBoard(int[][])
	 */
	@Override
	public abstract void displayBoard(int[][] data);

	/* (non-Javadoc)
	 * @see view.View#getUserCommand()
	 */
	@Override
	public abstract UserCommand getUserCommand();
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public abstract void run(); //the run is inside the concrete implementation

	/* (non-Javadoc)
	 * @see view.View#displayScore(int)
	 */
	@Override
	public void displayScore(final int score) {
		getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				 scoreLabel.setText("Score: "+score);				
			}
		});
		
	}

	/* (non-Javadoc)
	 * @see view.View#killThread()
	 */
	@Override
	public void killThread() {
		killThread = true;
	}
	
	/**
	 * Checks if is kill thread.
	 *
	 * @return true, if is kill thread
	 */
	public boolean isKillThread() {
		return killThread;
	}
	
	
	/**
	 * Sets the kill thread.
	 *
	 * @param flag the new kill thread
	 */
	public void setKillThread(boolean flag) {
		killThread = flag;
	}

	/**
	 * Sets the direction label.
	 *
	 * @param str the new direction label
	 */
	public void setDirectionLabel(String str) {
		this.directionLabel.setText(str);
	}
	
}