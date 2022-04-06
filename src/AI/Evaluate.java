package AI;

import Backend.BoardManage;
import Backend.BoardStructure;
import GUI.Marble;
import enums.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Evaluate
{
    /**
     * defining a weight for each scenario
     */
    private int win = 9999;//if the move will get the AI a win
    private int pushOut = 200;// if the move will get the AI to push an enemy's marble out of the board
    private int dangerous = -200;// if the move will cause that an AI's marble is pushable by enemy's marbles
    private BoardManage boardManage;
    private BoardStructure dataStructure;

    public Evaluate(BoardStructure dataStructure, BoardManage boardManage)
    {
        this.dataStructure = dataStructure;
    }

    public int BallsDistancesToEdge(BoardStructure dataStructure, Player player)
    {

        //System.out.println(dataStructure.numOfBlacks - dataStructure.numOfWhites);
        return (dataStructure.numOfBlacks - dataStructure.numOfWhites);
    }

    public int evaluateAllBoard(BoardStructure dataStructure, Player p)
    {
        return BallsDistancesToEdge(dataStructure, p);

    }


}
