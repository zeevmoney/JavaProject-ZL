package controller;


/*
 * Enum to represent every user command
 */

public enum UserCommand {
	/*
	 * the movement codes are like the NumPad on the keyboard
	 * new game = 5, all other are 10...n
	 */
	
	Default(0),
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
	GetLose(14),
	RestartGame(15), 
	SwitchGame(16),
	Connect(17),
	Disconnect(18),
	Connected(19),
	Disconnected(20);	

	
	private int command;
	
	//default constructor
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
