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
		setModel(m);
		setUi(ui2);
	}

	public View getUi() {
		return ui;
	}

	public void setUi(View ui) {
		this.ui = ui;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	
	/*
	 * update method:
	 * This is invoked upon any change to objects "view" and "model"
	 * we can actively get the state of this objects.
	 * TODO: must make it recognize what object sent the data. 
	 */

	@Override
	public void update(Observable arg0, Object arg1) {
		//TODO (Zeev): //ui.displayData(model.getData());
			
	}
}
