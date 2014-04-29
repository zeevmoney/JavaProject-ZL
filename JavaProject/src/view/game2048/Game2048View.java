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
	}

	
/*
 * 
 * comment
 */
	
private void initComponents(int[][] data){
	display= getDisplay();
	shell= getShell();
	shell.setSize(300, 300);
	shell.open();
	board = new Board(shell, SWT.BORDER, data);		
}


@Override
public void run() {
	
	while(!shell.isDisposed()){
		if(!display.readAndDispatch()){
			display.sleep();
		}
	}
	display.dispose();
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
}

