package TankGame.src.game;

public class PowerUpEffect {
    private String type;
    private long duration; // Duration in milliseconds
    private long startTime;

    public void apply(Tank tank) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime < duration) {
            switch (type) {
                case "HEALTH":
                    tank.increaseHealth(100);
                    break;
                case "SPEED":
                    tank.applySpeedBoost(2.0f, (int)duration);
                    break;
                case "TURRET":
                    tank.applyTurretPowerUp(2);
                    break;
            }
        } else {
            tank.removeEffect(this);
        }
    }
}
