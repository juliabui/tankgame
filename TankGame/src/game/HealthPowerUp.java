package TankGame.src.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HealthPowerUp implements PowerUps {
    private float x, y;
    private BufferedImage img;
    private boolean active;

    public HealthPowerUp(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.active = true;
    }

    @Override
    public void drawImage(Graphics2D g) {
        if (active) {
            g.drawImage(img, (int)x, (int)y, null);
        }
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle((int)x, (int)y, img.getWidth(), img.getHeight());
    }


    public void drawImage(Graphics g) {
        if (active) {
            g.drawImage(img, (int)x, (int)y, null);
        }
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.increaseHealth(1);
        this.active = true;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
