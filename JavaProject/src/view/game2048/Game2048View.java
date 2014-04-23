package view.game2048;

import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import view.View;

public class Game2048View extends Observable implements View, Runnable {
	Display display;
	Shell shell;
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
@Override
public void displayData(int[][] data) {
	// TODO Auto-generated method stub
	
}
@Override
public int getUserCommand() {
	// TODO Auto-generated method stub
	return 0;
}
}
