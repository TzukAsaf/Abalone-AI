package enums;

import javax.swing.*;
import java.awt.*;

public enum Direction
{
    RIGHT(new Point(1,0)),
    LEFT(new Point(-1,0)),
    UPRIGHT(new Point(1, -1)),
    UPLEFT(new Point(0, -1)),
    DOWNRIGHT( new Point( 0, 1)),
    DOWNLEFT( new Point(-1,1));
    private final Point move;

    Direction(Point p)
    {
        move = p;
    }
    public Point GetDirection()
    {
        return move;
    }



    public static Point AddOffsetToNeighbor(Point source,Point offset)
    {
        return new Point(source.x+offset.x,source.y+offset.y);
    }

}
