package model.mazegame;

//http://www.jonathanzong.com/blog/2012/11/06/maze-generation-with-prims-algorithm

import java.awt.Point;
import java.util.Observable;
import java.util.Stack;

import model.Model;
import model.algoirthms.GameState;
import model.algoirthms.GameStateXML;
import controller.UserCommand;

public class MazeGameModel extends Observable implements Model,Runnable {
	GameState currentGame; //current game state
	Stack<GameState> gameStack; //stack of previous games
	UserCommand cmd;
	private final int Start = 1;
	private final int Wall = -1;
	private final int End = 2;
	private final int Empty = 0;
	private final int Horizontal = 10;
	private final int Diagonal = 15;
	int rows;
	int cols;
	boolean win;
	boolean lose;
	
	//init all values on the game board & set score to 0.
	//TODO: (Zeev): random generated maze.
	private void boardInit() {
		win=false;
		lose=false;
		currentGame.setScore(0);
		int [][] bigMaze = new int [rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (i==0 && j==0) {
					bigMaze[i][j] = End;
					currentGame.setEnd(new Point (i,j));
				} else if (i==rows-1 && j==cols-1) {
					bigMaze[i][j] = Start;
					currentGame.setStart(new Point (i,j));
				} else if (i==1 && (j>0 && j<cols-1) || i>0 && j==1)
					bigMaze[i][j] = Wall;	
				else bigMaze[i][j] = Empty;
			}
		}
		currentGame.setBoard(bigMaze);		
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void newGame() {
		currentGame = new GameState(rows,cols);
		boardInit();
	
		gameStack.clear();
		gameStack.add(currentGame);
		
		setChanged();
		notifyObservers();
	}

