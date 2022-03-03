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
    public static boolean IsInMainDiag(Point p1, Point  p2)
    {
        Point newp = new Point();
        if(Objects.equals(p1, AddOffsetToNeighbor(p2, Direction.UPLEFT.GetMovementOffsetByCurrentLocation(p2, 9))) ||
                Objects.equals(p1, AddOffsetToNeighbor(p2, Direction.DOWNRIGHT.GetMovementOffsetByCurrentLocation(p2, 9))))
            return true;
        newp = AddOffsetToNeighbor(p2, Direction.UPLEFT.GetMovementOffsetByCurrentLocation(p2, 9));


        if(Objects.equals(p1, AddOffsetToNeighbor(newp, Direction.UPLEFT.GetMovementOffsetByCurrentLocation(newp, 9))))
            return true;

        newp = AddOffsetToNeighbor(p2, Direction.DOWNRIGHT.GetMovementOffsetByCurrentLocation(p2, 9));

        if(Objects.equals(p1, AddOffsetToNeighbor(newp, Direction.DOWNRIGHT.GetMovementOffsetByCurrentLocation(newp, 9))))
            return true;
        return false;
    }

    public static boolean IsInSecondaryDiag(Point p1, Point  p2)
    {
        Point newp = new Point();
        if(Objects.equals(p1, AddOffsetToNeighbor(p2, Direction.UPRIGHT.GetMovementOffsetByCurrentLocation(p2, 9))) ||
                Objects.equals(p1, AddOffsetToNeighbor(p2, Direction.DOWNLEFT.GetMovementOffsetByCurrentLocation(p2, 9))))
            return true;
        newp = AddOffsetToNeighbor(p2, Direction.UPRIGHT.GetMovementOffsetByCurrentLocation(p2, 9));


        if(Objects.equals(p1, AddOffsetToNeighbor(newp, Direction.UPRIGHT.GetMovementOffsetByCurrentLocation(newp, 9))))
            return true;

        newp = AddOffsetToNeighbor(p2, Direction.DOWNLEFT.GetMovementOffsetByCurrentLocation(p2, 9));

        if(Objects.equals(p1, AddOffsetToNeighbor(newp, Direction.DOWNLEFT.GetMovementOffsetByCurrentLocation(newp, 9))))
            return true;
        return false;
    }


}
