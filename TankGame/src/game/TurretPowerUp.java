package TankGame.src.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TurretPowerUp implements PowerUps{
    private int x;
    private int y;
    private BufferedImage img;
    public TurretPowerUp(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }
    @Override
    public void drawImage(Graphics2D g) {
        g.drawImage(img, x, y, null);
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.applyTurretPowerUp(2);
    }

    @Override
    public boolean isActive() {
        return false;
    }

}
