package view.game2048;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import controller.UserCommand;
import view.AbsView;
import view.Board;


//import view.View;

/*
 * TODO: (Lital)
 * Don't forget to add:
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
	display= getDisplay();
	shell= getShell();
	
	//getShell().setLayout(new GridLayout(2, false));
	
	
	int [][] data = {{2,16},{2,16}};
	displayBoard(data);
	super.setDisplay(display);
	super.setShell(shell);
	
	
	//display = new Display();//display = my screen
//	shell = new Shell(display);//shell = specific window
	shell.setText(string);
	setMenuToolsBar();//create the menu tools bar 
	
	setButtons();//create the buttons on the left side
	//displayBoard(data);
	shell.setSize(600, 600);
	shell.open();
	
	//board= new Board(board, SWT.BORDER, data);
//	board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,2));
	int i=0;
	
}

	
	
//private void initComponents(){
//	
//
//	String string="my 2048 game";
//	board= new Board(board, 0, null);
//	
//	display = new Display();//display = my screen
//	shell = new Shell(display);//shell = specific window
//	shell.setText(string);
//	setMenuToolsBar();//create the menu tools bar 
//	setButtons();//create the buttons on the left side
//	shell.setSize(300, 300);
//	shell.open();
//	int i=0;
//	
//	
////	display= getDisplay();
////	shell= getShell();	
//		
//}
//@Override
//public void run() {
//	initComponents();
//	while(!shell.isDisposed()){
//		if(!display.readAndDispatch()){
//			display.sleep();
//			
//		}


	
/*
 * 
 * comment
 */
	
private void initComponents(int[][] data){
	board = new Board(shell, SWT.BORDER, data);
	board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,2));
}





//display the board

@Override
public void displayBoard(int[][] data) {

	if(flag==false){
		flag=true;
		display=new Display();
		shell=new Shell(display);	
		initComponents(data);
	}
	else {
		board= new Board(shell, SWT.BORDER, data);
		board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,2));}
}



@Override
public void displayScore(int score) {
	// TODO Auto-generated method stub	
}


@Override
public UserCommand getUserCommand() {
//	display.addFilter(SWT.KeyUp, new Listener()
//    {
//	    UserCommand userCommand= getUserCommand();
//		public void handleEvent(Event e)
//        {			
//			switch (e.keyCode) {
//			case (SWT.ARROW_UP):
//				userCommand = UserCommand.Up;
//				setChanged();				
//				notifyObservers();
//				break;
//			case (SWT.ARROW_DOWN):
//				userCommand = UserCommand.Down;
//				setChanged();				
//				notifyObservers();
//				break;
//			case (SWT.ARROW_LEFT):
//				userCommand = UserCommand.Left;
//				setChanged();				
//				notifyObservers();
//				break;
//			case (SWT.ARROW_RIGHT):
//				userCommand = UserCommand.Right;
//				setChanged();				
//				notifyObservers();
//				break;
//			default:
//				break;
//			}		
//	
//       	}
//   	}
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





//@Override
//public void run() {
//	// TODO Auto-generated method stub
//	
//}

}