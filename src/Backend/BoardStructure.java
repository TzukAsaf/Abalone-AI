package Backend;

import enums.Player;

import java.awt.*;

public class BoardStructure
{
    private int size;
    private int numOfColsInFirstRow;
    private int numOfWhites;
    private int numOfBlacks;
    private MarbleManage[][] dataSet;



    public BoardStructure() {
        this.size = 9;
        this.numOfColsInFirstRow = 5;
        this.dataSet = new MarbleManage[size][size];
    }


    public BoardStructure(BoardStructure dataStructure)
    {
        //clone the board stats
        this.size = dataStructure.size;
        this.numOfBlacks = dataStructure.numOfBlacks;
        this.numOfWhites = dataStructure.numOfWhites;
        this.numOfColsInFirstRow = dataStructure.numOfColsInFirstRow;
        this.dataSet = dataStructure.cloneDataSet();
    }

    private MarbleManage[][] cloneDataSet() {
        MarbleManage[][] mat = new MarbleManage[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // if soldier exists in that spot
                if (dataSet[i][j] != null) {
                    mat[i][j] = new MarbleManage(dataSet[i][j].getPlayer());
                } else {
                    mat[i][j] = null;
                }
            }
        }
        return mat;
    }

    /**
     * @return the type of the player in the given point
     */
    public Player getSquareContent(Point pos)
    {

        if (dataSet[pos.y][pos.x] != null) {
            return dataSet[pos.y][pos.x].getPlayer();
        }
        return null;
    }

    /**
     * sets the given player to be in the given point
     */
    public void setSquareContent(Point pos, Player content) {
        if (content == null) {
            dataSet[pos.y][pos.x] = null;
        } else {
            dataSet[pos.y][pos.x] = new MarbleManage(content);
        }
    }

    /**
     * swaps between the values of the given points
     */
    public void swapSquaresContents(Point pos1, Point pos2) {
        Player c1 = null, c2 = null;
        if (dataSet[pos1.y][pos1.x] != null) {
            c1 = dataSet[pos1.y][pos1.x].getPlayer();
        }
        if (dataSet[pos2.y][pos2.x] != null) {
            c2 = dataSet[pos2.y][pos2.x].getPlayer();
        }
        setSquareContent(pos1, c2);
        setSquareContent(pos2, c1);
    }

    public void initBoard() {
        // initialize first board state
        numOfWhites = numOfBlacks = 0;
        int colsInRow = numOfColsInFirstRow;
        int rows = size / 2;
        Point pos;
        Player soldier;
        // all the rows except the middle row
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colsInRow; j++)
            {
                //the upper part of the hexagon
                pos = new Point(j, i);
                soldier = soldierInPos(pos);
                addSoldierToPos(pos, soldier);

                //the lower part of the hexagon
                pos = new Point(j, size - 1 - i);
                soldier = soldierInPos(pos);
                addSoldierToPos(pos, soldier);
            }
            colsInRow++;
        }
        // middle row
        for (int i = 0; i < colsInRow; i++) {
            pos = new Point(i, size / 2);
            soldier = soldierInPos(pos);
            addSoldierToPos(pos, soldier);
        }
    }
    private Player soldierInPos(Point pos) {
        // if first or second row
        if (pos.y == 0 || pos.y == 1 || (pos.y == 2 && pos.x > 1 && pos.x < 5)) {
            return Player.BLACK;
        }
        // if last and second last row
        if (pos.y == size - 1 || pos.y == size - 2
                || (pos.y == size - 3 && pos.x > 1 && pos.x < 5)) {
            return Player.WHITE;
        }
        return null;
    }

    public void addSoldierToPos(Point pos, Player player) {
        if (player != null) {
            dataSet[pos.y][pos.x] = new MarbleManage(player);
            incNumOfSoldiersOfPlayer(player);
        } else {
            dataSet[pos.y][pos.x] = null;
        }
    }


    public void incNumOfSoldiersOfPlayer(Player p) {
        if (p == Player.WHITE) {
            numOfWhites++;
        }
        if (p == Player.BLACK) {
            numOfBlacks++;
        }
    }

    public void decNumOfSoldiersOfPlayer(Player p) {
        if (p == Player.WHITE) {
            numOfWhites--;
        }
        if (p == Player.BLACK) {
            numOfBlacks--;
        }
    }

    private String generateSpaces(int num) {
        String st = "";
        for (int i = 1; i <= num * 1.5; i++) {
            st += " ";
        }
        return st;
    }

    // getters and setters
    public int getNumOfMarbles(Player player)
    {
        if(player == Player.WHITE)
            return numOfWhites;
        return numOfBlacks;
    }


    public Player[][] boardDescription() {
        Player[][] boardLayout = new Player[size][size];
        int colsInRow = numOfColsInFirstRow;
        int rows = size / 2;
        Point pos;
        // all the rows except the middle row
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colsInRow; j++) {
                pos = new Point(j, i);
                boardLayout[i][j] = getSquareContent(pos);
                pos = new Point(j, size - 1 - i);
                boardLayout[size - 1 - i][j] = getSquareContent(pos);
            }
            colsInRow++;
        }
        // middle row
        for (int i = 0; i < colsInRow; i++) {
            pos = new Point(i, size / 2);
            boardLayout[size / 2][i] = getSquareContent(pos);
        }
        return boardLayout;
    }


}
