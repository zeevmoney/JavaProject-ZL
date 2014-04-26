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
			 int input = ui.getUserCommand();
			 switch (input) {
			case 8: 
				model.moveUp();
				break;
			case 2:
				model.moveDown();
				break;
			case 4:
				model.moveDown();
				break;
			case 6:
				model.moveRight();
				break;
			}
		 }
		 
		 
			
	}
		
	
	public int getScore() {
		// TODO (Zeev):// return game current score
		return 0;
	}



	

}






