package view.game2048;


//import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import controller.UserCommand;
import view.AbsView;
import view.Board;
//import view.View;

/*
 * TODO: (Lital)
 * Don't forget to add:
 * setChanged & notifyObservers
 */

public class Game2048View extends AbsView implements Runnable {
	Display display;
	Shell shell;
	Board board;
	boolean flag= false;
	
	public Game2048View(String string) {
		super(string);
		display= getDisplay();
		shell= getShell();
		int [][] data = {{2}};
		displayBoard(data);
	}

	
/*
 * 
 * comment
 */
	
private void initComponents(int[][] data){
	board = new Board(shell, SWT.BORDER, data);
}




//display the board
@Override
public void displayBoard(int[][] data) {
	if(flag==false){
		flag=true;
		initComponents(data);
	}
	else 
		board= new Board(shell, SWT.BORDER, data);	
}

@Override
public UserCommand getUserCommand() {
	// TODO Auto-generated method stub
	return null;
}


@Override
public void displayScore(int score) {
	// TODO Auto-generated method stub
	
}
@Override
public void setLose(boolean lose) {
	// TODO Auto-generated method stub
	
}
@Override
public void setWin(boolean win) {
	// TODO Auto-generated method stub
	
}

@Override
public void run() {
	//the GUI and main loop thread should be in the same THREAD.
	
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

