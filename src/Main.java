import Backend.BoardState;


import javax.swing.*;

public class Main extends JFrame
{


    public static void main(String[] args)
    {

        int numOfRows = 9, numOfColsInFirstRow = 5;

        BoardState abalon = new BoardState(numOfRows,
                numOfColsInFirstRow);


        abalon.initBoard();
        BoardFrame af = new BoardFrame(numOfRows,
                numOfColsInFirstRow, abalon);
        af.setLocationRelativeTo(null);//make the screen pop up in middle
        af.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        af.setResizable(false);
        af.setBoard();


    }
}
