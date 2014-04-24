package view.game2048;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import view.Board;
import view.View;

/*
 * TODO: (Lital)
 * Don't forget to add:
 * setChanged & notifyObservers
 */

public class Game2048View extends Observable implements View, Runnable {
	Display display;
	Shell shell;
	Board board;
	
private void initComponents(){
	display= new Display();
	shell= new Shell(display);
		
}
@Override
public void run() {
	initComponents();
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
	
	
}
@Override
public int getUserCommand() {
	// TODO Auto-generated method stub
	return 0;
}
@Override
public void displayScore() {
	// TODO Auto-generated method stub
	
}


//set the tablet at board color
private void setColor(Color background) {
	// TODO Auto-generated method stub
	
}
//get the background color each cell
public Color getBackground(int value) {
    
  }
}

