package Backend;

import enums.Direction;
import enums.Player;

import java.awt.*;
import java.util.ArrayList;

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
     * the function gets arraylist of the marbles that already been selected, and a desired point to put a new marble
     * @param newmarble
     * @param selectedmarbles
     * @param player
     * @return true if the marble can be legally added according to the game's rules.
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
                if(Direction.IsInMainDiagonal(selectedmarbles.get(0), selectedmarbles.get(1)))// if the two marbles in a diagonal starts from left
                {
                    if(!Direction.IsInMainDiagonal(selectedmarbles.get(0), newmarble))// the 3rd must be in same diagonal
                        return false;
                }
                else// the two marbles are in diagonal starts from right
                {
                    if(!Direction.IsInSecondaryDiagonal(selectedmarbles.get(0), newmarble))// the 3rd must be in same diagonal
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * @param dir
     * @param selectedmarbles
     * @return the new locations of the marbles, if the selected marbles can be legally moved to the desired direction. throws exception otherwise
     * @throws Exception
     */
    public ArrayList<Point> LegalMove(Direction dir,  ArrayList<Point> selectedmarbles) throws Exception {
        int playerMarbles = 0, computerMarbles = 0;
        Point pointsCounter = new Point();
        if(selectedmarbles.isEmpty())
        {
            throw new Exception("No marble selected");
        }
        ArrayList<Point> newLocationMarbles = NewLocations(selectedmarbles, dir);

        // if player tries to kill his own soldier
        for (Point selectedmarble : newLocationMarbles) {
            if (!IsPointInBoundsOfBoard(selectedmarble))
                throw new Exception("out of bounds");

        }

        return newLocationMarbles;

    }

    public boolean IsBroadsideMove(ArrayList<Point> selectedmarbles)
    {
        for (Point selectedmarble : selectedmarbles)
            if (selectedmarble.y != selectedmarbles.get(0).y)
                return false;
        return true;

    }

    /**
     *
     * @param selectedmarbles
     * @return arraylist of points after adding each of them the offset
     */
    public ArrayList<Point> NewLocations(ArrayList<Point> selectedmarbles, Direction offset)
    {

        ArrayList<Point> marblesAfterMove = new ArrayList<>();
        Point newPoint;
        for(int i = 0; i < selectedmarbles.size(); i++)
        {
            newPoint = Direction.AddOffsetToNeighbor(selectedmarbles.get(i), offset.GetMovementOffsetByCurrentLocation(selectedmarbles.get(i), 9));
            marblesAfterMove.add(newPoint);
        }
        System.out.println(marblesAfterMove);
        return marblesAfterMove;

    }

    public void MakeMove(Direction dir, ArrayList<Point> selectedmarbles){
        ArrayList<Point> newLocationMarbles;
        try
        {
            newLocationMarbles = LegalMove(dir, selectedmarbles);
            //if reached here, the move is legal
            //delete the marbles that have been moved
            for (Point selectedmarble : selectedmarbles) {
                dataStructure.setSquareContent(selectedmarble, null);
            }
            // set the marbles at their new locations
            for (Point newLocationMarble : newLocationMarbles) {
                dataStructure.setSquareContent(newLocationMarble, Player.WHITE);
            }

            selectedmarbles.clear();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }


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
     * @param x1
     * @param x2
     * @param x3
     * @return true if the three numbers are adjacent to each other
     */
    public static boolean Consecutive(int x1, int x2, int x3)
    {
        int min = Math.min(x1, Math.min(x2, x3));
        int max = Math.max(x1, Math.max(x2, x3));
        return max - min == 2 && x1 != x2 && x1 != x3 && x2 != x3;
    }


    /**
     * @param rowNum
     * @return how many columns there are in the given row
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
     * @param source
     * @return arraylist of all the neighbors of a given point
     */
    public ArrayList<Point> GetNeighbors(Point source) {
        Point neighbor;
        ArrayList<Point> neighbors = new ArrayList<>();
        Direction.setDirectionsOffsetsByCurrPos(source, numOfRows);
        for (Direction d : Direction.values())
        {
            neighbor = Direction.AddOffsetToNeighbor(source, d.GetMovementOffsetByCurrentLocation(source, 9));
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
