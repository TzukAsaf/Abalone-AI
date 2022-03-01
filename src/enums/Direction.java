package enums;

import java.awt.*;

public enum Direction
{
    RIGHT(new Point(1,0)),
    LEFT(new Point(-1,0)),
    UPRIGHT(new Point(1, -1)),
    UPLEFT(new Point(0, -1)),
    DOWNRIGHT( new Point( 0, 1)),
    DOWNLEFT( new Point(-1,1));
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




}
