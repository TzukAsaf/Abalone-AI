package AI;

import Backend.BoardStructure;

import java.util.ArrayList;

public class Node
{
    private BoardStructure board;
    private ArrayList<Node> children;

    public Node(BoardStructure board)
    {
        this.board = board;
        this.children = new ArrayList<>();
    }

    public void AddChild(Node child)
    {
        this.children.add(child);
    }

    public BoardStructure GetBoard()
    {
        return board;
    }

    public ArrayList<Node> GetChildren()
    {
        return children;
    }

    public boolean HasChildren()
    {
        return !this.children.isEmpty();
    }
}
