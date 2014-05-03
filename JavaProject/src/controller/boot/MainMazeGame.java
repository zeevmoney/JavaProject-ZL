package controller.boot;

import model.mazegame.MazeGameModel;
import view.maze.MazeGameView;
import controller.Presenter;

public class MainMazeGame {

	public static void main(String[] args) {
		MazeGameModel m = new MazeGameModel();
		MazeGameView ui = new MazeGameView("Maze Game - Zeev & Lital");
		Presenter p= new Presenter(m,ui);
		m.addObserver(p);
		ui.addObserver(p);	
		ui.run();

	}

}
