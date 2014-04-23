package view;

import model.Model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import controller.Presenter;



public class GUI extends Thread {
	
	Display display;
	Shell shell;
	Presenter presenter;
	
	public GUI() {
		// TODO Auto-generated constructor stub
	}
	
	//start from start
	//shell = specific window
	//display = my screen
	
	private void initComponents () {
		display = new Display();
		shell = new Shell(display);
		shell.setText("My gui");
		
		//button1- show the game score- need to be fixed- not need to be button
		shell.setLayout(new GridLayout(2, true));
		final Button gameScore = new Button(shell, SWT.PUSH);
		gameScore.setText("game score");
		gameScore.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2));
		/*for letter use- to fix button1
		final Text t1 = new Text(shell, SWT.BORDER);
		t1.setText("0");
		t1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2));*/
		//button2-button for undo move
		shell.setLayout(new GridLayout(2, true));
		Button undoMove = new Button(shell, SWT.PUSH);
		undoMove.setText("Undo Move");
		undoMove.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		//button3-button for restart game
		shell.setLayout(new GridLayout(2, true));
		Button restartGame = new Button(shell, SWT.PUSH);
		restartGame.setText("Restart Game");
		restartGame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		//button4- button for load game
		shell.setLayout(new GridLayout(2, true));
		Button loadGame = new Button(shell, SWT.PUSH);
		loadGame.setText("Load Game");
		loadGame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		//button5- button for save game
		shell.setLayout(new GridLayout(2, true));
		Button saveGame = new Button(shell, SWT.PUSH);
		saveGame.setText("Save Game");
		saveGame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		
		
		
		
		//add* = EVENT		
		//all the vars in this anon class are transfered by value!
		//this means that every change in here is on the object itslef and not on the reference.
		//need to be fix
		gameScore.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				gameScore.setText("The game score is "+ presenter.getScore());
				
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
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
		
		

		

		
		
		shell.setSize(300, 300);
		shell.open();
	*/	
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

