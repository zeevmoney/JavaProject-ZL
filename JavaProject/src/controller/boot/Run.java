package controller.boot;

import model.mazegame.MazeGameModel;
import model.model2048.Game2048Model;
import view.game2048.Game2048View;
import view.maze.MazeGameView;
import controller.Presenter;

public class Run {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Game2048Model model2048 = new Game2048Model(4,2048);
		MazeGameModel modelMaze = new MazeGameModel();
		Game2048View ui2048 = new Game2048View("2048 Game - Zeev & Lital");
		MazeGameView uiMaze = new MazeGameView("Maze Game - Zeev & Lital");
		Presenter p= new Presenter(model2048,modelMaze,ui2048,uiMaze);
		model2048.addObserver(p);
		modelMaze.addObserver(p);
		ui2048.addObserver(p);
		uiMaze.addObserver(p);
		new Thread(ui2048).start();
		
	}

}
