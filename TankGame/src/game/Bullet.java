package TankGame.src.game;

import TankGame.src.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet {

    private float x;
    private float y;
    private float vx;
    private float vy;
    private BufferedImage img;
    private final float speed;
    private final float angle;
    private Tank owner;
    private static int damage;

    public Bullet(float x, float y, float angle, BufferedImage img, Tank owner) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.img = img;
        this.speed = (float) Math.sqrt(vx * vx + vy * vy);
        this.owner = owner;
        this.damage = 1;
    }

    public Tank getOwner() {
        return owner;
    }
    public void update() {

        float speed = 3; // Bullet speed
        this.x += (float) (Math.cos(Math.toRadians(this.angle)) * speed);
        this.y += (float) (Math.sin(Math.toRadians(this.angle)) * speed);
    }

    public Rectangle getHitBox() {
        return new Rectangle((int)x, (int)y, img.getWidth(), img.getHeight());
    }



    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }

    // Getter and Setter methods
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public float getSpeed() {
        return speed;
    }

    public float getAngle() {
        return angle;
    }
    public static int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}

