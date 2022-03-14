import Backend.BoardManage;
import GUI.BoardFrame;


import javax.swing.*;

public class Main extends JFrame
{


    public static void main(String[] args)
    {

        BoardManage abalon = new BoardManage();
        abalon.initBoard();
        new BoardFrame(abalon);


    }
}
