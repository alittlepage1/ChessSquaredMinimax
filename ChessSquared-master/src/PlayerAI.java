import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;


public class PlayerAI extends Player
{
    Random random;
    ChessBlock[][] board = new ChessBlock[8][8];
    ArrayList<ChessBlock> dangerousBlocks = new ArrayList<>();
    
    public PlayerAI(Color color)
    {
        super(color);
        random = new Random();
    }
    
    public ChessBlock[][] makeMove(ChessBlock[][] board, ArrayList<ChessBlock> dangerousBlocksForWhite, ArrayList<ChessBlock> dangerousBlocksForBlack)
    {
        this.board = board;
        ChessBlock currentBlock = null;
        ChessBlock currentMove = null;
        
        int[][] intBoard = new int[8][8];
        
        for (int y=7;y>=0;y--){
            for(int x=7;x>=0;x--){                  
                if(board[y][x].hasPiece()){
                    intBoard[y][x] = board[y][x].value;
                    if(this.color != board[y][x].getPiece().color)
                        intBoard[y][x] = intBoard[y][x] * (-1);
                }
                else intBoard[y][x] = 0;
            }
        }
        for (int y=0;y<8;y++){
            for(int x=0;x<8;x++){  
                    System.out.print(intBoard[y][x] +", ");
                if (x==7)
                    System.out.println();
            }
        }
        
        Point[] move = new Point[1]; 
        move = Minimax.Minimax(intBoard,3);
        
        currentBlock = board[move[0].y][move[0].x];
        currentMove = board[move[1].y][move[1].x];
        
        
        if (currentMove.hasPiece())
        {
            capturePiece(currentBlock, currentMove);
        }
        else
        {
            moveToBlock(currentBlock, currentMove);
        }
        
        return board;
    }
    
    private int EvaluateBoard(ChessBlock[][] board){
        int boardSum = 0;
        
        for(int i = 0; i <= 7; i++){
            for(int j = 0; j <= 7; j++){
                if(board[i][j].getPiece() != null){
                    if(board[i][j].getPiece().color == this.color){
                        boardSum += board[i][j].value;
                    }
                    else{
                        boardSum -= board[i][j].value;
                    }                    
                }
            }
        }        
        return boardSum;
    }
    private void capturePiece(ChessBlock selectedBlock, ChessBlock chessBlock)
    {
        if (chessBlock.getPiece().color == Color.WHITE)
        {
            ChessBoard.whitePieces.remove(chessBlock.getPiece());
        }
        else
        {
            ChessBoard.blackPieces.remove(chessBlock.getPiece());
        }
        
        chessBlock.setPiece(null);
        selectedBlock.getPiece().move(chessBlock);
        selectedBlock = null;
    }
    
    private void moveToBlock(ChessBlock selectedBlock, ChessBlock chessBlock)
    {
        selectedBlock.getPiece().move(chessBlock);
        selectedBlock = null;
    }
}