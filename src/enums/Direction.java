package enums;

import java.awt.*;
import java.util.Objects;

public enum Direction
{
    RIGHT(null),
    LEFT(null),
    UPRIGHT(null),
    UPLEFT(null),
    DOWNRIGHT(null),
    DOWNLEFT( null);
    private Point move;

    Direction(Point p)
    {
        move = p;
    }

    public Point GetDirection()
    {
        return move;
    }

    public Point GetMovementOffsetByCurrentLocation(Point currPos, int numOfRows)
    {
        Direction d=this;
        return switch (d) {
            case UPRIGHT -> new Point(currPos.y / (numOfRows / 2 + 1), -1);
            case DOWNRIGHT -> new Point((currPos.y / (numOfRows / 2)) ^ 1, 1);
            case UPLEFT -> new Point(-((currPos.y / (numOfRows / 2 + 1)) ^ 1), -1);
            case DOWNLEFT -> new Point(-((currPos.y / (numOfRows / 2))), 1);
            case RIGHT -> new Point(1, 0);
            case LEFT -> new Point(-1, 0);
        };
    }

    public Direction GetOppositeDir()
    {
        Direction d=this;
        return switch (d) {
            case UPRIGHT -> DOWNLEFT;
            case DOWNRIGHT -> UPLEFT;
            case UPLEFT -> DOWNRIGHT;
            case DOWNLEFT -> UPRIGHT;
            case RIGHT ->LEFT;
            case LEFT -> RIGHT;
        };
    }
    public static Point AddOffsetToNeighbor(Point source,Point offset)
    {
        return new Point(source.x+offset.x,source.y+offset.y);
    }
    public static void setDirectionsOffsetsByCurrPos(Point currPos, int numOfRows)
    {
        for (Direction d : Direction.values()) {
            d.GetMovementOffsetByCurrentLocation(currPos, numOfRows);
        }
    }
    /**
     * @param p1
     * @param p2
     * @return true if the given points are in diagonal that starts from left (main)
     */
    public static boolean IsInMainDiagonal(Point p1, Point  p2)
    {
        Point newp;
        if(Objects.equals(p1, AddOffsetToNeighbor(p2, Direction.UPLEFT.GetMovementOffsetByCurrentLocation(p2, 9))) ||
                Objects.equals(p1, AddOffsetToNeighbor(p2, Direction.DOWNRIGHT.GetMovementOffsetByCurrentLocation(p2, 9))))
            //if one step to up left or down right is enough
            return true;

        //if it takes two steps up left/ down right to reach
        newp = AddOffsetToNeighbor(p2, Direction.UPLEFT.GetMovementOffsetByCurrentLocation(p2, 9));
        if(Objects.equals(p1, AddOffsetToNeighbor(newp, Direction.UPLEFT.GetMovementOffsetByCurrentLocation(newp, 9))))
            return true;

        newp = AddOffsetToNeighbor(p2, Direction.DOWNRIGHT.GetMovementOffsetByCurrentLocation(p2, 9));
        if(Objects.equals(p1, AddOffsetToNeighbor(newp, Direction.DOWNRIGHT.GetMovementOffsetByCurrentLocation(newp, 9))))
            return true;

        return false;
    }

    /**
     * @param p1
     * @param p2
     * @return true if the given points are in diagonal that starts from right (secondary)
     */
    public static boolean IsInSecondaryDiagonal(Point p1, Point  p2)
    {
        Point newp;
        if(Objects.equals(p1, AddOffsetToNeighbor(p2, Direction.UPRIGHT.GetMovementOffsetByCurrentLocation(p2, 9))) ||
                Objects.equals(p1, AddOffsetToNeighbor(p2, Direction.DOWNLEFT.GetMovementOffsetByCurrentLocation(p2, 9))))
            //if one step to up right or down left is enough
            return true;

        //if it takes two steps up right / down left to reach
        newp = AddOffsetToNeighbor(p2, Direction.UPRIGHT.GetMovementOffsetByCurrentLocation(p2, 9));
        if(Objects.equals(p1, AddOffsetToNeighbor(newp, Direction.UPRIGHT.GetMovementOffsetByCurrentLocation(newp, 9))))
            return true;
        newp = AddOffsetToNeighbor(p2, Direction.DOWNLEFT.GetMovementOffsetByCurrentLocation(p2, 9));
        if(Objects.equals(p1, AddOffsetToNeighbor(newp, Direction.DOWNLEFT.GetMovementOffsetByCurrentLocation(newp, 9))))
            return true;
        return false;
    }


}
