package TankGame.src.game;

import java.awt.*;

public class TankAttributes {
    private int health1;
    private int health2;
    private int health3;
    private int extraHealth;
    private int maxHealthBars;
    private int currentHealthBars;
    public TankAttributes(int initialHealth1, int initialHealth2, int initialHealth3, int extraHealth) {
        this.health1 = initialHealth1;
        this.health2 = initialHealth2;
        this.health3 = initialHealth3;
        this.extraHealth = extraHealth;
        this.maxHealthBars = 4;
        this.currentHealthBars = 3;
    }

    public TankAttributes() {
        this(100, 100, 100, 0);
    }


    public int getHealth1() {
        return this.health1;
    }

    public void setHealth1(int health1) {
        this.health1 = health1;
    }

    public int getHealth2() {
        return this.health2;
    }

    public void setHealth2(int health2) {
        this.health2 = health2;
    }

    public int getHealth3() {
        return this.health3;
    }

    public void setHealth3(int health3) {
        this.health3 = health3;
    }
    public int getExtraHealth() {
        return this.extraHealth;
    }
    public void setExtraHealth() {
         this.extraHealth = extraHealth;
    }

    public void increaseHealthBar() {
        if (currentHealthBars < maxHealthBars) {
            currentHealthBars++;
            if (currentHealthBars == 4) {
                extraHealth = 100; // Initialize extra health bar with 100 health
            }
        }
    }


    public void decreaseHealth1(int amount) {
        this.health1 -= amount;
        if (this.health1 < 0) {
            this.health1 = 0;
        }
    }

    public void decreaseHealth2(int amount) {
        this.health2 -= amount;
        if (this.health2 < 0) {
            this.health2 = 0;
        }
    }

    public void decreaseHealth3(int amount) {
        this.health3 -= amount;
        if (this.health3 < 0) {
            this.health3 = 0;
        }
    }

    public void decreaseExtraHealth(int amount) {
        this.extraHealth -= amount;
        if (this.extraHealth < 0) {
            this.extraHealth = 0;
        }
    }


    public void drawHealthBars(Graphics2D g, float x, float y) {
        int barWidth = 50;
        int barHeight = 5;
        int space = 5;

        // Draw health bar 1
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y - (barHeight + space) * 2, barWidth, barHeight);
        int health1Percentage = (int) ((health1 / 100.0) * barWidth);
        g.setColor(Color.GREEN);
        g.fillRect((int) x, (int) y - (barHeight + space) * 2, health1Percentage, barHeight);

        // Draw health bar 2
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y - (barHeight + space), barWidth, barHeight);
        int health2Percentage = (int) ((health2 / 100.0) * barWidth);
        g.setColor(Color.GREEN);
        g.fillRect((int) x, (int) y - (barHeight + space), health2Percentage, barHeight);

        // Draw health bar 3
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, barWidth, barHeight);
        int health3Percentage = (int) ((health3 / 100.0) * barWidth);
        g.setColor(Color.GREEN);
        g.fillRect((int) x, (int) y, health3Percentage, barHeight);

        if (currentHealthBars > 3) {
            g.setColor(Color.RED);
            g.fillRect((int) x, (int) y + (barHeight + space), barWidth, barHeight);
            int extraHealthPercentage = (int) ((extraHealth / 100.0) * barWidth);
            g.setColor(Color.GREEN);
            g.fillRect((int) x, (int) y + (barHeight + space), extraHealthPercentage, barHeight);
        }
    }
}

