package view;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.algoirthms.ObjectXML;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

import controller.UserCommand;

/*
 * Abstract Class AbsView:
 * This class defines the common parts for classes that run a 2D board game.
 */
//TODO: add mouse check
public abstract class AbsView extends Observable implements View,Runnable  {
	UserCommand ui; //user command ENUM
	//SWT Components:
	Display display; 
	Shell shell; 
	Menu menuBar;
	Group gameButtons;
	Group gameBoard;
	Label scoreLabel;
	Combo serverCombo;
	String gameName; //game name String.	
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
		scoreLabel.setLayoutData(new GridData(SWT.FILL,  SWT.TOP, false, false, 1, 1));
		scoreLabel.setText ("Score: "); //Score label.	     
	    
	    //init menu buttons:
		menuUndoMoveButton();
		menuRestartButton();
		menuLoadGameButton();
		menuSaveGameButton();	
		gameServerMenu();
	}
	
	


	//Undo button (under buttons menu)
	private void menuUndoMoveButton() {
		Button undoMove = new Button(gameButtons, SWT.PUSH);
		undoMove.setText("Undo Move");
		undoMove.setLayoutData(new GridData(SWT.FILL,  SWT.TOP, false, false, 1, 1));
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
	
	/*
	 * 	private Listener connectToServer() {
		return (new ConnectToServer(display){

			@Override
			public void setUserCommand(int userCommand) {
				GamesMaze2048View.this.userCommand = userCommand;
				setChanged();
				notifyObservers(this.socketAddress);
				if (connectedToServer) {
					connected(true);
					connect.setText("Disonnect");
					if (ipBox.indexOf(ipBox.getText()) == -1) {
						ipBox.add(ipBox.getText());
					}
				} else {
					connected(false);
					connect.setText("Connect");
				}
				
			}

			@Override
			public void connected(Boolean flag) {
				getSolver.setEnabled(flag);
				
			}});
	}
	 */
	
	private void gameServerMenu() {
		//TODO: asd
		
		//Seperator
		Label seperator = new Label(gameButtons, SWT.SEPARATOR | SWT.HORIZONTAL);
		seperator.setLayoutData(new GridData(SWT.FILL,  SWT.TOP, false, false, 1, 1));
		
		Label serverLabel = new Label(gameButtons, SWT.NONE);
		serverLabel.setText("Enter a server address:");
		
		//Server Add/Select combo box.
		serverCombo = new Combo(gameButtons, SWT.FILL);
		serverCombo.setText("localhost:1337");
		serverCombo.setToolTipText("Syntax: <Server>:<Port>");
		//Dynamic load of selections
		String serverList[];
		try {
			serverList = (String[]) ObjectXML.ObjectFromXML("resources/Ips.xml");
			serverCombo.setItems(serverList);
		} catch (Exception e) {
			System.out.println("File Doesn't Exist!");
			serverCombo.add("localhost:1337");
		}
		
//////		//Server Selection + validation.
//////		serverCombo.addModifyListener(new ModifyListener() {
//////
//////			@Override
//////			public void modifyText(ModifyEvent arg0) {
//////				String[] server = serverCombo.getItems();
//////				for (String string : server) {
//////					System.out.println(string);
//////					
//////				}
////////				if (!isValidIP(server)) {
////////					//connectButton.setEnabled(true);
////////					System.out.println("Invalid");
////////				}
////////				System.out.println(serverCombo.getItem(serverCombo.getSelectionIndex()));
////////				
////
////
////			}
//		});

		Button connect = new Button(gameButtons, SWT.PUSH);
		connect.setText("Connect");
		connect.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		connect.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				 		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {	
			}
		});	
		Label seperator2 = new Label(gameButtons, SWT.SEPARATOR | SWT.HORIZONTAL);
		seperator2.setLayoutData(new GridData(SWT.FILL,  SWT.TOP, false, false, 1, 1));
		
		
		//TODO: add/remove to xml
		
		//ArrayList<String> serverData = getServerData();
		//String[] arr = new String[serverData.size()];
		//serverData.toArray(arr);
		//comboServers.setItems(arr);
		
		
		

