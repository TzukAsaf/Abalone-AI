package Backend;

import enums.Direction;
import enums.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class BoardManage
{
    //the class is in charge of defining the game's rules
    private final int minNumberOfSoldiers = 7;
    private final int maxNumberOfSoldiersToMove = 3;
    private final int kill = 20;
    private final int push = 10;
    private final int isolatedSoldier = 50;
    private int numOfRows;
    private int numOfColsInFirstRow;
    private BoardStructure dataStructure;

    public BoardManage(int numOfRows, int firstRowCols) {
        this.numOfRows = numOfRows;
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

    public boolean LegalAdd(Point newmarble, ArrayList<Point> selectedmarbles)
    {
        if(selectedmarbles.size() == 3)//if the player has already selected 3 marbles, no more can be added.
            return false;
        if(selectedmarbles.size() ==1)//the second marble must be neighbor of the first one
        {
            System.out.println(GetNeighbors(selectedmarbles.get(0)));
            if(!GetNeighbors(selectedmarbles.get(0)).contains(newmarble))
                return false;
        }

        return true;
    }

    /**
     * the function returns how many columns there are in the given row
     * @param rowNum
     * @return
     */
    private int numOfColsInRow(int rowNum)
    {
        if (rowNum < numOfRows / 2) {
            return rowNum + 5;
        }
        return -(rowNum - (numOfRows - 1)) + 5;
    }

    private boolean IsPointInBoundsOfBoard(Point pos) {
        return pos.y > -1 && pos.y < numOfRows && pos.x > -1
                && pos.x < numOfColsInRow(pos.y);
    }

    /**
     * the function returns arraylist of all the neighbors of a given point
     * @param source
     * @return
     */
    public ArrayList<Point> GetNeighbors(Point source) {
        Point neighbor;
        ArrayList<Point> neighbors = new ArrayList<>();
        Direction.SetNeighbors(source, numOfRows);
        for (Direction d : Direction.values()) {
            neighbor = Direction.AddOffsetToNeighbor(source, d.GetDirection());
            // if the coordinate is in the bounds of the board
            if (IsPointInBoundsOfBoard(neighbor)) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }



    public void initBoard() {
        dataStructure.initBoard();
    }
    public Player[][] boardDescription()
    {
        return dataStructure.boardDescription();
    }

}
