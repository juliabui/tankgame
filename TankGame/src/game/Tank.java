package TankGame.src.game;

import TankGame.src.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author anthony-pc
 */
public class Tank extends TankAttributes {
    private float screen_x, screen_y, x, y, vx, vy, angle;
    private float R = 1;
    private float ROTATIONSPEED = 3.0f;

    private BufferedImage img;
    private boolean UpPressed, DownPressed, RightPressed, LeftPressed, ShootPressed;

    private BufferedImage bulletImg;
    private boolean showExplosion;
    private int explosionX, explosionY;
    private ImageIcon smallExplosion = new ImageIcon();
    private float previousX, previousY;
    private final CopyOnWriteArrayList<PowerUpEffect> activeEffects;
    private static final long bulletCooldown = 500;
    private long lastBullet = 0;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img, BufferedImage bulletImg) {
        super();
        this.screen_x = x;
        this.screen_y = y;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.bulletImg = bulletImg;
        this.img = img;
        this.angle = angle;
        this.activeEffects = new CopyOnWriteArrayList<>();

    }
    public void drawHealthBar(Graphics g) {
        super.drawHealthBars((Graphics2D) g, x, y);
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }
    void toggleShootPressed() {
        this.ShootPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }
    public void unToggleShootPressed() {
        this.ShootPressed = false;
    }

    public Rectangle getHitBox() {
        return new Rectangle((int)x, (int)y, img.getWidth(), img.getHeight());
    }

    public int getScreen_x(){
        return (int)screen_x;
    }

    public int getScreen_y(){
        return (int)screen_y;
    }

    public void setPreviousPosition() {
        this.previousX = this.x;
        this.previousY = this.y;
    }

    public float getPreviousX() {
        return previousX;
    }

    public float getPreviousY() {
        return previousY;
    }

    private void loadExplosionImage() {
        smallExplosion = new ImageIcon("out/production/csc413-tankgame-juliabui/images/explosion_sm/explosion_sm_0002.png");
        if (smallExplosion.getIconWidth() == -1) {
            System.out.println("Small explosion image not found.");
        }
    }

    private void handleCollision(Bullet bullet, CopyOnWriteArrayList<Wall> walls, CopyOnWriteArrayList<PowerUps> powerUps){
        Rectangle tankBounds = getHitBox();
        ArrayList<Wall> wallsToRemove = new ArrayList<>();

        for (Wall wall : walls) {
            if (tankBounds.intersects(wall.getHitBox())) {
                if (wall.isBreakable()) {
                    wallsToRemove.add(wall); // Add to wall needed to be removed
                    System.out.println("Wall broken");

                    loadExplosionImage();
                    explosionX = (int) x;
                    explosionY = (int) y;

                    Timer explosionTimer = new Timer();
                    explosionTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            showExplosion = false;
                        }
                    }, 1000);

                } else {
                    x -= vx;
                    y -= vy;
                    System.out.println("Unable to break");
                    break; // Stop checking further walls as tank can't move
                }
            }
        }

        // Remove the breakable walls after the loop
        walls.removeAll(wallsToRemove);

           checkBorder();


        for (PowerUpEffect effect : activeEffects) {
            effect.apply(this);
        }
        // Collision detection and power-up collection
        for (PowerUps powerUp : powerUps) {
            if (getHitBox().intersects(powerUp.getHitBox()) && powerUp.isActive()) {
                powerUp.applyEffect(this);
            }
        }
    }
    void update(CopyOnWriteArrayList<Wall> walls, CopyOnWriteArrayList<PowerUps> powerUps) {

        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        if(this.ShootPressed){
            this.shoot();
        }

        handleCollision(null, walls, powerUps);

        // Update screen position
        centerScreen();
        checkBorder();
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void centerScreen(){
        this.screen_x = this.x - GameConstants.GAME_SCREEN_WIDTH / 4f;
        this.screen_y = this.y - GameConstants.GAME_SCREEN_HEIGHT / 2f;

        if (this.screen_x < 0) screen_x = 0;
        if (this.screen_y < 0) screen_y = 0;

        if (screen_x > GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 2f) {
            this.screen_x = GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 2f;
        }

        if (screen_y > GameConstants.GAME_WORLD_LENGTH - GameConstants.GAME_SCREEN_HEIGHT) {
            this.screen_y = GameConstants.GAME_WORLD_LENGTH - GameConstants.GAME_SCREEN_HEIGHT;

        }
    }
    void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 88) {
            x = GameConstants.GAME_WORLD_WIDTH - 88;
        }
        if (y < 30) {
            y = 30;
        }
        if (y >= GameConstants.GAME_WORLD_LENGTH - 88) {
            y = GameConstants.GAME_WORLD_LENGTH - 88;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    private Timer speedBoostTimer;
    public void applySpeedBoost(float speedMultiplier, int duration) {
        this.R *= speedMultiplier;

        if (speedBoostTimer != null) {
            speedBoostTimer.cancel();
        }
        speedBoostTimer = new Timer();
        speedBoostTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                R /= speedMultiplier;
            }
        }, duration);
    }

    public void increaseHealth(int effect) {
        increaseHealthBar();
    }

    private int turretPowerUpShotsRemaining = 0;
    private boolean isTurretPowerUpActive = false;

    public void applyTurretPowerUp(int newDamage) {
        this.turretPowerUpShotsRemaining = 3;
        this.isTurretPowerUpActive = true;
    }


    public void removeEffect(PowerUpEffect effect) {
        activeEffects.remove(effect);
    }


    public void shoot() {
        float offset = 25;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBullet >= bulletCooldown) {
            lastBullet = currentTime;
            float bulletX = this.x + (float) this.img.getWidth() / 2 - offset;
            float bulletY = this.y + (float) this.img.getHeight() / 2 - offset;
            Bullet bullet = new Bullet(bulletX, bulletY, this.angle, bulletImg, this);

            if (isTurretPowerUpActive && turretPowerUpShotsRemaining > 0) {
                bullet.setDamage(bullet.getDamage() * 2);  // Double the damage
                turretPowerUpShotsRemaining--;

                // Deactivate the power-up if all shots are used
                if (turretPowerUpShotsRemaining == 0) {
                    isTurretPowerUpActive = false;
                }
            }

            GameWorld.getInstance().bullets.add(bullet);

            System.out.println("Tank position: (" + x + ", " + y + ")");
            System.out.println("Bullet start position: (" + bulletX + ", " + bulletY + ")");

            GameWorld.getAudioManager().playSoundEffect("bullet");
        }
    }



    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        g2d.drawRect((int) x, (int) y, this.img.getWidth(), this.img.getHeight());

        if (showExplosion) {
            smallExplosion.paintIcon(null, g, explosionX, explosionY);
        }
    }
}

