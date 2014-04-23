package view;

import model.Model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import controller.Presenter;

//need to fix button1

public class GUI extends Thread {
	
	Display display;
	Shell shell;
	Presenter presenter;
	String gameName;
    private MenuItem pinkScreen;
    private MenuItem yellowScreen;
    
    //get the game name
	public GUI(String string) {
		gameName=string;
	}
		
	private void initComponents () {
		display = new Display();//display = my screen
		shell = new Shell(display);//shell = specific window
		shell.setText(gameName);
		setMenuToolsBar();
		
		setButtons();

		shell.setSize(300, 300);
		shell.open();		
	}
	
//change the screen color to pink	
	 Listener pinkListener = new Listener() {

	        public void handleEvent(Event event) {
	            if (pinkScreen.getSelection()) {
	 //               status.setVisible(true);
	            } else {
	 //               status.setVisible(false);
	            }
	        }
	    };

 //change the screen color to yellow
	    Listener yellowListener1 = new Listener() {

	        public void handleEvent(Event event) {
	            if (yellowScreen.getSelection()) {
	  //              status.setVisible(true);
	            } else {
	 //               status.setVisible(false);
	            }
	        }
	    };
	    
	private void setMenuToolsBar() {
		setMenu();//create the menu tools bar
		setFile();//create the file tools bar
		setEdit();//create the edit tools bar		
	}

//show the menu of the game- new game and exit game
	private void setMenu(){
		//Create the menu			
		Menu menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);

        MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeFileMenu.setText("&Menu");

        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        cascadeFileMenu.setMenu(fileMenu);
        
        newGameOption(fileMenu);// add	menu the option for start new game
		exitGameOption(fileMenu);// add	menu the option for exit new game
  	
	}	
	

//	menu option for start new game
	private void newGameOption(Menu fileMenu) {
		MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
        newItem.setText("&new game");
        
        newItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                presenter.startNewGame();
            }
        });
		
	}
	
//	menu option for exit the game	
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

	//show the menu of edit label at the game- need to finish	
	private void setEdit() {
		String[] items = "white screen, yellow screen".split(",");
		Combo edit = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		edit.setItems(items);
		
	}

	//show the menu of file label at the game- need to finish	
	private void setFile() {
		String[] items = "game rules, game tips".split(",");
		Combo file = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		file.setItems(items);
		
		
	}

	private void setButtons() {
		
		setScoreButton();
		setUndoMoveButton();
		setRestartButton();
		setLoadGameButton();
		setSaveGameButton();
		
	}
	




//undo the last step
	private void setUndoMoveButton() {
		//button2-button for undo move
		shell.setLayout(new GridLayout(2, true));
		Button undoMove = new Button(shell, SWT.PUSH);
		undoMove.setText("Undo Move");
		undoMove.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		//if this button pushed down or up the last move is undo
		undoMove.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				presenter.undoMove();		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {				
				presenter.undoMove();
			}
		});
	}
	


//show the current score at game
	private void setScoreButton() {
		//button1- show the game score- need to be fixed- not need to be button
		shell.setLayout(new GridLayout(2, true));
		final Button gameScore = new Button(shell, SWT.PUSH);
		gameScore.setText("game score");
		gameScore.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2));
		/*for letter use- to fix button1
		final Text t1 = new Text(shell, SWT.BORDER);
		t1.setText("0");
		t1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2));*/
		//need to be fix
		gameScore.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				gameScore.setText("score" +presenter.getScore());		
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
				
			}
		});
	}

//button for restart the current game
	private void setRestartButton() {
		//button3-button for restart game
		shell.setLayout(new GridLayout(2, true));
		Button restartGame = new Button(shell, SWT.PUSH);
		restartGame.setText("Restart Game");
		restartGame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		//if this button pushed down the last move is undo and the the button pushed up back
		restartGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				presenter.restartGame();		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {	
			}
		});
	}

//button for load the last game
	private void setLoadGameButton() {
		//button4- button for load game
		shell.setLayout(new GridLayout(2, true));
		Button loadGame = new Button(shell, SWT.PUSH);
		loadGame.setText("Load Game");
		loadGame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		
		//if this button pushed down you can load last game
		loadGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				presenter.loadGame();		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {	
			}
		});

	}

	
//button for save the current game
	private void setSaveGameButton() {		
		//button5- button for save game
		shell.setLayout(new GridLayout(2, true));
		Button saveGame = new Button(shell, SWT.PUSH);
		saveGame.setText("Save Game");
		saveGame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		
		//if this button pushed down you can save last game
		saveGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				presenter.saveGame();		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {	
			}
		});
				
		
	}	
	
	
	public void run() {
		//the GUI and main loop thread should be in the same THREAD.
		initComponents();
		//while shell is alive
		while (!shell.isDisposed()) {
			//while there are no events (this is the event handler)
			if(!display.readAndDispatch()) {
				//the OS will wake the display on EVENT (mouse, keyboard, etc).
				display.sleep();				
			}
		}		
		
		
		display.dispose();
		
		
	}
}


/*
Canvas canvas = new Canvas(shell, SWT.BORDER);
canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
canvas.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
canvas.addPaintListener(new PaintListener() {
	
	@Override
	public void paintControl(PaintEvent e) {
		
		
	}
});

//button + button2 need to fill the whole row
//text needs to fill the whole row
//canvas.
*/		

