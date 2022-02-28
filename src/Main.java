import Backend.BoardState;
import GUI.BoardFrame;


import javax.swing.*;

public class Main extends JFrame
{


    public static void main(String[] args)
    {

        BoardState abalon = new BoardState();
        abalon.initBoard();
        new BoardFrame(abalon);


    }
}
