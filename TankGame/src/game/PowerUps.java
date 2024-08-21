package TankGame.src.game;

import java.awt.*;

public interface PowerUps {
    void drawImage(Graphics2D g);
    Rectangle getHitBox();
    void applyEffect(Tank tank);
    boolean isActive();
}
