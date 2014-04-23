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
	for (int i = 0; i < data.length; i++) {
		for (int j = 0; j < data[i].length; j++){
			board.setColor(getBackground(data[i][j]));
		}
			
	}
	
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

//get the background color each cell
public Color getBackground(int value) {
    switch (value) {
      case 2:    return new Color(0xeee4da);
      case 4:    return new Color(0xede0c8);
      case 8:    return new Color(0xf2b179);
      case 16:   return new Color(0xf59563);
      case 32:   return new Color(0xf67c5f);
      case 64:   return new Color(0xf65e3b);
      case 128:  return new Color(0xedcf72);
      case 256:  return new Color(0xedcc61);
      case 512:  return new Color(0xedc850);
      case 1024: return new Color(0xedc53f);
      case 2048: return new Color(0xedc22e);
    }
    return new Color(0xcdc1b4);
  }
}

