package AI;

import Backend.BoardManage;
import Backend.BoardStructure;
import enums.Player;

import java.awt.*;

public class Evaluate
{
    /**
     * defining a weight for each scenario
     */
    private int win = 9999;//if the move will get the AI a win
    private int pushOut = 200;// if the move will get the AI to push an enemy's marble out of the board
    private int dangerous = -200;// if the move will cause that an AI's marble is pushable by enemy's marbles
    private BoardStructure dataStructure;
    private BoardManage boardManage;
    public int GetEvaluationOfSinglePoint(Point pos, Player player)
    {
        int evaluation = 0;

        return evaluation;
    }

    public int evaluateAllBoard(Player player)
    {
        int colsInRow = boardManage.numOfColsInFirstRow;
        int rows = boardManage.numOfRows / 2;
        Point pos;
        int evaluation = 0;
        if(boardManage.Won(player))
            return win;
        assert player.getOpponent() != null;
        if(boardManage.Won(player.getOpponent()))
            return -win;

        //scan the board and evaluate every position
        // all the rows except the middle row
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colsInRow; j++) {
                pos = new Point(j, i);
                evaluation += GetEvaluationOfSinglePoint(pos, player);
                pos = new Point(j, boardManage.numOfRows - 1 - i);
                evaluation += GetEvaluationOfSinglePoint(pos, player);
            }
            colsInRow++;
        }
        // middle row
        for (int i = 0; i < colsInRow; i++) {
            pos = new Point(i, rows / 2);
            evaluation += GetEvaluationOfSinglePoint(pos, player);
        }
        return evaluation;


    }

}
