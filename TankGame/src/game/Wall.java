package TankGame.src.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall {
    private int x;
    private int y;
    private BufferedImage wallImg;

    public Wall(int x, int y, BufferedImage wallImg) {
        this.x = x;
        this.y = y;
        this.wallImg = wallImg;
    }



    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    public Rectangle getHitBox() {
        return new Rectangle(x, y, wallImg.getWidth(), wallImg.getHeight());
    }

    public void drawImage(Graphics2D g){
        g.drawImage(wallImg, x, y, null);
    }

    public interface Breakable{
        boolean isBreakable();
    }

    public boolean isBreakable(){
        return false;
    }

}
