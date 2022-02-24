import Backend.BoardState;
import enums.Player;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class BoardFrame extends JFrame implements MarbleListener
{
    private BoardPanel bp;
    //private AbalonBoardGame ab;
    //private GameBoardAI<Board<AbalonBoardDataStructure, AbalonSoldier[][]>> _ai;
    private Point source,dest;
    private Vector<Point> points;
    private Player winner;
    private JButton reset;
    private JButton exit;
    private  BoardState ab;
    private int width,height;

    /**
     * the frame constructor
     * @param numOfRows
     * @param numOfColsInFirstRow
     * @param ab
     */
    public BoardFrame(int numOfRows, int numOfColsInFirstRow, BoardState ab)
    {

        setLayout(null);
        width=1000;
        height=600;
        setVisible(true);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bp=new BoardPanel(numOfRows,numOfColsInFirstRow,this);
        this.ab = ab;

        add(bp);
        bp.setBounds(0, 0, width, height);
        source = dest = null;
        winner = null;
    }




    public void setBoard()
    {

        if(bp.get_board()==null)
        {
            bp.set_board(ab.boardDescription());
            bp.initMarbles();
        }
        else
        {
            bp.set_board(ab.boardDescription());
            bp.setPanelsByBoard();
        }
    }


    /**
     * the function is in charge of managing the event of clicking a marble
     * @param pos
     */
    @Override
    public void MarbleSelected(Point pos)
    {

        bp.get_panels()[pos.y][pos.x].markMarble();

    }
}
