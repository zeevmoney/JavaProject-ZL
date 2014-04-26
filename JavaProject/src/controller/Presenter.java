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
	
	public Presenter(Model m, View ui2){
		this.model = m;
		this.ui = ui2;
	}

	/*
	 * update method:
	 * This is invoked upon any change to objects "view" and "model"
	 * we can actively get the state of this objects.
	 */

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 == model) {
			ui.displayBoard(model.getBoard());
		}
		if (arg0 == ui) {
		UserCommand cmd;
		cmd.setCommand(ui.getUserCommand());
		switch (cmd) {
			case Up: 
				model.moveUp();
				break;
			case Down:
				model.moveDown();
				break;
			case Left:
				model.moveDown();
				break;
			case Right:
				model.moveRight();
				break;
			case UpLeft:
				model.moveUp();
				model.moveLeft();
				break;
			case UpRight:
				model.moveUp();
				model.moveRight();
				break;
			case DownLeft:
				model.moveDown();
				model.moveLeft();
				break;
			case DownRight:
				model.moveDown();
				model.moveRight();
				break;
			case GameLose:
				model.getLose();
			case GetWin:
				model.getWin();
			case LoadGame:
				model.loadGame();
			case NewGame:
				model.newGame();
			case SaveGame:
				model.saveGame();
			case UndoMove:
				model.undoMove();
			default:
				break;				
			}
		}
}
 
 
	public int getScore() {
		// TODO (Zeev):// return game current score
		return 0;
	}



	

}






