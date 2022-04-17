package GUI;

import Backend.BoardManage;
import enums.Direction;
import enums.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class BoardFrame extends JFrame implements MarbleListener
{
    private BoardPanel panel;
    private JButton right, left, upright, upleft, downright, downleft, undo;
    private JLabel blackCount, whiteCount;
    private BoardManage board;
    private ArrayList<Point> selected;// the marbles that the player has selected
    private int width,height, changewidth = 55;

    /**
     * the frame constructor
     * @param board
     */
    public BoardFrame(BoardManage board){

        setLayout(null);
        width=1000;
        height=600;
        setVisible(true);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel =new BoardPanel(this);
        setLocationRelativeTo(null);//make the screen pop up in middle
        setResizable(false);
        this.board = board;
        selected = new ArrayList<>();
        add(panel);
        panel.setBounds(0, 0, width, height);


        left = new JButton();
        right = new JButton();;

        downright = new JButton();
        downleft = new JButton();
        upright = new JButton();
        upleft = new JButton();

        undo = new JButton();

        blackCount = new JLabel("0");
        whiteCount = new JLabel("0");
        setButtons();
        setLabels();
        setBoard();
    }




    public void setBoard()
    {

        if(panel.get_board()==null)
        {
            panel.set_board(board.boardDescription());
            panel.initMarbles();
        }
        else
        {
            panel.set_board(board.boardDescription());
            panel.setPanelsByBoard();
        }
        setScores();
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
            undo.setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("../Assets/undo.png")))));

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

        undo.setOpaque(false);
        undo.setContentAreaFilled(false);
        undo.setBounds(770,450,75,100);
        undo.setLayout(null);
        undo.setVisible(true);
        undo.addActionListener(this::UndoActionPerformed);

        panel.add(right);
        panel.add(upright);
        panel.add(left);
        panel.add(downright);
        panel.add(upleft);
        panel.add(downleft);
        panel.add(undo);
        repaint();
    }

    public void setLabels()
    {
        blackCount.setLayout(null);
        blackCount.setBounds(750,20,90,90);
        blackCount.setFont(new Font("impact",Font.PLAIN, 40));
        blackCount.setForeground(Color.BLACK);

        whiteCount.setLayout(null);
        whiteCount.setBounds(836,20,90,90);
        whiteCount.setFont(new Font("impact",Font.PLAIN, 40));
        whiteCount.setForeground(Color.WHITE);



        panel.add(blackCount);
        panel.add(whiteCount);
        repaint();
    }

    public void setScores()
    {
        panel.remove(blackCount);
        panel.remove(whiteCount);
        blackCount.setText(String.valueOf(14- board.dataStructure.numOfBlacks));
        whiteCount.setText(String.valueOf(14- board.dataStructure.numOfWhites));
        panel.add(blackCount);
        panel.add(whiteCount);
        repaint();


    }

    public void aiMove()
    {
        if(board.computerTurn)
        {
            board.MakeAIMove();
            setBoard();

        }

        board.computerTurn = false;
    }


    /**
     * the function is in charge of managing the event of clicking a marble
     * @param pos
     */
    @Override
    public void MarbleSelected(Point pos)
    {
        if(board.gameOver /*|| ab.computerTurn*/)
            return;
        if(selected.contains(pos))
        {
            /*
            if the player clicked a marble that is already selected
            then cancel the selection of that marble
            */
            selected.remove(pos);
            panel.get_panels()[pos.y][pos.x].drawMarble();
        }
        else
        {
            //the selected marble hasn't been selected yet
            if(board.LegalAdd(pos, selected, panel.get_panels()[pos.y][pos.x].getPlayer()))
            {
                panel.get_panels()[pos.y][pos.x].markMarble();
                selected.add(pos);
            }

        }
        //System.out.println(pos);
        //System.out.println(selected);
    }
    private void RightActionPerformed(java.awt.event.ActionEvent evt)
    {
        board.MakeMove(Direction.RIGHT, selected, Player.WHITE);
        setBoard();
        aiMove();
    }

    private void LeftActionPerformed(java.awt.event.ActionEvent evt)
    {
        board.MakeMove(Direction.LEFT, selected, Player.WHITE);
        setBoard();
        aiMove();
    }

    private void UprightActionPerformed(java.awt.event.ActionEvent evt)
    {
        board.MakeMove(Direction.UPRIGHT, selected, Player.WHITE);
        setBoard();
        aiMove();
    }

    private void DownrightActionPerformed(java.awt.event.ActionEvent evt)
    {
        board.MakeMove(Direction.DOWNRIGHT, selected, Player.WHITE);
        setBoard();
        aiMove();
    }

    private void UpleftActionPerformed(java.awt.event.ActionEvent evt)
    {
        board.MakeMove(Direction.UPLEFT, selected, Player.WHITE);
        setBoard();
        aiMove();
    }

    private void DownleftActionPerformed(java.awt.event.ActionEvent evt)
    {
        board.MakeMove(Direction.DOWNLEFT, selected, Player.WHITE);
        setBoard();
        aiMove();
    }

    private void UndoActionPerformed(java.awt.event.ActionEvent evt)
    {
        if(!board.gameOver)
        {
            board.Undo();
            selected.clear();
            setBoard();
        }
    }

}
