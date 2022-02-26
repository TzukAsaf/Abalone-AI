package Backend;

import enums.Player;

public class BoardState
{
    //the class is in charge of keeping the state of the board behind the scenes
    private BoardManage board;


    public BoardState()
    {
        board = new BoardManage();

    }
    public void initBoard() {
        board.initBoard();
    }
    public BoardManage getBoard() {
        return board;
    }
    public Player[][] boardDescription() {
        return board.boardDescription();
    }

}
