package enums;

import javax.swing.*;
import java.awt.*;

public enum Direction
{
    RIGHT(null), LEFT(null), UPRIGHT(null), UPLEFT(null), DOWNRIGHT(null), DOWNLEFT(null);
    private Point move;

    Direction(Point p)
    {
        move = p;
    }
    public Point GetDirection()
    {
        return move;
    }

    public void SetNeighborsOffsets(Point pos, int rowsNum)
    {
        switch (this)
        {
            case RIGHT -> RIGHT.move = new Point(1,0);
            case LEFT -> LEFT.move = new Point(-1,0);
            case UPRIGHT -> UPRIGHT.move = new Point(1, -1);
            case UPLEFT -> UPLEFT.move = new Point(0, -1);
            case DOWNRIGHT -> DOWNRIGHT.move = new Point( 0, 1);
            case DOWNLEFT -> DOWNLEFT.move = new Point(-1,1);
        }
    }
    public static void SetNeighbors(Point currPos, int numOfRows)
    {
        for (Direction d : Direction.values())
        {
            d.SetNeighborsOffsets(currPos, numOfRows);
        }
    }

    public static Point AddOffsetToNeighbor(Point source,Point offset)
    {
        return new Point(source.x+offset.x,source.y+offset.y);
    }

}
