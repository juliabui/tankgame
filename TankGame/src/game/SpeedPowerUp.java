package TankGame.src.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpeedPowerUp implements PowerUps {

    private int x;
    private int y;
    private BufferedImage img;
    private boolean active;

    public SpeedPowerUp(int x, int y, BufferedImage img) {
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
        tank.applySpeedBoost(2.0f, 5000);
    }

    @Override
    public boolean isActive() {
        return active;
    }

}
