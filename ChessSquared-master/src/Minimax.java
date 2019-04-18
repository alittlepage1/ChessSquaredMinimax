/*
HashMap<Point,ArrayList<Point>> example;
        example = AllPossibleMovesForMinimax(testBoard, false);
        for (Point key: example.keySet()){ 
            String value = example.get(key).toString();  
            System.out.println("Piece at: (" + key.x + ", " + key.y  + ") can move to: " + value);  
        }
*/

 
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;


 
public class Minimax {
 
    
    public static void main(String[] args) { 
        int[][] testBoard = new int[8][8];
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                testBoard[y][x] = 0;
                if(x == 6)
                    testBoard[y][x] = 100;
                if(x == 1)
                    testBoard[y][x] = -100; 
            }
        }   
        testBoard[0][0] = -525;
        testBoard[0][7] = 525;
        testBoard[7][7] = 525;
        testBoard[7][0] = -525;
        
        testBoard[1][0] = -350;
        testBoard[1][7] = 350;
        testBoard[6][7] = 350;
        testBoard[6][0] = -350;
        
        testBoard[2][0] = -400;
        testBoard[2][7] = 400;
        testBoard[5][7] = 400;
        testBoard[5][0] = -400;
        
        testBoard[3][0] = -1000;
        testBoard[3][7] = 1000;
        testBoard[4][7] = 10000;
        testBoard[4][0] = -10000; 
        
        
        testBoard[4][5] = -100;
        
        for (int y=0;y<8;y++){
            for(int x=0;x<8;x++){  
                    System.out.print(testBoard[y][x] +", ");
                if (x==7)
                    System.out.println();
            }
        }  
        System.out.println(IsKingInCheck(testBoard, true));
    }
    
    public Minimax(){
    
    }
    
    
    public static Point[] Minimax(int[][] board, int targetDepth){
        Point[] bestMove = new Point[2];
        HashMap<Point,ArrayList<Point>> allPossibleMoves = new HashMap<>();
        allPossibleMoves = AllPossibleMoves(board, true);
        int bestScore = Integer.MIN_VALUE;
        int tempScore = 0;
        int betterMovesFoundCount = 0;
        
        int[][] tempBoard = DeepCopyArray(board);
        for(Point key: allPossibleMoves.keySet()){ 
            ArrayList<Point> list = allPossibleMoves.get(key);
            for(int i = 0; i < list.size(); i++){ 
                tempBoard = DeepCopyArray(MockMove(tempBoard,key, list.get(i)));
                tempScore = Minimise(tempBoard,1,targetDepth);
                if(tempScore>bestScore){
                    bestScore = tempScore;
                    bestMove[0] = key;
                    bestMove[1] = list.get(i);
                    betterMovesFoundCount++;
                }
            }
        }
        if(betterMovesFoundCount == 1)
        {
            System.out.println("An improved move was not found!");
        }
        
        return bestMove;
    }
     
    private static int Maximise(int[][] board, int depth, int targetDepth){ 
        if(IsKingInCheck(board, true))
            return Integer.MIN_VALUE;
        else if(depth == targetDepth)
            return EvaluateBoard(board);
        else{
            HashMap<Point,ArrayList<Point>> allPossibleMoves = new HashMap<>();
            allPossibleMoves = AllPossibleMoves(board, true);
            int bestScore = Integer.MIN_VALUE;
            int tempScore = 0;

            for(Point key: allPossibleMoves.keySet()){ 
                ArrayList<Point> list = allPossibleMoves.get(key);
                for(int i = 0; i < list.size(); i++){
                    int[][] tempBoard = DeepCopyArray(board);
                    tempBoard = DeepCopyArray(MockMove(tempBoard,key, list.get(i)));
                    tempScore = Minimise(tempBoard,depth+1,targetDepth);
                    if(tempScore>bestScore){
                        bestScore = tempScore; 
                    }
                }
            } 
            return bestScore;  
         } 
    }
    private static int Minimise(int[][] board, int depth, int targetDepth){ 
        if(IsKingInCheck(board, false))
            return Integer.MAX_VALUE;
        if(depth == targetDepth)
             return EvaluateBoard(board);
         else{
            HashMap<Point,ArrayList<Point>> allPossibleMoves = new HashMap<>();
            allPossibleMoves = AllPossibleMoves(board, false);
            int bestScore = Integer.MAX_VALUE;
            int tempScore = 0;

            for(Point key: allPossibleMoves.keySet()){ 
                ArrayList<Point> list = allPossibleMoves.get(key);
                for(int i = 0; i < list.size(); i++){
                    int[][] tempBoard = DeepCopyArray(board);
                    tempBoard = DeepCopyArray(MockMove(tempBoard,key, list.get(i)));
                    tempScore = Maximise(tempBoard,depth+1,targetDepth);
                    if(tempScore<bestScore){
                        bestScore = tempScore; 
                    }
                }
            } 
            return bestScore;  
         } 
    }
    private static boolean IsKingInCheck(int[][] board, boolean aiTurn){ 
        HashMap<Point, ArrayList<Point>> allOpponentMoves = AllPossibleMoves(board, !aiTurn);
        if(aiTurn){
          for(Point key: allOpponentMoves.keySet()){ 
                ArrayList<Point> list = allOpponentMoves.get(key);
                for(int i = 0; i < list.size(); i++){
                     if(board[list.get(i).y][list.get(i).x] == 10000)
                         return true;
                }
            }   
        } 
        return false;
    } 
    private static int EvaluateBoard(int[][] board){
        int boardSum= 0;
        for(int i = 0; i <= 7; i++){
            for(int j = 0; j <= 7; j++){ 
                boardSum = board[i][j] + boardSum;
            }
        }
        return boardSum;
    }
    
    private static int[][] MockMove(int[][] board, Point start, Point destination){
        int startX = start.x;
        int startY = start.y;
        int destX = destination.x;
        int destY = destination.y;
        int[][] newBoard = DeepCopyArray(board); 
        try{ 
            if(newBoard[destY][destX] == 0){
                int newDestValue = newBoard[startY][startX];
                int newStartValue = newBoard[destY][destX];
                newBoard[startY][startX] = newStartValue;
                newBoard[destY][destX] = newDestValue;
            }
            else if(newBoard[destY][destX] != 0){
                int newDestValue = newBoard[startY][startX];
                int newStartValue = 0;
                newBoard[startY][startX] = newStartValue;
                newBoard[destY][destX] = newDestValue; 
            }
        }
        catch(Exception e){
        }
        return newBoard;
    }
    
    private static int[][] DeepCopyArray(int[][] input) { 
        int[][] result = new int[input.length][];
        for (int r = 0; r < input.length; r++) {    
            result[r] = input[r].clone();
        }
        return result;
    }
    
    
    
    private static HashMap<Point,ArrayList<Point>> AllPossibleMoves(int[][] board, boolean aiTurn){
        HashMap<Point,ArrayList<Point>> allPossibleMoves = new HashMap<>(); 
        int i = 1;
        if(!aiTurn)
            i = -1;
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                switch (board[y][x]*i) {
                    case 100://pawn 
                        allPossibleMoves.put(new Point(x,y), Pawn(board, x, y, aiTurn));
                        break;
                    case 350://knight 
                        allPossibleMoves.put(new Point(x,y), Knight(board, x, y, aiTurn));
                        break;
                    case 400://bishop
                        allPossibleMoves.put(new Point(x,y), Bishop(board, x, y, aiTurn)); 
                        break;
                    case 525://rook
                        allPossibleMoves.put(new Point(x,y), Rook(board, x, y, aiTurn));
                        break;
                    case 1000://queen
                        allPossibleMoves.put(new Point(x,y), Queen(board, x, y, aiTurn));
                        break;
                    case 10000://king
                        allPossibleMoves.put(new Point(x,y), King(board, x, y, aiTurn));
                        break;
                    default:
                        break;
                } 
            }
        } 
        return allPossibleMoves;
    }
    private static ArrayList<Point> King(int[][] board, int x, int y, boolean aiTurn){
        ArrayList<Point> kingMoves = new ArrayList<>();
        if(aiTurn){
            try{ 
                if(board[y+1][x+1] <=0){
                    kingMoves.add(new Point(x+1, y+1));
                }
            }catch(Exception e){}
            try{ 
                if(board[y-1][x+1] <=0){
                    kingMoves.add(new Point(x+1, y-1));
                }
            }catch(Exception e){}
            try{ 
                if(board[y-1][x-1] <=0){
                    kingMoves.add(new Point(x-1, y-1));
                }
            }catch(Exception e){}
            try{ 
                if(board[y+1][x-1] <=0){
                    kingMoves.add(new Point(x-1, y+1));
                }
            }catch(Exception e){}
            try{ 
                if(board[y][x-1] <=0){
                    kingMoves.add(new Point(x-1, y));
                }
            }catch(Exception e){}
            try{ 
                if(board[y][x+1] <=0){
                    kingMoves.add(new Point(x+1, y));
                }
            }catch(Exception e){}
            try{ 
                if(board[y-1][x] <=0){
                    kingMoves.add(new Point(x, y-1));
                }
            }catch(Exception e){}
            try{ 
                if(board[y+1][x] <=0){
                    kingMoves.add(new Point(x, y+1));
                }
            }catch(Exception e){}
        } else{ 
            try{ 
            if(board[y+1][x+1] >=0){
                kingMoves.add(new Point(x+1, y+1));
            }
            }catch(Exception e){}
            try{ 
                if(board[y-1][x+1] >=0){
                    kingMoves.add(new Point(x+1, y-1));
                }
            }catch(Exception e){}
            try{ 
                if(board[y-1][x-1] <=0){
                    kingMoves.add(new Point(x-1, y-1));
                }
            }catch(Exception e){}
            try{ 
                if(board[y+1][x-1] >=0){
                    kingMoves.add(new Point(x-1, y+1));
                }
            }catch(Exception e){}
            try{ 
                if(board[y][x-1] >=0){
                    kingMoves.add(new Point(x-1, y));
                }
            }catch(Exception e){}
            try{ 
                if(board[y][x+1] >=0){
                    kingMoves.add(new Point(x+1, y));
                }
            }catch(Exception e){}
            try{ 
                if(board[y-1][x] >=0){
                    kingMoves.add(new Point(x, y-1));
                }
            }catch(Exception e){}
            try{ 
                if(board[y+1][x] >=0){
                    kingMoves.add(new Point(x, y+1));
                }
            }catch(Exception e){} 
        }
        return kingMoves;
    }
    private static ArrayList<Point> Queen(int[][] board, int x, int y, boolean aiTurn){
        ArrayList<Point> queenMoves = new ArrayList<>();
        if(aiTurn){
            try{ for(int i = 1; i < 8; i++){
                    if(board[y][x+i] > 0)
                        break; 
                    queenMoves.add(new Point(x+i, y));
                    if(board[y][x+i] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y][x-i] > 0)
                        break; 
                    queenMoves.add(new Point(x-i, y));
                    if(board[y][x-i] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y+i][x] > 0)
                        break; 
                    queenMoves.add(new Point(x, y+i));
                    if(board[y+i][x] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x] > 0)
                        break; 
                    queenMoves.add(new Point(x, y-i));
                    if(board[y-i][x] < 0)
                        break;
                }
            } catch(Exception e){ }
        }
        else{
            try{ for(int i = 1; i < 8; i++){
                    if(board[y][x+i] < 0)
                        break; 
                    queenMoves.add(new Point(x+i, y));
                    if(board[y][x+i] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y][x-i] < 0)
                        break; 
                    queenMoves.add(new Point(x-i, y));
                    if(board[y][x-i] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y+i][x] < 0)
                        break; 
                    queenMoves.add(new Point(x, y+i));
                    if(board[y+i][x] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x] < 0)
                        break; 
                    queenMoves.add(new Point(x, y-i));
                    if(board[y-i][x] > 0)
                        break;
                }
            } catch(Exception e){ }
        }
        
        if(aiTurn){
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x-i] > 0)
                        break; 
                    queenMoves.add(new Point(x-i, y-i));
                    if(board[y-i][x-i] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y+i][x-i] > 0)
                        break; 
                    queenMoves.add(new Point(x-i, y+i));
                    if(board[y+i][x-i] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x+i] > 0)
                        break; 
                    queenMoves.add(new Point(x-i, y+i));
                    if(board[y-i][x+i] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x+i] > 0)
                        break; 
                    queenMoves.add(new Point(x+i, y-i));
                    if(board[y-i][x+i] < 0)
                        break;
                }
            } catch(Exception e){ }
        }
        else{
            try{ for(int i = 1; i < 8; i++){
                    if(board[y+i][x+i] < 0)
                        break; 
                    queenMoves.add(new Point(x+i, y+i));
                    if(board[y+i][x+i] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x+i] < 0)
                        break; 
                    queenMoves.add(new Point(x+i, y-i));
                    if(board[y-i][x+i] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y+i][x-i] < 0)
                        break; 
                    queenMoves.add(new Point(x-i, y+i));
                    if(board[y+i][x-i] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x-i] < 0)
                        break; 
                    queenMoves.add(new Point(x-i, y-i));
                    if(board[y-i][x-i] > 0)
                        break;
                }
            } catch(Exception e){ }
        }
        return queenMoves;
    }
    private static ArrayList<Point> Rook(int[][] board, int x, int y, boolean aiTurn){
        ArrayList<Point> rookMoves = new ArrayList<>();
        if(aiTurn){
            try{ for(int i = 1; i < 8; i++){
                    if(board[y][x+i] > 0)
                        break; 
                    rookMoves.add(new Point(x+i, y));
                    if(board[y][x+i] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y][x-i] > 0)
                        break; 
                    rookMoves.add(new Point(x-i, y));
                    if(board[y][x-i] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y+i][x] > 0)
                        break; 
                    rookMoves.add(new Point(x, y+i));
                    if(board[y+i][x] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x] > 0)
                        break; 
                    rookMoves.add(new Point(x, y-i));
                    if(board[y-i][x] < 0)
                        break;
                }
            } catch(Exception e){ }
        }
        else{
            try{ for(int i = 1; i < 8; i++){
                    if(board[y][x+i] < 0)
                        break; 
                    rookMoves.add(new Point(x+i, y));
                    if(board[y][x+i] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y][x-i] < 0)
                        break; 
                    rookMoves.add(new Point(x-i, y));
                    if(board[y][x-i] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y+i][x] < 0)
                        break; 
                    rookMoves.add(new Point(x, y+i));
                    if(board[y+i][x] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x] < 0)
                        break; 
                    rookMoves.add(new Point(x, y-i));
                    if(board[y-i][x] > 0)
                        break;
                }
            } catch(Exception e){ }
        }
        
        return rookMoves;
    }
    private static ArrayList<Point> Bishop(int[][] board, int x, int y, boolean aiTurn){
        ArrayList<Point> bishopMoves = new ArrayList<>();
        if(aiTurn){
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x-i] > 0)
                        break; 
                    bishopMoves.add(new Point(x-i, y-i));
                    if(board[y-i][x-i] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y+i][x-i] > 0)
                        break; 
                    bishopMoves.add(new Point(x-i, y+i));
                    if(board[y+i][x-i] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x+i] > 0)
                        break; 
                    bishopMoves.add(new Point(x-i, y+i));
                    if(board[y-i][x+i] < 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x+i] > 0)
                        break; 
                    bishopMoves.add(new Point(x+i, y-i));
                    if(board[y-i][x+i] < 0)
                        break;
                }
            } catch(Exception e){ }
        }
        else{
            try{ for(int i = 1; i < 8; i++){
                    if(board[y+i][x+i] < 0)
                        break; 
                    bishopMoves.add(new Point(x+i, y+i));
                    if(board[y+i][x+i] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x+i] < 0)
                        break; 
                    bishopMoves.add(new Point(x+i, y-i));
                    if(board[y-i][x+i] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y+i][x-i] < 0)
                        break; 
                    bishopMoves.add(new Point(x-i, y+i));
                    if(board[y+i][x-i] > 0)
                        break;
                }
            } catch(Exception e){ }
            try{ for(int i = 1; i < 8; i++){
                    if(board[y-i][x-i] < 0)
                        break; 
                    bishopMoves.add(new Point(x-i, y-i));
                    if(board[y-i][x-i] > 0)
                        break;
                }
            } catch(Exception e){ }
            
        }
        return bishopMoves;
    }
    private static ArrayList<Point> Knight(int[][] board, int x, int y, boolean aiTurn){
        ArrayList<Point> knightMoves = new ArrayList<>();
        if(aiTurn){
            try{if(board[y+1][x+2] <=0){
                    knightMoves.add(new Point(x+2, y+1));
                }
            } catch(Exception e){ } 
            try{if(board[y-1][x+2] <=0){
                    knightMoves.add(new Point(x+2, y-1));
                }
            } catch(Exception e){ }
            try{if(board[y+1][x-2] <=0){
                    knightMoves.add(new Point(x-2, y+1));
                }
            } catch(Exception e){ } 
            try{if(board[y-1][x-2] <=0){
                    knightMoves.add(new Point(x-2, y-1));
                }
            } catch(Exception e){ } 
            try{if(board[y+2][x+1] <=0){
                    knightMoves.add(new Point(x+1, y+2));
                }
            } catch(Exception e){ } 
            try{if(board[y-2][x+1] <=0){
                    knightMoves.add(new Point(x+1, y-2));
                }
            } catch(Exception e){ }

            try{if(board[y+2][x-1] <=0){
                    knightMoves.add(new Point(x-1, y+2));
                }
            } catch(Exception e){ } 
            try{if(board[y-2][x-1] <=0){
                    knightMoves.add(new Point(x-1, y-2));
                }
            } catch(Exception e){ }
        } 
        else{
            try{if(board[y+1][x+2] >=0){
                    knightMoves.add(new Point(x+2, y+1));
                }
            } catch(Exception e){ } 
            try{if(board[y-1][x+2] >=0){
                    knightMoves.add(new Point(x+2, y-1));
                }
            } catch(Exception e){ }
            try{if(board[y+1][x-2] >=0){
                    knightMoves.add(new Point(x-2, y+1));
                }
            } catch(Exception e){ } 
            try{if(board[y-1][x-2] >=0){
                    knightMoves.add(new Point(x-2, y-1));
                }
            } catch(Exception e){ } 
            try{if(board[y+2][x+1] >=0){
                    knightMoves.add(new Point(x+1, y+2));
                }
            } catch(Exception e){ } 
            try{if(board[y-2][x+1] >=0){
                    knightMoves.add(new Point(x+1, y-2));
                }
            } catch(Exception e){ }

            try{if(board[y+2][x-1] >=0){
                    knightMoves.add(new Point(x-1, y+2));
                }
            } catch(Exception e){ } 
            try{if(board[y-2][x-1] >=0){
                    knightMoves.add(new Point(x-1, y-2));
                }
            } catch(Exception e){ }
        }  
        return knightMoves;
    }
    private static ArrayList<Point> Pawn(int[][] board, int x, int y, boolean aiTurn){ 
        ArrayList<Point> pawnMoves = new ArrayList<>();
            if(aiTurn){
                try{
                    if(x == 6){
                        if(board[y][x-1] == 0)
                            pawnMoves.add(new Point(x-1,y));
                        if(board[y][x-2] == 0)
                            pawnMoves.add(new Point(x-2,y));
                    }
                    else{ 
                        if(board[y][x-1] ==0)
                            pawnMoves.add(new Point(x-1,y));
                    }
                } catch(Exception e){}
                try{
                    if(y == 0){
                        if(board[y+1][x-1] < 0)
                            pawnMoves.add(new Point(x-1, y+1));
                    }
                    else if(y > 0 && y < 7){
                        if(board[y+1][x-1] < 0)
                            pawnMoves.add(new Point(x-1, y+1));
                        else if(board[y-1][x-1] <0)
                            pawnMoves.add(new Point(x-1, y-1));
                    }
                    else if(y == 0){
                        if(board[y-1][x-1] < 0)
                            pawnMoves.add(new Point(x-1, y-1));
                    }
                } catch(Exception e){}
            } 
            else{
                try{
                    if(x == 1){
                        if(board[y][x+1] == 0)
                            pawnMoves.add(new Point(x+1,y));
                        if(board[y][x+2] == 0)
                            pawnMoves.add(new Point(x+2,y));
                    }
                    else{ 
                        if(board[y][x+1] ==0 )
                            pawnMoves.add(new Point(x+1,y));
                    }
                }catch(Exception e){}
                try{
                    if(y == 0){
                        if(board[y+1][x+1] > 0){
                            pawnMoves.add(new Point(x+1, y+1));
                        }
                    }
                    else if(y > 0 && y < 7){
                        if(board[y+1][x+1] > 0){
                            pawnMoves.add(new Point(x+1, y+1));}
                        else if(board[y-1][x+1] >0){
                            pawnMoves.add(new Point(x+1, y-1));}
                    }
                    else if(y == 7){
                        if(board[y-1][x+1] > 0){
                            pawnMoves.add(new Point(x+1, y-1));}
                    }
                } catch(Exception e){}
            } 
            
        return pawnMoves;
    }
    
}
