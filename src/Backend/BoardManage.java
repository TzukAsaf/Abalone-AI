package Backend;

import enums.Player;

public class BoardManage
{
    //the class is in charge of defining the game's rules
    private final int _minNumberOfSoldiers = 7;
    private final int _maxNumberOfSoldiersToMove = 3;
    private final int _kill = 20;
    private final int _push = 10;
    private final int _isolatedSoldier = 50;
    private int numOfRows;
    private int numOfColsInFirstRow;
    private BoardStructure dataStructure;

    public BoardManage(int numOfRows, int firstRowCols) {
        numOfRows = numOfRows;
        numOfColsInFirstRow = firstRowCols;
        dataStructure = new BoardStructure(numOfRows,
                numOfColsInFirstRow);
    }

    public BoardManage(BoardManage board)
    {
        numOfRows = board.numOfRows;
        numOfColsInFirstRow = board.numOfColsInFirstRow;
        dataStructure = new BoardStructure(board.dataStructure);
    }

    public void initBoard() {
        dataStructure.initBoard();
    }
    public Player[][] boardDescription()
    {
        return dataStructure.boardDescription();
    }

}
