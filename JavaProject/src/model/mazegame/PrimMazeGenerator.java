package model.mazegame;

import java.util.*;

/*
 * PrimMazeGenerator class: generates a maze using Prim algorithm
 * Copied from here: http://www.jonathanzong.com/blog/2012/11/06/maze-generation-with-prims-algorithm
 */

public class PrimMazeGenerator {
 
	public int[][] generateMazeByPrimAlgo(int rows,int cols)
    {
		// dimensions of generated maze
    	int r=rows;
    	int c=cols;
 
    	// build maze and initialize with only walls
        StringBuilder s = new StringBuilder(c);
        for(int x=0;x<c;x++)
        	s.append('*');
        char[][] maz = new char[r][c];
        for(int x=0;x<r;x++) 
        	maz[x] = s.toString().toCharArray();
 
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
 
        int[][] maze = new int[rows][cols];
        
        
        
		// print final maze
		for(int i=0;i<r;i++){
			for(int j=0;j<c;j++)
				{
					if (maz[i][j] == '*')
						maze[i][j] = MazeGameModel.Wall;
					else if (maz[i][j] == '.')
						maze[i][j] = MazeGameModel.Empty;
					else if (maz[i][j] == 'S')
					{
						maze[i][j] = MazeGameModel.Start;					
					}
					else if (maz[i][j] == 'E')
						maze[i][j] = MazeGameModel.End;					
				}			
		}
		
		return maze;
    }
    
    private class Point{
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