package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MyImage
{
    private Image image;
    private int x,y, width, height;

    public MyImage(String path, int x, int y, int width, int height) {
        image = new ImageIcon(Objects.requireNonNull(this.getClass()
                .getResource(path))).getImage();
        setCords(x, y);
        setSize(width, height);
    }


    public void setCords(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void setImage(Image image)
    {
        this.image = image;
    }

    public void drawImage(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.image, x, y, width, height, null);

    }

}
