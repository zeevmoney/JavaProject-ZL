package controller.boot;

import model.model2048.Game2048Model;
import view.game2048.Game2048View;
import controller.Presenter;

//This class starts the game / games
public class Main2048Game {
	
	public static void main(String[] args) {
		Game2048Model m = new Game2048Model(4);
		Game2048View ui = new Game2048View();
		Presenter p= new Presenter(m,ui);
		m.addObserver(p);
		ui.addObserver(p);		

	}

}
