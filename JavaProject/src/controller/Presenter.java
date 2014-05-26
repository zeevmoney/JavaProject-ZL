package controller;

import java.util.Observable;
import java.util.Observer;
import model.Model;
import view.View;
import common.UserCommand;


/**
 * @author Zeev Manilovich.
 * @author Lital Gilboa.
 * 
 * The Presenter class acts upon the model and the view. 
 * It retrieves data from repositories (the model), 
 * and formats it for display in the view.
 * Presenter implements Observer in order to observe
 * changes in View and Model Interfaces.
 */


public class Presenter implements Observer {
	private View ui;
	private View ui2;
	private Model model;
	private Model model2;
	UserCommand cmd;
	Thread t;

	/**
	 * @param model - Model interface
	 * @param model2 - Model interface
	 * @param ui- View interface
	 * @param ui2- View interface
	 * Both View & Model are injected using strategy pattern.
	 */
	
	public Presenter(Model model,Model model2, View ui, View ui2){
		this.model = model;
		this.model2 = model2;
		this.ui = ui;
		this.ui2 = ui2;		
		cmd = UserCommand.Default;
	}

	/**
	 * update method:
	 * This is invoked upon any change to objects "view" and "model"
	 * we can actively get the state of this objects.
	 */
	
	@Override
	public void update(Observable arg0, final Object arg1) {
		if (arg0 == model) {
			ui.displayScore(model.getScore());
			ui.displayBoard(model.getBoard());
			if (arg1 != null && arg1.toString() == "Connected") 
				ui.connected(true);
			if (arg1 != null && arg1.toString() == "Disconnected") 
				ui.connected(false);			
			if (arg1 != null && arg1.toString() == "Win")
				ui.setWin();
			if (arg1 != null && arg1.toString() == "Lose") {
				ui.setLose();				
			}				
		}
		
		
		if (arg0 == ui) {
			cmd = ui.getUserCommand();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
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
					case LoadGame:
						if (arg1 != null && arg1.toString()!= "")
							model.loadGame(arg1.toString());
						break;
					case NewGame:
						model.newGame();
						break;
					case SaveGame:
						if (arg1 != null && arg1.toString()!= "")
							model.saveGame(arg1.toString());
						break;
					case UndoMove:
						model.undoMove();
						break;
					case RestartGame:
						model.newGame();
						break;
					case SwitchGame:
						//Switching Models
						Model tempModel = model;
						model = model2;
						model2 = tempModel;
						//Switching Uis
						View tempView = ui;
						ui = ui2;
						ui2 = tempView;
						//Killing old game
						ui2.killThread();
						t = new Thread((Runnable) ui);
						t.start();
						break;
					case Connect:
						if (arg1 != null) 
						{ 
							String temp = (String)arg1; //string to string (for later use)
							String ip = temp.split(":")[0];
							Integer port = new Integer(temp.split(":")[1]);
							model.connectToServer(ip, port);
						}				
						break;
					case Disconnect:
						model.disconnectFromServer();
						break;
					case Solve: 
						if (arg1 != null && arg1 instanceof int[]) {
							int[] temp = (int[]) arg1;
							int hintsNum = temp[0];						
							int treeDepth = temp[1];
							model.getHint(hintsNum,treeDepth);
						}
						break;				
					default:
						break;				
					}					
				}
			}).start();
		}
	}

}





