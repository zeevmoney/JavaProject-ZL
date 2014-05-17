package controller;

import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;

/*
 * The presenter acts upon the model and the view. 
 * It retrieves data from repositories (the model), 
 * and formats it for display in the view.
 * Presenter implements Observer in order to observe
 * changes in View and Model Interfaces.
 */

public class Presenter implements Observer {
	//Both View & Model are injected using strategy pattern.
	private View ui;
	private Model model;
	UserCommand cmd;
	
	public Presenter(Model m, View ui2){
		this.model = m;
		this.ui = ui2;
		cmd = UserCommand.Default;
	}

	/*
	 * update method:
	 * This is invoked upon any change to objects "view" and "model"
	 * we can actively get the state of this objects.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 == model) {
			//System.out.println(arg1.toString());
			ui.displayScore(model.getScore());
			ui.displayBoard(model.getBoard());
			if (arg1 != null && arg1.toString() == "Win")
				ui.setWin();
			if (arg1 != null && arg1.toString() == "Lose") {
				ui.setLose();				
			}
				
		}
		if (arg0 == ui) {
			cmd = ui.getUserCommand();
			switch (cmd) {
				case Up: 
					model.moveUp();
					break;
				case Down:
					model.moveDown();
					break;
				case Left:
					model.moveLeft();
					break;
				case Right:
					model.moveRight();
					break;
				case UpLeft:
					model.UpLeft();
					break;
				case UpRight:
					model.UpRight();
					break;
				case DownLeft:
					model.DownLeft();
					break;
				case DownRight:
					model.DownRight();
					break;
				case GetLose:
					model.getLose();
					break;
				case GetWin:
					model.getWin();
					break;
				case LoadGame:
					if (arg1 != null && arg1.toString()!= "")
						model.loadGame(arg1.toString());
					break;
				case NewGame:
					model.newGame();
					break;
				case SaveGame:
					if (arg1 != null && arg1.toString()!= "")
						System.out.println(arg1.toString());
						model.saveGame(arg1.toString());
					break;
				case UndoMove:
					model.undoMove();
					break;
				case RestartGame:
					model.newGame();
				default:
					break;				
				}
		}
	}
 
}






