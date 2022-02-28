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

    public BoardManage() {
        this.numOfRows = 9;
        numOfColsInFirstRow = 5;
        dataStructure = new BoardStructure();
    }

    public BoardManage(BoardManage board)
    {
        numOfRows = board.numOfRows;
        numOfColsInFirstRow = board.numOfColsInFirstRow;
        dataStructure = new BoardStructure(board.dataStructure);
    }


    /**
     * the function gets arraylist of the marbles that already been selected, and a desired point to put a new marble,
     * and returns true if the marble can be legally added according to the game's rules.
     * @param newmarble
     * @param selectedmarbles
     * @param player
     * @return
     */
    public boolean LegalAdd(Point newmarble, ArrayList<Point> selectedmarbles, Player player)
    {
        if(player != Player.WHITE)// if the selected marble belongs to the AI
            return false;
        if(selectedmarbles.size() == 3)//if the player has already selected 3 marbles, no more can be added.
            return false;
        if(selectedmarbles.size() ==1)//the second marble must be a neighbor of the first one
        {
            if(!GetNeighbors(selectedmarbles.get(0)).contains(newmarble))// if they are not neighbors, don't allow
                return false;
        }
        if(selectedmarbles.size() == 2)//the 3rd marble must be in straight line to the others
        {
            if(IsInSameRow(selectedmarbles.get(0), selectedmarbles.get(1)))//if the two marbles are in the same row, the 3rd must be also in that row
            {
                if(!Consecutive(newmarble.x, selectedmarbles.get(0).x, selectedmarbles.get(1).x) || !IsInSameRow(selectedmarbles.get(0), newmarble))
                    // allow only if the selected marble is adjacent to the other two and in the same row
                    return false;
            }
            else// the two marbles are in diagonal, so the 3rd must be also
            {
                if(IsInMainDiagonal(selectedmarbles.get(0), selectedmarbles.get(1)))// if the two marbles in a diagonal starts from left
                {
                    if(!IsInMainDiagonal(selectedmarbles.get(0), newmarble))// the 3rd must be in same diagonal
                        return false;
                }
                else// the two marbles are in diagonal starts from right
                {
                    if(!IsInSecondaryDiagonal(selectedmarbles.get(0), newmarble))// the 3rd must be in same diagonal
                        return false;
                }
            }
        }
        return true;
    }

    public boolean LegalMove(String dir,  ArrayList<Point> selectedmarbles)
    {
        return false;
    }

    public void MakeMove(Point dir, ArrayList<Point> selectedmarbles)
    {
        System.out.println(dir.toString());

    }

    /**
     * the function returns true if the given points are in the same row
     * @param p1
     * @param p2
     * @return
     */
    public boolean IsInSameRow(Point p1, Point p2)
    {
        return p1.y == p2.y;
    }

    /**
     * the function returns true if the three numbers are adjacent to each other
     * @param x1
     * @param x2
     * @param x3
     * @return
     */
    public static boolean Consecutive(int x1, int x2, int x3)
    {
        int min = Math.min(x1, Math.min(x2, x3));
        int max = Math.max(x1, Math.max(x2, x3));
        return max - min == 2 && x1 != x2 && x1 != x3 && x2 != x3;
    }

    /**
     * the function returns true if the given points are in diagonal that starts from left (main)
     * @param p1
     * @param p2
     * @return
     */
    public boolean IsInMainDiagonal(Point p1, Point p2)
    {
        return (p1.x == p2.x && (Math.abs(p1.y - p2.y) == 1 || Math.abs(p1.y - p2.y) == 2));
    }

    /**
     * the function returns true if the given points are in diagonal that starts from right (secondary)
     * @param p1
     * @param p2
     * @return
     */
    public boolean IsInSecondaryDiagonal(Point p1, Point p2)
    {
        return (Math.abs(p1.y - p2.y) == 1 && Math.abs(p1.x - p2.x) == 1 || (Math.abs(p1.y - p2.y) == 2 && Math.abs(p1.x - p2.x) == 2));

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