//		Composite controlsLayout = new Composite(composite, SWT.NULL);
//		controlsLayout.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false,
//				false, 1, 1));
//
//		FillLayout fillLayout = new FillLayout();
//		fillLayout.type = SWT.VERTICAL;
//		controlsLayout.setLayout(fillLayout);
//		// controlsLayout.setBackground(shellColor);
//
//		allGame = new Button(composite, SWT.RADIO);
//		allGame.setText("Solve All Game");
//		allGame.setFont(font);
//		// allGame.setBackground(shellColor);
//		allGame.setSelection(true);
//		singleMove = new Button(composite, SWT.RADIO);
//		singleMove.setText("Give Hints:");
//		singleMove.setFont(font);
//		// singleMove.setBackground(shellColor);
//
//		numOfMoves = new Text(composite, SWT.BORDER);
//		numOfMoves.setText("1");
//		numOfMoves.setEnabled(false);
//
//		Label labelServerDetails = new Label(composite, SWT.NONE);
//		labelServerDetails.setText("2. Select Server Details");
//
//		comboServers = new Combo(composite, SWT.NONE);
//		ArrayList<String> serverData = getServerData();
//		String[] arr = new String[serverData.size()];
//		serverData.toArray(arr);
//		comboServers.setItems(arr);
//
//		Label labelNewServer = new Label(composite, SWT.NONE);
//		labelNewServer.setText("3. Add New Server:");
//		ip = new Text(composite, SWT.BORDER);
//		port = new Text(composite, SWT.BORDER);
//
//		// Hint button
//		addServer = GenerateButton(composite, SWT.PUSH, new GridData(SWT.FILL,
//				SWT.TOP, false, false, 1, 1), "Add server", null);
//		addServer.setEnabled(false);
//
//		connectButton = GenerateButton(composite, SWT.PUSH, new GridData(
//				SWT.FILL, SWT.TOP, false, false, 1, 1), "Connect", null);
//		connectButton.setEnabled(false);
//		connectButton.setText("Connect");
//		
//		connectionStateLbl = new Label(composite, SWT.NONE);
//		
//		ExpandItem hintSettingsExpander = new ExpandItem(bar, SWT.NONE, 0);
//		hintSettingsExpander.setText("Hint Settings         :");
//		hintSettingsExpander.setHeight(composite.computeSize(SWT.DEFAULT,
//				SWT.DEFAULT).y);
//		hintSettingsExpander.setControl(composite);
//		// hintSettingsExpander.setImage(image);
//		hintSettingsExpander.setExpanded(true);
//
//		addServer.setEnabled(false);
//		bar.setSpacing(8);
//		
//
//		// On Choose solve all game or solve few moves
//		allGame.addSelectionListener(new SelectionListener() {
//
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//
//				numOfMoves.setEnabled(false);
//			}
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent arg0) {
//
//			}
//		});
//		singleMove.addSelectionListener(new SelectionListener() {
//
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				numOfMoves.setEnabled(true);
//			}
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent arg0) {
//
//			}
//		});
//
//		// On Change IP & Port:
//		ip.addModifyListener(new ModifyListener() {
//			@Override
//			public void modifyText(ModifyEvent arg0) {
//				addServer.setEnabled(validIpPort());
//			}
//		});
//		port.addModifyListener(new ModifyListener() {
//
//			@Override
//			public void modifyText(ModifyEvent arg0) {
//				addServer.setEnabled(validIpPort());
//
//			}
//		});
//
//		// on adding new port
//		// note: no need to do validation here - in case not valid data - button
//		// is disabled
//		addServer.addSelectionListener(new SelectionListener() {
//
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				// add server data to combo
//				comboServers.add(ip.getText() + " " + port.getText());
//
//				// clear text boxes
//				ip.setText("");
//				port.setText("");
//
//				// select in servers combo the newly added ip
//				// new ip is always added to the end of the list
//				comboServers.select(comboServers.getItemCount() - 1);
//
//				// TODO: save server settings
//			}
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//		// on server selection changed
//		comboServers.addModifyListener(new ModifyListener() {
//
//			@Override
//			public void modifyText(ModifyEvent arg0) {
//				String selectedServer = comboServers.getItem(comboServers
//						.getSelectionIndex());
//				if (selectedServer != "") {
//					// enable hints!!!
//					connectButton.setEnabled(true);
//				}
//
//			}
//		});
//
//		// connect to server on click event
//		connectButton.addSelectionListener(new SelectionListener() {
//
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				if (connectButton.getText() == "Connect"){
//				String selectedServerData = comboServers.getText();
//				userCommand = GameAction.CONNECT;
//				// raise a flag of a change
//				setChanged();
//				// actively notify all observers
//				// and invoke their update method
//				Object[] args = { selectedServerData.split(" ")[0],
//						Integer.parseInt(selectedServerData.split(" ")[1]) };
//				notifyObservers(args);
//				hintButton.setEnabled(true);
//				connectionStateLbl.setText("connected....");
//				connectButton.setText("Disconnect");
//				}
//				else{
//					userCommand = GameAction.DISCONNECT;
//					// raise a flag of a change
//					setChanged();
//					notifyObservers();
//					hintButton.setEnabled(false);
//					connectionStateLbl.setText("");
//					connectButton.setText("Connect");
//				}
//
//			}
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});

	}

//    private void hidePopUp() {
//        if(popup != null){
//            popup.hide();
//        }
//    }
//
//    private void showPopUp(Event e, Shell shell) {
//       Composite composite = new Composite(shell, SWT.EMBEDDED);
//       Frame frame = SWT_AWT.new_Frame(composite);
//       JPanel p = new JPanel();
//       frame.add(p);
//       JLabel mouse = new JLabel("Syntax: Server:Port");
//      
//       
//       popup = PopupFactory.getSharedInstance().getPopup(p, mouse , shell.getBounds().x + 20,shell.getBounds().y+20);
//       popup.show();
//    }
	
	

	/* ************************
	 * Other Methods:
	 * ************************/
	
	//a regex to make that the string is a valid ip:port format.
	private boolean isValidIP(String in) {
		String[] temp = in.split(":");
		try {
			String ip = new String (InetAddress.getByName(temp[0]).toString());
			in = new String (ip + temp[1]);
		} catch (UnknownHostException e) {
			System.out.println("Wrong Input, Syntax is: <Ip/Host>:<Port>");
		}		
		Matcher m = Pattern.compile("((\\d+\\.){3}\\d+):(\\d+)").matcher(in);
		if (m.find()) {
		    String[] p = m.group(1).split("\\.");
		    for (int i = 0; i < 4; i++)
		      if (Integer.parseInt(p[i]) > 255) return false;
		    if (Integer.parseInt(m.group(3)) > 65535) return false;
		    return true;
		  }
		  return false;
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

