import Backend.BoardState;


import javax.swing.*;

public class Main extends JFrame
{


    public static void main(String[] args)
    {

        BoardState abalon = new BoardState();
        abalon.initBoard();
        BoardFrame af = new BoardFrame(abalon);
        af.setLocationRelativeTo(null);//make the screen pop up in middle
        af.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        af.setResizable(false);
        af.setBoard();


    }
}
