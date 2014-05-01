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

import controller.Presenter;
import controller.UserCommand;


//TODO: delete the menu+set win+loose+check score

public abstract class AbsView extends Observable implements View {
	public  Display display;
	public Shell shell;
	Presenter presenter;
	UserCommand ui;
	Menu menuBar;
	Group group;
	GridLayout gridLayout;
	Label scoreLabel;
	
 //   private MenuItem pinkScreen;
 //   private MenuItem yellowScreen;
    
    //get the game name
	public AbsView(String gameName) {
//		display = new Display();//display = my screen
//		shell = new Shell(display);//shell = specific window
//		shell.setText(gameName);
//		setMenuToolsBar();//create the menu tools bar 
//		setButtons();//create the buttons on the left side
//		shell.setSize(300, 300);
//		shell.open();
	}
	
	public void setDisplay(Display display){
		this.display= display;
	}
	
	public void setShell(Shell shell){
		this.shell= shell;
	}
	public Display getDisplay(){
		return display;
	}
	
	public Shell getShell(){
		return shell;
	}
	    
	protected void setMenuToolsBar() {
//		setMenu();//create the menu label
		this.menuBar=new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		setFile();//create the file tools bar
		setEdit();//create the edit tools bar		
	}

//show the label menu on the left up side
	private void setMenu(){
		//create menu label
		Label label = new Label(shell, SWT.CENTER);
	    label.setText("menu: ");
	    label.setBounds(shell.getClientArea());
	    //Create horizontal line
	//    Label shadow_sep_h = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.HORIZONTAL);
	//	shadow_sep_h.;
  	
	}	
	


	//show the menu of edit label at the game- need to finish	
	private void setEdit() {
	//Create the edit			
			//Menu menuBar = new Menu(shell, SWT.BAR);
	       // shell.setMenuBar(menuBar);

	        MenuItem aFileMenu = new MenuItem(menuBar, SWT.CASCADE);
	        aFileMenu.setText("&Edit");

	        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	        aFileMenu.setMenu(fileMenu);
	        restartGame(fileMenu);// add to edit label the option for restart game
	        undoMove(fileMenu);//  add to edit label the option for undo last step
	       
	}

	  

//show the menu of file label at the game- need to finish	
	private void setFile() {
	//	Menu menuBar = new Menu(shell, SWT.BAR);
     //   shell.setMenu(menuBar);

        MenuItem aFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        aFileMenu.setText("&File");

        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        aFileMenu.setMenu(fileMenu);
        loadGame(fileMenu);// add menu the option for load game
        saveGame(fileMenu);// add menu the option for save game
        saveAndExitGameOption(fileMenu);// add	menu the option for save and exit game
		exitGameOption(fileMenu);// add	menu the option for exit new game
		
		
	}


//	edit option for restart  game
private void restartGame(Menu fileMenu) {

	MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
    newItem.setText("&restart game");
    
    newItem.addSelectionListener(new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
        	ui = UserCommand.RestartGame;
			setChanged();
			notifyObservers();
        }
    });
    
	}

//edit option for undo last move at  game	
private void undoMove(Menu fileMenu) {

	MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
    newItem.setText("&undo move");
    
    newItem.addSelectionListener(new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
        	ui = UserCommand.UndoMove;
			setChanged();
			notifyObservers();              
            
        }
    });
		
	}
//	file option for save  game
private void saveGame(Menu fileMenu) {
	MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
    newItem.setText("&save game");
    
    newItem.addSelectionListener(new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
        	ui = UserCommand.SaveGame;
			setChanged();
			notifyObservers();                
            
        }
    });
		
	}

//file option for load  game
private void loadGame(Menu fileMenu) {
	MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
    newItem.setText("&load game");
    
    newItem.addSelectionListener(new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
        	ui = UserCommand.LoadGame;
			setChanged();
			notifyObservers();              
            
        }
    });
		
	}

//	file option for save and exit game
	private void saveAndExitGameOption(Menu fileMenu) {
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
        newItem.setText("&save and exit");
        
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
	
//	file text- for exit game	
	private void exitGameOption(Menu fileMenu) {
	 MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText("&Exit game");
        
        exitItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });
		
	}

	
//set all the right side buttons
	protected void setButtons() {
		
		group = new Group(shell, SWT.NONE);
	    group.setText ( "need find name" ) ;
	    gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		
		group.setLayout(gridLayout);
	//	group.setLocation(5, 5);

	     scoreLabel = new Label ( group, SWT.NONE ) ; 
	     scoreLabel.setText ("score"); 	     
	     scoreLabel.pack () ; 
	
		//setScoreButton();
		setUndoMoveButton();
		setRestartButton();
		setLoadGameButton();
		setSaveGameButton();
		
	}
	




//undo the last step
	private void setUndoMoveButton() {
		//button2-button for undo move
		shell.setLayout(new GridLayout(1, true));
		Button undoMove = new Button(group, SWT.PUSH);
		undoMove.setText("Undo Move");
		undoMove.setLayoutData(new GridData(SWT.FILL,  SWT.TOP, false, false, 1, 1));
		//if this button pushed down or up the last move is undo
		undoMove.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ui = UserCommand.UndoMove;
				setChanged();
				notifyObservers();   		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {				
				//ui.setCommand(12);     
			}
		});
	}
	


//show the current score at game
	private void setScoreButton() {
		//button1- show the game score- need to be fixed- not need to be button
		shell.setLayout(new GridLayout(1, true));
		final Button gameScore = new Button(group, SWT.PUSH);
		gameScore.setText("game score");
		gameScore.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		/*for letter use- to fix button1
		final Text t1 = new Text(shell, SWT.BORDER);
		t1.setText("0");
		t1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2));*/
		//need to be fix
		gameScore.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
				
			}
		});
	}

//button for restart the current game
	private void setRestartButton() {
		//button3-button for restart game
		shell.setLayout(new GridLayout(1, true));
		Button restartGame = new Button(group, SWT.PUSH);
		restartGame.setText("Restart Game");
		restartGame.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		//if this button pushed down the last move is undo and the the button pushed up back
		restartGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ui = UserCommand.RestartGame;
				setChanged();
				notifyObservers();     		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {	
			}
		});
	}

//button for load the last game
	private void setLoadGameButton() {
		//button4- button for load game
		shell.setLayout(new GridLayout(1, true));
		Button loadGame = new Button(group, SWT.PUSH);
		loadGame.setText("Load Game");
		loadGame.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		
		//if this button pushed down you can load last game
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

	
//button for save the current game
	private void setSaveGameButton() {		
		//button5- button for save game
		shell.setLayout(new GridLayout(1, true));
		Button saveGame = new Button(group, SWT.PUSH);
		saveGame.setText("Save Game");
		saveGame.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		
		//if this button pushed down you can save last game
		saveGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ui = UserCommand.SaveGame;
				setChanged();
				notifyObservers();     	
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {	
			}
		});
				
		
	}	
	


	@Override
	public abstract void displayBoard(int[][] data);

	@Override
	public UserCommand getUserCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void displayScore(int score) {
		 scoreLabel.setText ("score"+score); 	
		
	}
//open new window msg
	@Override
	public void setLose(boolean lose) {
		// TODO Auto-generated method stub
		
	}
//open new window msg
	@Override
	public void setWin(boolean win) {
		// TODO Auto-generated method stub
		
	}
}

