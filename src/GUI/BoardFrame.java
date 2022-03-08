package GUI;

import Backend.BoardState;
import enums.Direction;
import enums.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;
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
    private JButton right, left, upright, upleft, downright, downleft;
    private  BoardState ab;
    private ArrayList<Point> selected;// the marbles that the player has selected
    private int width,height, changewidth = 55;

    /**
     * the frame constructor
     * @param ab
     */
    public BoardFrame(BoardState ab){

        setLayout(null);
        width=1000;
        height=600;
        setVisible(true);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bp=new BoardPanel(this);
        setLocationRelativeTo(null);//make the screen pop up in middle
        setResizable(false);
        this.ab = ab;
        selected = new ArrayList<>();
        add(bp);
        bp.setBounds(0, 0, width, height);
        source = dest = null;
        winner = null;

        left = new JButton();
        right = new JButton();;

        downright = new JButton();
        downleft = new JButton();
        upright = new JButton();
        upleft = new JButton();
        setButtons();
        setBoard();
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

    public void setButtons()
    {
        try
        {
            right.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("../Assets/right.png")))));
            left.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("../Assets/left.png")))));
            upright.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("../Assets/upright.png")))));
            downright.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("../Assets/downright.png")))));
            upleft.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("../Assets/upleft.png")))));
            downleft.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("../Assets/downleft.png")))));

        } catch (Exception ex)
        {
            System.out.println(ex);
        }
        right.setOpaque(false);
        right.setContentAreaFilled(false);
        right.setBounds(860-changewidth,250,90,90);
        right.setLayout(null);
        right.setVisible(true);
        right.addActionListener(this::RightActionPerformed);

        left.setOpaque(false);
        left.setContentAreaFilled(false);
        left.setBounds(770-changewidth,250,90,90);
        left.setLayout(null);
        left.setVisible(true);
        left.addActionListener(this::LeftActionPerformed);


        upright.setOpaque(false);
        upright.setContentAreaFilled(false);
        upright.setBounds(860-changewidth,160,90,90);
        upright.setLayout(null);
        upright.setVisible(true);
        upright.addActionListener(this::UprightActionPerformed);


        downright.setOpaque(false);
        downright.setContentAreaFilled(false);
        downright.setBounds(860-changewidth,340,90,90);
        downright.setLayout(null);
        downright.setVisible(true);
        downright.addActionListener(this::DownrightActionPerformed);


        upleft.setOpaque(false);
        upleft.setContentAreaFilled(false);
        upleft.setBounds(770-changewidth,160,90,90);
        upleft.setLayout(null);
        upleft.setVisible(true);
        upleft.addActionListener(this::UpleftActionPerformed);


        downleft.setOpaque(false);
        downleft.setContentAreaFilled(false);
        downleft.setBounds(770-changewidth,340,90,90);
        downleft.setLayout(null);
        downleft.setVisible(true);
        downleft.addActionListener(this::DownleftActionPerformed);



        bp.add(right);
        bp.add(upright);
        bp.add(left);
        bp.add(downright);
        bp.add(upleft);
        bp.add(downleft);
        repaint();
    }


    /**
     * the function is in charge of managing the event of clicking a marble
     * @param pos
     */
    @Override
    public void MarbleSelected(Point pos)
    {
        if(selected.contains(pos))
        {
            /*
            if the player clicked a marble that is already selected
            then cancel the selection of that marble
            */
            selected.remove(pos);
            bp.get_panels()[pos.y][pos.x].drawMarble();
        }
        else
        {
            //the selected marble hasn't been selected yet
            if(ab.getBoard().LegalAdd(pos, selected, bp.get_panels()[pos.y][pos.x].getPlayer()))
            {
                bp.get_panels()[pos.y][pos.x].markMarble();
                selected.add(pos);
            }

        }
        //System.out.println(pos);
        System.out.println(selected);
    }
    private void RightActionPerformed(java.awt.event.ActionEvent evt)
    {
        ab.getBoard().MakeMove(Direction.RIGHT, selected, Player.WHITE);
        setBoard();
    }

    private void LeftActionPerformed(java.awt.event.ActionEvent evt)
    {
        ab.getBoard().MakeMove(Direction.LEFT, selected, Player.WHITE);
        setBoard();
    }

    private void UprightActionPerformed(java.awt.event.ActionEvent evt)
    {
        ab.getBoard().MakeMove(Direction.UPRIGHT, selected, Player.WHITE);
        setBoard();
    }

    private void DownrightActionPerformed(java.awt.event.ActionEvent evt)
    {
        ab.getBoard().MakeMove(Direction.DOWNRIGHT, selected, Player.WHITE);
        setBoard();
    }

    private void UpleftActionPerformed(java.awt.event.ActionEvent evt)
    {
        ab.getBoard().MakeMove(Direction.UPLEFT, selected, Player.WHITE);
        setBoard();
    }

    private void DownleftActionPerformed(java.awt.event.ActionEvent evt)
    {
        ab.getBoard().MakeMove(Direction.DOWNLEFT, selected, Player.WHITE);
        setBoard();
    }

}
