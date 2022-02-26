package GUI;

import enums.Player;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel
{
    private MyImage backgroundImage;
    private int startX= 201;
    private int startY = 116;
    private int colsNum;// the number of columns in the current row
    private int rowsNum;
    private Player[][] board;// for each cell, which player has the possession

    private Marble[][] marbles;// array that contains the marbles
    private MarbleListener listener;
    private int marbleSize = 35;// size in pixels of each marble

    /**
     * the panel constructor
     * @param bf
     */
    public BoardPanel(BoardFrame bf)
    {
        rowsNum = 9;
        colsNum = 5;
        board = null;
        marbles = new Marble[rowsNum][rowsNum];
        backgroundImage = new MyImage("../Assets\\abalone_board.png",0,0,1000,565);
        listener = bf;
        repaint();
    }

    /**
     * the function initializes the marble's matrix and paints the screen with marbles
     */
    public void initMarbles()
    {
        int curX = startX, curY = startY;
        int colsInRow = colsNum;
        int adder = 2;
        int rows = rowsNum / 2;
        Point pos;
        for (int i = 0; i < rows; i++)// 0-4
        {
            for (int j = 0; j < colsInRow; j++)// 0-5, 0-6, 0-7, 0-8
            {
                //the upper half of the hexagon
                pos = new Point(j, i);
                marbles[i][j] = new Marble(pos, marbleSize,board[i][j]);
                marbles[i][j].addListener(listener);
                marbles[i][j].setBounds(curX, curY, marbleSize, marbleSize);
                add(marbles[i][j]);
                marbles[i][j].drawMarble();

                //the lower half of the hexagon
                pos = new Point(j, rowsNum - 1 - i);
                marbles[rowsNum - 1 - i][j] = new Marble(pos, marbleSize, board[rowsNum - 1 - i][j]);
                marbles[rowsNum- 1 - i][j].addListener(listener);
                marbles[rowsNum - 1 - i][j].setBounds(curX, startY + (marbleSize + 3)*(rowsNum - i - 1),marbleSize, marbleSize);
                add(marbles[rowsNum - 1 - i][j]);
                marbles[rowsNum - 1 - i][j].drawMarble();
                curX += marbleSize + 13;
            }
            curY += marbleSize + 5;

            curX = startX - (i + 1) * (marbleSize + 5) + (marbleSize / 2)
                    * (i + 1) ;

            colsInRow++;
        }
        //the middle row in the hexagon
        curX -= 4;
        curY -= 1;
        for (int i = 0; i < colsInRow; i++) {
            pos = new Point(i, rowsNum / 2);
            marbles[rowsNum / 2][i] = new Marble(pos, marbleSize,
                    board[rowsNum / 2][i]);
            marbles[rowsNum / 2][i].addListener(listener);
            marbles[rowsNum / 2][i].setBounds(curX, curY, marbleSize,
                    marbleSize);
            add(marbles[rowsNum / 2][i]);
            marbles[rowsNum / 2][i].drawMarble();
            curX += marbleSize + 13;
        }
    }

    /**
     * the function paints the screen with marbles as described in the board matrix
     *
     */
    public void setPanelsByBoard()
    {
        int colsInRow = colsNum;
        int rows = rowsNum / 2;
        // all the rows except the middle row
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colsInRow; j++)
            {
                //the upper half of the hexagon
                marbles[i][j].setPlayer(board[i][j]);
                marbles[i][j].drawMarble();

                //the lower half of the hexagon
                marbles[rowsNum - 1 - i][j].setPlayer(board[rowsNum - 1
                        - i][j]);
                marbles[rowsNum - 1 - i][j].drawMarble();
            }
            colsInRow++;
        }
        // middle row
        for (int i = 0; i < colsInRow; i++) {
            marbles[rowsNum / 2][i].setPlayer(board[rowsNum / 2][i]);
            marbles[rowsNum / 2][i].drawMarble();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        backgroundImage.drawImage(g);
    }

    public Player[][] get_board() {
        return board;
    }



    public void set_board(Player[][] _board) {
        this.board = _board;
    }




    public Marble[][] get_panels() {
        return marbles;
    }



}
