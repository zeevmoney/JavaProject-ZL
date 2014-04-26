package controller;


/*
 * Enum to represent every user command
 */


public enum UserCommand {
	/*
	 * the movement keys are like the Numpad on the keyboard
	 * new game = 5, all other are 10...n
	 */
	Up (8),
	Down(2),
	Left(4),
	Right(6),
	UpLeft(7),
	UpRight(9),
	DownLeft(1),
	DownRight(3),
	NewGame(5),
	SaveGame(10),
	LoadGame(11),
	UndoMove(12),
	GetWin(13),
	GameLose(14);
	

	//default constructor
	private int command;
	private UserCommand(int command) {
		this.command = command;
	}
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}

	
}
