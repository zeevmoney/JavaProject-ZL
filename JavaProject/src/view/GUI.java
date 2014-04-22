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



public class GUI extends Thread {
	
	Display display;
	Shell shell;
	State state;
	
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
		
		//button1
		shell.setLayout(new GridLayout(2, true));
		final Button gameScore = new Button(shell, SWT.PUSH);
		gameScore.setText("game score");
		gameScore.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2));
		/*
		final Text t1 = new Text(shell, SWT.BORDER);
		t1.setText("0");
		t1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 2));*/
		//button2
		shell.setLayout(new GridLayout(2, true));
		Button undoMove = new Button(shell, SWT.PUSH);
		undoMove.setText("Undo Move");
		undoMove.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		//button3
		shell.setLayout(new GridLayout(2, true));
		Button restartGame = new Button(shell, SWT.PUSH);
		restartGame.setText("Restart Game");
		restartGame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		//button4
		shell.setLayout(new GridLayout(2, true));
		Button loadGame = new Button(shell, SWT.PUSH);
		loadGame.setText("Load Game");
		loadGame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		//button5
		shell.setLayout(new GridLayout(2, true));
		Button saveGame = new Button(shell, SWT.PUSH);
		saveGame.setText("Save Game");
		saveGame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		
		
		
		
		//add* = EVENT		
		//all the vars in this anon class are transfered by value!
		//this means that every change in here is on the object itslef and not on the reference.
		
		gameScore.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				gameScore.setText("The game score is "+state.getScore());
				
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		undoMove.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				state.undoMove();		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		restartGame.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				state.restartGame();		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
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

