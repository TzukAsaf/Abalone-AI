import enums.Player;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JPanel;


public class Marble extends JPanel
{
    private MyImage image;
    private Point pos;
    private LinkedList<MarbleListener> listeners;
    private Player player;
    private int size;

    public Marble(Point pos, int size, Player p)
    {
        setOpaque(false);
        this.listeners = new LinkedList<MarbleListener>();
        this.player = p;
        this.size = size;
        this.pos = pos;
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                for (MarbleListener i : listeners) {
                    i.MarbleSelected(pos);
                }
            }
        });

    }

    public void addListener(MarbleListener l)
    {
        listeners.add(l);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image!=null)
        {
            image.drawImage(g);
        }
    }

    public void drawMarble()
    {
        if(player==null)
        {
            image=null;
        }
        if(player==Player.WHITE)
        {
            image=new MyImage("Assets/white-ball.png", 0, 0, size, size);
        }
        if(player==Player.BLACK)
        {
            image=new MyImage("Assets/black-ball.png", 0, 0, size, size);
        }
        repaint();
    }

    public void markMarble()
    {
        if(player == Player.WHITE)
        {
            image = new MyImage("Assets/mark.png", 0, 0, size, size);
            repaint();
        }

    }
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player _player) {
        this.player = _player;
    }
}
