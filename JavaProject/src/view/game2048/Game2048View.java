package view.game2048;


import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import controller.UserCommand;
import view.Board;
import view.GUI;
import view.View;

/*
 * TODO: (Lital)
 * Don't forget to add:
 * setChanged & notifyObservers
 */

public class Game2048View extends Observable implements View, Runnable {
	Display display;
	Shell shell;
	GUI gui;
	Board board;
	
private void initComponents(){
	gui= new GUI("my 2048 game");
	board= new Board(board, 0, null);
	display= gui.getDisplay();
	shell= gui.getShell();
	
		
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
@Override
public void displayBoard(int[][] data) {
	// TODO Auto-generated method stub
	
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

