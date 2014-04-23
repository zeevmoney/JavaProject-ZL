package controller;

import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;

/*
 * The presenter acts upon the model and the view. 
 * It retrieves data from repositories (the model), 
 * and formats it for display in the view.
 */


public class Presenter implements Observer {
	
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

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
