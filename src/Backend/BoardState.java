package Backend;

import enums.Player;

public class BoardState
{
    //the class is in charge of keeping the state of the board behind the scenes
    private BoardManage board;


    public BoardState(int numOfRows, int numOfColsInFirstRow)
    {
        board = new BoardManage(numOfRows, numOfColsInFirstRow);

    }
    public void initBoard() {
        board.initBoard();
    }
    public Player[][] boardDescription() {
        return board.boardDescription();
    }

}
