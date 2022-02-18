package Backend;

import enums.Player;

public class MarbleManage
{
    private Player player;

    public  MarbleManage(Player player)
    {
        this.player = player;
    }

    public Player getPlayer()
    {
        return this.player;
    }
}