	@Override
	public void saveGame() {
		try {
			GameStateXML gXML = new GameStateXML();
			gXML.gameStateToXML(currentGame, "MazeSave.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void loadGame() {
		try {
			GameStateXML gXML = new GameStateXML();
			gXML.gameStateToXML(currentGame, "MazeSave.xml");			
		} catch (Exception e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers();		
	}

	@Override
	public void undoMove() {
		currentGame=gameStack.pop();
		setChanged();
		notifyObservers();	
		
	}
	
	private void moveHanlde(int moveX, int moveY,UserCommand cmd) {
			//x and y are sent by the moveX function.
			int score=Horizontal;
			int x = currentGame.getPlayer().x + moveX;
			int y = currentGame.getPlayer().y + moveY;
			if (!currentGame.validXY(x, y)) return;  //check if index is valid
			if (currentGame.getXY(x, y) == -1) return; //check if it's a wall.
			if (Math.abs(x) == Math.abs(y)) score = Diagonal; //if it's a diagonal move
			currentGame.setScore(currentGame.getScore()+score);
			currentGame.setPlayer(new Point (x,y));
			gameStack.add(currentGame.Copy());
			setChanged();
			if (currentGame.getPlayer().equals(currentGame.getEnd())) {
				notifyObservers("Win");
				return;
			}
			gameStack.add(currentGame.Copy());			
			notifyObservers();			
	}
	
	
	/*
	 * "-1,-1"	"-1,0"	"-1,1"
	 * "0,-1"	"0,0"	"0,1"
	 * "1,-1"	"1,0"	"1,1"

	 */
	

	@Override
	public void moveUp() {
		moveHanlde(-1,0, UserCommand.Up);
	}

	@Override
	public void moveDown() {
		moveHanlde(1, 0, UserCommand.Down);
	}

	@Override
	public void moveLeft() {
		moveHanlde(0, -1, UserCommand.Left);
	}
	
	@Override
	public void moveRight() {
		moveHanlde(0, 1, UserCommand.Right);
	}
	
	@Override
	public void UpRight() {
		moveHanlde(-1, 1, UserCommand.UpRight);
	}
	
	@Override
	public void UpLeft() {
		moveHanlde(-1, -1, UserCommand.UpLeft);
	}


	@Override
	public void DownRight() {
		moveHanlde(1, 1, UserCommand.DownRight);
	}


	@Override
	public void DownLeft() {
		moveHanlde(1, -1, UserCommand.DownLeft);
	}	

	@Override
	public int[][] getBoard() {
		return currentGame.getBoard();	
	}

	@Override
	public int getScore() {
		return currentGame.getScore();
	}

	@Override
	public boolean getWin() {
		return win;
	}

	@Override
	public boolean getLose() {
		return lose;
	}



	
	
	
	
	/*
	 * My implementation of Prim's Algorithm

import java.util.*;
 
public class Prim {
 
    public static void main(String[]args)
    {
    	// dimensions of generated maze
    	int r=10, c=10;
 
    	// build maze and initialize with only walls
        StringBuilder s = new StringBuilder(c);
        for(int x=0;x<c;x++)
        	s.append('*');
        char[][] maz = new char[r][c];
        for(int x=0;x<r;x++) maz[x] = s.toString().toCharArray();
 
        // select random point and open as start node
        Point st = new Point((int)(Math.random()*r),(int)(Math.random()*c),null);
        maz[st.r][st.c] = 'S';
 
        // iterate through direct neighbors of node
        ArrayList<Point> frontier = new ArrayList<Point>();
        for(int x=-1;x<=1;x++)
        	for(int y=-1;y<=1;y++){
        		if(x==0&&y==0||x!=0&&y!=0)
        			continue;
        		try{
        			if(maz[st.r+x][st.c+y]=='.') continue;
        		}catch(Exception e){ // ignore ArrayIndexOutOfBounds
        			continue;
        		}
        		// add eligible points to frontier
        		frontier.add(new Point(st.r+x,st.c+y,st));
        	}
 
        Point last=null;
        while(!frontier.isEmpty()){
 
        	// pick current node at random
        	Point cu = frontier.remove((int)(Math.random()*frontier.size()));
        	Point op = cu.opposite();
        	try{
        		// if both node and its opposite are walls
        		if(maz[cu.r][cu.c]=='*'){
        			if(maz[op.r][op.c]=='*'){
 
        				// open path between the nodes
        				maz[cu.r][cu.c]='.';
        				maz[op.r][op.c]='.';
 
        				// store last node in order to mark it later
        				last = op;
 
        				// iterate through direct neighbors of node, same as earlier
        				for(int x=-1;x<=1;x++)
				        	for(int y=-1;y<=1;y++){
				        		if(x==0&&y==0||x!=0&&y!=0)
				        			continue;
				        		try{
				        			if(maz[op.r+x][op.c+y]=='.') continue;
				        		}catch(Exception e){
				        			continue;
				        		}
				        		frontier.add(new Point(op.r+x,op.c+y,op));
				        	}
        			}
        		}
        	}catch(Exception e){ // ignore NullPointer and ArrayIndexOutOfBounds
        	}
 
        	// if algorithm has resolved, mark end node
        	if(frontier.isEmpty())
        		maz[last.r][last.c]='E';
        }
 
		// print final maze
		for(int i=0;i<r;i++){
			for(int j=0;j<c;j++)
				System.out.print(maz[i][j]);
			System.out.println();
		}
    }
 
    static class Point{
    	Integer r;
    	Integer c;
    	Point parent;
    	public Point(int x, int y, Point p){
    		r=x;c=y;parent=p;
    	}
    	// compute opposite node given that it is in the other direction from the parent
    	public Point opposite(){
    		if(this.r.compareTo(parent.r)!=0)
    			return new Point(this.r+this.r.compareTo(parent.r),this.c,this);
    		if(this.c.compareTo(parent.c)!=0)
    			return new Point(this.r,this.c+this.c.compareTo(parent.c),this);
    		return null;
    	}
    }
}
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
//	switch (cmd) {
//	case Up:
//		x--;
//		break;
//	case Down:
//		x++;
//		break;
//	case Left:
//		y--;
//		break;
//	case Right:
//		y++;
//		break;
//	case UpRight:
//		
//	default:
//		break;
//}
}