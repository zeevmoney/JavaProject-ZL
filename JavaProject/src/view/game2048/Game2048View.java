package view.game2048;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import controller.UserCommand;
import view.AbsView;
import view.Board;




/*
 * TODO: (Lital)
 * Don't forget to add:
 * need to add the data for new board of the new game
 * need add key+mouse listener
 * setChanged & notifyObservers
 * active user command
 */

public class Game2048View extends AbsView implements Runnable {
	Display display;
	Shell shell;	
	Board board;
	boolean flag= false;
	
public Game2048View(String string) {
	super(string);
	display= getDisplay();//get from AbsView
	shell= getShell();//get from AbsView
	
	//need to add the data for new board	
	
	int [][] data = {{2,16},{2,16}};//just for check, need to change it to start board
	displayBoard(data);//display the new board
	board.updateBoard(data);
	super.setDisplay(display);
	super.setShell(shell);	
		
	shell.setText(string);//set the game name
	setMenuToolsBar();//create the menu tools bar from AbsView	
	setButtons();//create the buttons  from AbsView
	
	shell.setSize(600, 600);
	shell.open();	
}

	


//display the board
@Override
public void displayBoard(int[][] data) {
	//display the board for the first time after create the board otherwise update board
	if(flag==false){
		flag=true;
		display=new Display();
		shell=new Shell(display);	
		board = new Board(shell, SWT.BORDER, data);
		board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,2));
	}
	else {
		board.updateBoard(data);}
}


//update score 
@Override
public void displayScore(int score) {
	 getScoreLabel().setText("Score:"+score);
	
}


@Override
public UserCommand getUserCommand() {

	return null;
}



@Override
public void run() {
	//the GUI and main loop thread should be in the same THREAD.
	
	//while shell is alive
	while (!shell.isDisposed()) {
		//while there are no events (this is the event handler)
		if(!display.readAndDispatch()) {
			//the OS will wake the display on EVENT (mouse, keyboard, etc).
		}
	}		
	display.dispose();			
}



}