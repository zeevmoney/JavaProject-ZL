package model.algoirthms;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import model.Model;
import controller.UserCommand;
import view.Board;
import model.model2048.Game2048Model;

public class AbsMinimax {
	
	static Game2048Model model;

	public enum Player {
        /**
         * Computer
         */
        COMPUTER, 

        /**
         * User
         */
        USER
    }
    
    /**
     * Method that finds the best next move.
     * 
     * @param theBoard
     * @param depth
     * @return
     * @throws CloneNotSupportedException 
     */
	//need change return to user command
    public static String findBestMove(GameState Board,int depth) throws CloneNotSupportedException {
        //Map<String, Object> result = minimax(theBoard, depth, Player.USER);
        
        Map<String, Object> result = alphabeta(Board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, Player.USER);
        
        return result.toString();
    }
    
    /**
     * Finds the best move by using the Minimax algorithm.
     * 
     * @param theBoard
     * @param depth
     * @param player
     * @return
     * @throws CloneNotSupportedException 
     */
    private static Map<String, Object> minimax(GameState Board, int depth, Player player) throws CloneNotSupportedException {
        Map<String, Object> result = new HashMap<String, Object>();
        
        String bestDirection;
        int bestScore;
        
       //the break condition 
		if(depth==0 ||model.getLose()||model.getWin()) {
            bestScore=calculateClusteringScore(Board.getBoard());
        }
        else {
            if(player == Player.USER) {
                bestScore = Integer.MIN_VALUE;

                for(int i=1;i<=4;i++) {
                    GameState newBoard = Board.Copy();
                    newBoard.userCommand(2*i);
                    int points=newBoard.getScore();                    
                    if(points==0) {
                    	continue;
                    }

                    Map<String, Object> currentResult = minimax(newBoard, depth-1, Player.COMPUTER);
                    int currentScore=((Number)currentResult.get("Score")).intValue();
                    if(currentScore>bestScore) { //maximize score
                        bestScore=currentScore;
                        bestDirection=direction.getDescription();
                    }
                }
            }
            else {
                bestScore = Integer.MAX_VALUE;

                List<Integer> moves = getEmptyCellIds(Board);
                if(moves.isEmpty()) {
                    bestScore=0;
                }
                int[] possibleValues = {2, 4};

                int i,j;
                int[][] boardArray;
                for(Integer cellId : moves) {
                    i = Board.getBoardRows();
                    j = i;

                    for(int value : possibleValues) {
                    	GameState newBoard = Board.Copy();
                        setEmptyCell(newBoard,i, j, value);

                        Map<String, Object> currentResult = minimax(newBoard, depth-1, Player.USER);
                        int currentScore=((Number)currentResult.get("Score")).intValue();
                        if(currentScore<bestScore) { //minimize best score
                            bestScore=currentScore;
                        }
                    }
                }
            }
        }
        
        result.put("Score", bestScore);
        result.put("Direction", bestDirection);
        
        return result;
    }
    
    private static void setEmptyCell(GameState newBoard, int i, int j, int value) {
		// TODO Auto-generated method stub
		
	}

	/**
     * Finds the best move bay using the Alpha-Beta pruning algorithm.
     * 
     * @param ks
     * @param depth
     * @param alpha
     * @param beta
     * @param player
     * @return
     * @throws CloneNotSupportedException 
     */
    private static Map<String, Object> alphabeta(GameState Board, int depth, int alpha, int beta, Player player) throws CloneNotSupportedException {
        Map<String, Object> result = new HashMap<String, Object>();
        
        Direction bestDirection = null;
        int bestScore;
        
        if(model.getLose()||model.getWin()) {
            if(model.getWin()) {
                bestScore=Integer.MAX_VALUE; //highest possible score
            }
            else {
                bestScore=Math.min(Board.getScore(), 1); //lowest possible score
            }
        }
        else if(depth==0) {
            bestScore=calculateClusteringScore(Board.getBoard());
        }
        else {
            if(player == Player.USER) {
                for(Direction direction : Direction.values()) {
                    GameState newBoard = Board.Copy();

                    int points=newBoard.move(direction);
                    
                    if(points==0) {
                    	continue;
                    }
                    
                    Map<String, Object> currentResult = alphabeta(newBoard, depth-1, alpha, beta, Player.COMPUTER);
                    int currentScore=((Number)currentResult.get("Score")).intValue();
                                        
                    if(currentScore>alpha) { //maximize score
                        alpha=currentScore;
                        bestDirection=direction;
                    }
                    
                    if(beta<=alpha) {
                        break; //beta cutoff
                    }
                }
                
                bestScore = alpha;
            }
            else {
                List<Integer> moves = getEmptyCellIds(Board);
                int[] possibleValues = {2, 4};

                int i,j;
                abloop: for(Integer cellId : moves) {
                    i = cellId/Board.BOARD_SIZE;
                    j = cellId%Board.BOARD_SIZE;

                    for(int value : possibleValues) {
                        GameState newBoard = Board.Copy();
                        setEmptyCell(newBoard,i, j, value);

                        Map<String, Object> currentResult = alphabeta(newBoard, depth-1, alpha, beta, Player.USER);
                        int currentScore=((Number)currentResult.get("Score")).intValue();
                        if(currentScore<beta) { //minimize best score
                            beta=currentScore;
                        }
                        
                        if(beta<=alpha) {
                            break abloop; //alpha cutoff
                        }
                    }
                }
                
                bestScore = beta;
                
                if(moves.isEmpty()) {
                    bestScore=0;
                }
            }
        }
        
        result.put("Score", bestScore);
        result.put("Direction", bestDirection);
        
        return result;
    }
    
  
    private static List<Integer> getEmptyCellIds(GameState board) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * Calculates a heuristic variance-like score that measures how clustered the
     * board is.
     * 
     * @param boardArray
     * @return 
     */
    private static int calculateClusteringScore(int[][] boardArray) {
        int clusteringScore=0;
        
        int[] neighbors = {-1,0,1};
        
        for(int i=0;i<boardArray.length;++i) {
            for(int j=0;j<boardArray.length;++j) {
                if(boardArray[i][j]==0) {
                    continue; //ignore empty cells
                }
                
                //clusteringScore-=boardArray[i][j];
                
                //for every pixel find the distance from each neightbors
                int numOfNeighbors=0;
                int sum=0;
                for(int k : neighbors) {
                    int x=i+k;
                    if(x<0 || x>=boardArray.length) {
                        continue;
                    }
                    for(int l : neighbors) {
                        int y = j+l;
                        if(y<0 || y>=boardArray.length) {
                            continue;
                        }
                        
                        if(boardArray[x][y]>0) {
                            ++numOfNeighbors;
                            sum+=Math.abs(boardArray[i][j]-boardArray[x][y]);
                        }
                        
                    }
                }
                
                clusteringScore+=sum/numOfNeighbors;
            }
        }
        
        return clusteringScore;
    }

}


