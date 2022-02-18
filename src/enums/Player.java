package enums;

public enum Player
{
    WHITE,BLACK;

    public Player getOpponent()
    {
        Player p=this;
        if(p==WHITE)
            return BLACK;
        if(p==BLACK)
            return WHITE;
        return null;
    }


}
