package Backend;

import AI.Node;
import enums.Direction;
import enums.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class BoardManage
{
    //the class is in charge of defining the game's rules
    public boolean gameOver = false;
    public int numOfRows;
    public int numOfColsInFirstRow;
    public BoardStructure dataStructure;
    public boolean computerTurn = false;
    private int level;
    public Stack<BoardStructure> undoStack;
    public String error;

    public BoardManage() {
        this.numOfRows = 9;
        numOfColsInFirstRow = 5;
        dataStructure = new BoardStructure();
        level = 2;
        undoStack = new Stack<>();
    }



    /**
     * the function gets arraylist of the marbles that already been selected, and a desired point to put a new marble
     * @param newmarble
     * @param selectedmarbles
     * @param player
     * @return true if the marble can be legally added according to the game's rules.
     */
    public boolean LegalAdd(Point newmarble, ArrayList<Point> selectedmarbles, Player player)
    {
        if(player != Player.WHITE)// if the selected marble belongs to the AI
            return false;
        if(selectedmarbles.size() == 3)//if the player has already selected 3 marbles, no more can be added.
            return false;
        if(selectedmarbles.size() ==1)//the second marble must be a neighbor of the first one
        {
            if(!GetNeighbors(selectedmarbles.get(0)).contains(newmarble))// if they are not neighbors, don't allow
                return false;
        }
        if(selectedmarbles.size() == 2)//the 3rd marble must be in straight line to the others
        {
            if(IsInSameRow(selectedmarbles.get(0), selectedmarbles.get(1)))//if the two marbles are in the same row, the 3rd must be also in that row
            {
                if(!Consecutive(newmarble.x, selectedmarbles.get(0).x, selectedmarbles.get(1).x) || !IsInSameRow(selectedmarbles.get(0), newmarble))
                    // allow only if the selected marble is adjacent to the other two and in the same row
                    return false;
            }
            else// the two marbles are in diagonal, so the 3rd must be also
            {
                if(Direction.IsInMainDiagonal(selectedmarbles.get(0), selectedmarbles.get(1)))// if the two marbles in a diagonal starts from left
                {
                    if(!Direction.IsInMainDiagonal(selectedmarbles.get(0), newmarble))// the 3rd must be in same diagonal
                        return false;
                }
                else// the two marbles are in diagonal starts from right
                {
                    if(!Direction.IsInSecondaryDiagonal(selectedmarbles.get(0), newmarble))// the 3rd must be in same diagonal
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * @param dir
     * @param selectedmarbles
     * @return the new locations of the marbles, if the selected marbles can be legally moved to the desired direction. throws exception otherwise
     * @throws Exception
     */
    public ArrayList<ArrayList<Point>> LegalMove(Direction dir,  ArrayList<Point> selectedmarbles, Player player) throws Exception
    {
        error = "";
        int playerMarbles, opponentMarbles;
        Point oppPointsCounter;
        Point playerPointsCounter;
        ArrayList<Point> opponentMovedMarbles = new ArrayList<>();

        // arraylist of arraylists, the first element is the locations of the player's marble after a move
        //and the second element is the location of the opponent's marbles after the player's move
        ArrayList<ArrayList<Point>> newlocations = new ArrayList<>();

        if(selectedmarbles.isEmpty())
        {
            error = "No marble selected";
            throw new Exception("No marble selected");
        }
        ArrayList<Point> newLocationMarbles = NewLocations(selectedmarbles, dir);

        for (int i = 0; i < newLocationMarbles.size(); i++)
        {
            opponentMarbles = 0;
            playerMarbles = 0;
            // if player tries to move out of the board
            if (!IsPointInBoundsOfBoard(newLocationMarbles.get(i)))
            {
                selectedmarbles.clear();
                error = "out of bounds";
                throw new Exception("out of bounds");
            }

            // if player tries to move to a square which is occupied by him
            /*explanation for the second condition: in a diagonal move of above 2 marbles, at least one of the marble's new location
            is a location which is occupied by another selected marble, but that marble will free the spot after the move.
            so we want to pass a situation where the new location is a location that now is occupied, but will get free*/
            if(dataStructure.getSquarePlayer(newLocationMarbles.get(i)) == dataStructure.getSquarePlayer(selectedmarbles.get(0)) && !selectedmarbles.contains(newLocationMarbles.get(i)))
            {
                selectedmarbles.clear();
                error = "self interrupting soldiers";
                throw new Exception("self interrupting soldiers");
            }

            //if one of the new location is occupied by enemy's marble, we need to check if a push can be made
            if(dataStructure.getSquarePlayer(newLocationMarbles.get(i)) == player.getOpponent())
            {
                oppPointsCounter = newLocationMarbles.get(i);
                playerPointsCounter = selectedmarbles.get(i);
                //count how many marbles the opponent has in this direction
                while(IsPointInBoundsOfBoard(oppPointsCounter) && dataStructure.getSquarePlayer(oppPointsCounter) == player.getOpponent())
                {
                    opponentMarbles++;
                    opponentMovedMarbles.add(oppPointsCounter);
                    oppPointsCounter = Direction.AddOffsetToNeighbor(oppPointsCounter, dir.GetMovementOffsetByCurrentLocation(oppPointsCounter, 9));

                }
                //if true, that means that a player's marble is in the way, like sandwich. thus, don't allow the move
                if(IsPointInBoundsOfBoard(oppPointsCounter) && dataStructure.getSquarePlayer(oppPointsCounter) == player)
                {
                    selectedmarbles.clear();
                    error = "self interrupting soldiers";
                    throw new Exception("self interrupting soldiers");
                }

                //count how many marbles the player has in this direction
                while(IsPointInBoundsOfBoard(playerPointsCounter) && dataStructure.getSquarePlayer(playerPointsCounter) == player && selectedmarbles.contains(playerPointsCounter))
                {
                    playerMarbles++;
                    playerPointsCounter = Direction.AddOffsetToNeighbor(playerPointsCounter, dir.GetOppositeDir().GetMovementOffsetByCurrentLocation(playerPointsCounter, 9));
                }

                if(opponentMarbles >= playerMarbles)
                {
                    selectedmarbles.clear();
                    error = "opponent overpowers you";
                    throw new Exception("opponent overpowers you");
                }

            }


        }
        opponentMovedMarbles = NewLocations(opponentMovedMarbles, dir);
        //System.out.println("\nmy locations: " + newLocationMarbles+"\nenemy locations: " + opponentMovedMarbles);
        newlocations.add(newLocationMarbles);
        newlocations.add(opponentMovedMarbles);
        return newlocations;

    }


    ArrayList<ArrayList<Point>> newlocations = new ArrayList<>();//global variable. used for the function "LegalAIMove" to keep it boolean

    /**
     * had to make another "Legality checking" function, for the AI, because the first one throws
     * exceptions if the move isn't good. but when the pc is generating possibilities, exceptions
     * are not necessary, and we will just want to get "False" if the move is illegal
     * @param dir
     * @param selectedmarbles
     * @param player
     * @return true if the move is legal for the board
     */
    public boolean LegalAIMove(Direction dir,  ArrayList<Point> selectedmarbles, Player player, BoardStructure board)
    {
        int playerMarbles, opponentMarbles;
        Point oppPointsCounter;
        Point playerPointsCounter;
        ArrayList<Point> opponentMovedMarbles = new ArrayList<>();

        // arraylist of arraylists, the first element is the locations of the player's marble after a move
        //and the second element is the location of the opponent's marbles after the player's move

        if(selectedmarbles.isEmpty())
        {
            return false;
        }
        ArrayList<Point> newLocationMarbles = NewLocations(selectedmarbles, dir);

        for (int i = 0; i < newLocationMarbles.size(); i++)
        {
            opponentMarbles = 0;
            playerMarbles = 0;
            // if player tries to move out of the board
            if (!IsPointInBoundsOfBoard(newLocationMarbles.get(i)))
            {
                return false;
            }

            // if player tries to move to a square which is occupied by him
            /*explanation for the second condition: in a diagonal move of above 2 marbles, at least one of the marble's new location
            is a location which is occupied by another selected marble, but that marble will free the spot after the move.
            so we want to pass a situation where the new location is a location that now is occupied, but will get free*/
            if(board.getSquarePlayer(newLocationMarbles.get(i)) == board.getSquarePlayer(selectedmarbles.get(0)) && !selectedmarbles.contains(newLocationMarbles.get(i)))
            {
                return false;
            }

            //if one of the new location is occupied by enemy's marble, we need to check if a push can be made
            if(board.getSquarePlayer(newLocationMarbles.get(i)) == player.getOpponent())
            {
                oppPointsCounter = newLocationMarbles.get(i);
                playerPointsCounter = selectedmarbles.get(i);
                //count how many marbles the opponent has in this direction
                while(IsPointInBoundsOfBoard(oppPointsCounter) && board.getSquarePlayer(oppPointsCounter) == player.getOpponent())
                {
                    opponentMarbles++;
                    opponentMovedMarbles.add(oppPointsCounter);
                    oppPointsCounter = Direction.AddOffsetToNeighbor(oppPointsCounter, dir.GetMovementOffsetByCurrentLocation(oppPointsCounter, 9));

                }
                //if true, that means that a player's marble is in the way, like sandwich. thus, don't allow the move
                if(IsPointInBoundsOfBoard(oppPointsCounter) && board.getSquarePlayer(oppPointsCounter) == player)
                {
                    return false;
                }

                //count how many marbles the player has in this direction
                while(IsPointInBoundsOfBoard(playerPointsCounter) && board.getSquarePlayer(playerPointsCounter) == player && selectedmarbles.contains(playerPointsCounter))
                {
                    playerMarbles++;
                    playerPointsCounter = Direction.AddOffsetToNeighbor(playerPointsCounter, dir.GetOppositeDir().GetMovementOffsetByCurrentLocation(playerPointsCounter, 9));
                }

                if(opponentMarbles >= playerMarbles)
                {
                    return false;
                }

            }


        }
        opponentMovedMarbles = NewLocations(opponentMovedMarbles, dir);
        //System.out.println("\nmy locations: " + newLocationMarbles+"\nenemy locations: " + opponentMovedMarbles);
        newlocations.add(newLocationMarbles);
        newlocations.add(opponentMovedMarbles);
        return true;

    }





    /**
     *
     * @param selectedmarbles
     * @return arraylist of points after adding each of them the offset
     */
    public ArrayList<Point> NewLocations(ArrayList<Point> selectedmarbles, Direction offset)
    {

        ArrayList<Point> marblesAfterMove = new ArrayList<>();
        Point newPoint;
        for(int i = 0; i < selectedmarbles.size(); i++)
        {
            newPoint = Direction.AddOffsetToNeighbor(selectedmarbles.get(i), offset.GetMovementOffsetByCurrentLocation(selectedmarbles.get(i), 9));
            marblesAfterMove.add(newPoint);
        }
        //System.out.println(marblesAfterMove);
        return marblesAfterMove;

    }

    public void MakeMove(Direction dir, ArrayList<Point> selectedmarbles, Player player)
    {
        if(gameOver)
            return;
        ArrayList<ArrayList<Point>> newlocations = new ArrayList<>();
        ArrayList<Point> newLocationPlayerMarbles;
        ArrayList<Point> newLocationOpponentMarbles;

        try
        {
            newlocations = LegalMove(dir, selectedmarbles, player);
            newLocationPlayerMarbles = newlocations.get(0);
            newLocationOpponentMarbles = newlocations.get(1);
            //if reached here, the move is legal
            BoardStructure tempBoard = new BoardStructure(dataStructure);
            undoStack.push(tempBoard);
            //push the opponent marbles
            for (Point newLocationOpponentMarble : newLocationOpponentMarbles)
            {
                if(!IsPointInBoundsOfBoard(newLocationOpponentMarble))
                {
                    dataStructure.decNumOfSoldiersOfPlayer(player.getOpponent());
                    assert player.getOpponent() != null;
                    System.out.printf("%s got pushed out of board!\n", player.getOpponent().GetPlayer());
                    System.out.printf("First to get to 6 lose\nwhite: %d\nblack: %d\n", 14 - dataStructure.getNumOfMarbles(Player.WHITE), 14 - dataStructure.getNumOfMarbles(Player.BLACK));
                    if(Won(player, dataStructure))
                    {
                        System.out.println(player.GetPlayer() + " won!");
                        gameOver = true;

                    }
                }
                else
                    dataStructure.setSquarePlayer(newLocationOpponentMarble, player.getOpponent());
            }


            //delete the marbles that have been moved
            for (Point selectedmarble : selectedmarbles) {
                dataStructure.setSquarePlayer(selectedmarble, null);
            }
            // set the marbles at their new locations
            for (Point newLocationMarble : newLocationPlayerMarbles) {
                dataStructure.setSquarePlayer(newLocationMarble, player);
            }



            selectedmarbles.clear();
            computerTurn = true;
        }

        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }



    }

    public void Undo()
    {
        if(!undoStack.isEmpty())
        {
            dataStructure = undoStack.pop();


        }
    }


    /**
     * the function gets a board and updates him after a move was made
     * IMPORTANT: the board is not the real game board:
     * the function is relevant only for choosing the next best board for the AI
     * so this board will get into list of possible boards
     * @param curBoard
     * @param marbles
     */
    public void MakeAIMoveOnTempBoard(BoardStructure curBoard, ArrayList<Point> marbles)
    {

        ArrayList<Point> newLocationPlayerMarbles;
        ArrayList<Point> newLocationOpponentMarbles;
        newLocationPlayerMarbles = newlocations.get(0);
        newLocationOpponentMarbles = newlocations.get(1);

        //set the enemy's marbles
        for (Point newLocationOpponentMarble : newLocationOpponentMarbles)
        {
            if(!IsPointInBoundsOfBoard(newLocationOpponentMarble))
            {
                curBoard.decNumOfSoldiersOfPlayer(Player.WHITE);

            }
            else
                curBoard.setSquarePlayer(newLocationOpponentMarble, Player.WHITE);
        }
        //delete the marbles that have been moved
        for (Point selectedmarble : marbles) {
            curBoard.setSquarePlayer(selectedmarble, null);
        }
        // set the marbles at their new locations
        for (Point newLocationMarble : newLocationPlayerMarbles) {
            curBoard.setSquarePlayer(newLocationMarble, Player.BLACK);
        }

    }


    /**
     * @param curBoard
     * @return array list of all the possible boards after one move from the current one
     */

    public ArrayList<BoardStructure> GetAllPossibleBoards(BoardStructure curBoard)
    {
        ArrayList<BoardStructure> boards = new ArrayList<>();
        ArrayList<Point> marblesLocations = curBoard.GetMarblesLocations(Player.BLACK);
        ArrayList<Point> marbles = new ArrayList<>();

        BoardStructure tempBoard;
        for (int i = 0; i < marblesLocations.size();i++)
        {
            marbles.add(marblesLocations.get(i));
            //all the possible one marble moves
            for (Direction d : Direction.values())
            {
                tempBoard = new BoardStructure(curBoard);
                newlocations.clear();
                if (LegalAIMove(d, marbles, Player.BLACK, tempBoard))
                {
                    MakeAIMoveOnTempBoard(tempBoard, marbles);
                    boards.add(tempBoard);
                }
                //from the selected one marble: search for a friendly neighbor. if found, scan all the direction for a legal move
                marbles = NeighborFromDir(d, marbles.get(0), curBoard);
                if(marbles.size() == 2)
                {
                    //if the size is 1, that means that the second marble was out of bound for this direction
                    for (Direction d2 : Direction.values())
                    {
                        tempBoard = new BoardStructure(curBoard);
                        newlocations.clear();
                        if(LegalAIMove(d2, marbles, Player.BLACK, tempBoard))
                        {
                            MakeAIMoveOnTempBoard(tempBoard, marbles);
                            boards.add(tempBoard);
                        }
                    }
                    //from the two marbles, search for a third friendly marble in the same direction of the 2nd
                    marbles = ThreeNeighbors(d,marbles, curBoard);
                    if(marbles.size() == 3)
                    {
                        for (Direction d3 : Direction.values())
                        {
                            tempBoard = new BoardStructure(curBoard);
                            newlocations.clear();
                            if(LegalAIMove(d3, marbles, Player.BLACK, tempBoard))
                            {
                                MakeAIMoveOnTempBoard(tempBoard, marbles);
                                boards.add(tempBoard);
                            }
                        }

                    }

                }
            }
            marbles.clear();
        }
        return boards;
    }

    /**
     * sets the children of a node
     * A child means a possible board from the current one
     * @param node
     * @param level
     */
    private void SetChildren(Node node, int level)
    {
        ArrayList<BoardStructure> possibleBoards = GetAllPossibleBoards(node.GetBoard());
        for (BoardStructure board : possibleBoards) {
            Node child = new Node(board);
            node.AddChild(child);
        }
        level--;
        if (level > 0) {
            for (Node child : node.GetChildren()) {
                SetChildren(child, level);
            }
        }
    }

    private Node SetupTree()
    {
        Node root = new Node(dataStructure);
        SetChildren(root, level);
        return root;
    }

    private double MinimaxAlphaBeta(Node node, double alpha, double beta)
    {
        Player player = computerTurn ? Player.BLACK:Player.WHITE;
        if(node.HasChildren())
        {
            double minMaxOfChildren = MinimaxAlphaBeta(node.GetChildren().get(0), alpha, beta);
            if(player == Player.BLACK)
            {
                for(Node child : node.GetChildren())
                {
                    double currentValue = MinimaxAlphaBeta(child, alpha, beta);
                    if(currentValue < minMaxOfChildren)
                        minMaxOfChildren = currentValue;
                    if(currentValue > alpha)
                        alpha = currentValue;

                    if(alpha >= beta)
                    {
                        node.GetChildren().remove(child);
                        break;
                    }
                }
            }
            else
            {
                for (Node child : node.GetChildren())
                {
                    double currentValue = MinimaxAlphaBeta(child, alpha, beta);
                    if (currentValue > minMaxOfChildren)
                        minMaxOfChildren = currentValue;
                    if(currentValue < beta)
                        beta = currentValue;
                    if(alpha >= beta)
                    {
                        node.GetChildren().remove(child);
                        break;
                    }


                }
            }
            return Evaluate(node.GetBoard()) + minMaxOfChildren;
        }
        else
            return Evaluate(node.GetBoard());
    }

    /**
     * @param root
     * @return the board (node) with the highest evaluation score among the children of the root
     */
    private Node GetBestBoard(Node root)
    {
        Node bestNode = root.GetChildren().get(0);
        double bestEvaluation = MinimaxAlphaBeta(bestNode, Double.MIN_VALUE, Double.MAX_VALUE);
        //choose the best node from the children
        for (Node node : root.GetChildren()) {
            double newValue = MinimaxAlphaBeta(node, Double.MIN_VALUE, Double.MAX_VALUE);
            if (newValue > bestEvaluation) {
                bestNode = node;
                bestEvaluation = newValue;
            }
        }
        return bestNode;
    }

    /**
     * the function is responsible for applying the best next board on the real one
     */
    public void MakeAIMove()
    {
        BoardStructure keepOld = new BoardStructure(dataStructure);
        dataStructure = GetBestBoard(SetupTree()).GetBoard();
        if(keepOld.numOfWhites != dataStructure.numOfWhites)
        {
            System.out.printf("%s got pushed out of board!\n", Player.WHITE.GetPlayer());
            System.out.printf("First to get to 6 lose\nwhite: %d\nblack: %d\n", 14 - dataStructure.getNumOfMarbles(Player.WHITE), 14 - dataStructure.getNumOfMarbles(Player.BLACK));
            if(Won(Player.BLACK, dataStructure))
            {
                System.out.println(Player.BLACK.GetPlayer() + " won!");
                gameOver = true;

            }
        }
    }

    /**
     * @param board
     * @return Integer representing the value of the board
     * bigger value means better board for the AI
     */
    public int Evaluate(BoardStructure board)
    {
        double distanceValue = MarblesDistancesToEdge(board);
        double winValue = 0;
        double mablesAmount = board.numOfBlacks - 1.5 * board.numOfWhites;
        if(Won(Player.BLACK, board))

            winValue = 99999;
        else
            if(Won(Player.WHITE, board))
                winValue = -99999;
        return (int)(distanceValue + winValue + mablesAmount);
    }

    public double MarblesDistancesToEdge(BoardStructure board)
    {
        int[] playerMarblesDistance = new int[numOfRows / 2 + 1];
        int[] computerMarblesDistance = new int[numOfRows / 2 + 1];

        ArrayList<Point> playerMarbles = board.GetMarblesLocations(Player.WHITE);
        ArrayList<Point> computerMarbles = board.GetMarblesLocations(Player.BLACK);
        for(Point marble : playerMarbles)
        {
            int index = CalculateDistToEdge(marble);
            playerMarblesDistance[index]++;
        }

        for(Point marble : computerMarbles)
        {
            int index = CalculateDistToEdge(marble);
            computerMarblesDistance[index]++;
        }
        double playerResult = 0;
        double computerResult = 0;

        for (int i = 0; i < playerMarblesDistance.length; i++)
        {
            playerResult += playerMarblesDistance[i] * i;
            computerResult += computerMarblesDistance[i] * i;
        }
        return computerResult - playerResult;




    }

    public int CalculateDistToEdge(Point pos)
    {
        int leftDist = pos.x;
        int upDist = pos.y;
        int rightDist = numOfColsInRow(pos.y) - pos.x;
        int downDist = numOfRows - pos.y;
        return(Math.min(Math.min(leftDist, rightDist), Math.min(upDist,downDist)));

    }

    /**
     * @param dir
     * @param point
     * @return array list contains the original point, and a neighbor point in the given direction
     */
    public ArrayList<Point> NeighborFromDir(Direction dir, Point point, BoardStructure board)
    {
        ArrayList<Point> twoPoints = new ArrayList<>();
        twoPoints.add(point);
        Point secondPoint = Direction.AddOffsetToNeighbor(point, dir.GetMovementOffsetByCurrentLocation(point, 9));
        if(IsPointInBoundsOfBoard(secondPoint) && board.getSquarePlayer(secondPoint) == Player.BLACK)
            twoPoints.add(secondPoint);
        return twoPoints;
    }

    /**
     * @param dir
     * @param points
     * @param board
     * @return array list with original two points, and third point which is a neighbor of the others
     * in the same direction. if out of bounds, or there is no marble there, don't add anything.
     */
    public ArrayList<Point> ThreeNeighbors(Direction dir, ArrayList<Point>  points, BoardStructure board)
    {

        Point thirdPoint = Direction.AddOffsetToNeighbor(points.get(1), dir.GetMovementOffsetByCurrentLocation(points.get(1), 9));
        if(IsPointInBoundsOfBoard(thirdPoint) && board.getSquarePlayer(thirdPoint) == Player.BLACK)
            points.add(thirdPoint);
        return points;
    }


    /**
     * @param player
     * @return true if the opponent lost 6 marbles remaining, in other words he lost.
     *
     */
    public boolean Won(Player player, BoardStructure board)
    {
        return  (14 - board.getNumOfMarbles(player.getOpponent())) >= 6;

    }

    /**
     * @param p1
     * @param p2
     * @return true if the given points are in the same row
     */
    public boolean IsInSameRow(Point p1, Point p2)
    {
        return p1.y == p2.y;
    }

    /**
     * @param x1
     * @param x2
     * @param x3
     * @return true if the three numbers are adjacent to each other
     */
    public static boolean Consecutive(int x1, int x2, int x3)
    {
        int min = Math.min(x1, Math.min(x2, x3));
        int max = Math.max(x1, Math.max(x2, x3));
        return max - min == 2 && x1 != x2 && x1 != x3 && x2 != x3;
    }


    /**
     * @param rowNum
     * @return how many columns there are in the given row
     */
    private int numOfColsInRow(int rowNum)
    {
        if (rowNum < numOfRows / 2) {
            return rowNum + 5;
        }
        return -(rowNum - (numOfRows - 1)) + 5;
    }

    public boolean IsPointInBoundsOfBoard(Point pos) {
        return pos.y > -1 && pos.y < numOfRows && pos.x > -1
                && pos.x < numOfColsInRow(pos.y);
    }

    /**
     * @param source
     * @return arraylist of all the neighbors of a given point
     */
    public ArrayList<Point> GetNeighbors(Point source) {
        Point neighbor;
        ArrayList<Point> neighbors = new ArrayList<>();
        Direction.setDirectionsOffsetsByCurrPos(source, numOfRows);
        for (Direction d : Direction.values())
        {
            neighbor = Direction.AddOffsetToNeighbor(source, d.GetMovementOffsetByCurrentLocation(source, 9));
            // if the coordinate is in the bounds of the board
            if (IsPointInBoundsOfBoard(neighbor)) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }



    public void initBoard() {
        dataStructure.initBoard();
    }
    public Player[][] boardDescription()
    {
        return dataStructure.boardDescription();
    }

}
