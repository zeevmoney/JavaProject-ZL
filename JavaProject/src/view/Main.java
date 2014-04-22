package view;

import game2084.Game2048Model;
import game2084.Game2048View;
import controller.Presenter;

public class Main {

	public static void main(String[] args) {
		Game2048Model m= new Game2048Model();
		Game2048View ui = new Game2048View();
		Presenter p= new Presenter(m,ui);
		m.addObserver(p);
		ui.addObserver(p);
		

	}

}
