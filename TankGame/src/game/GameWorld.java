package TankGame.src.game;


import TankGame.src.GameConstants;
import TankGame.src.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {
    private BufferedImage t1img;
    private BufferedImage t2img;

    private BufferedImage world;
    private BufferedImage backgroundImage;
    private BufferedImage wallImg1;
    private BufferedImage wallImg2;
    private BufferedImage healthPowerUpImg;
    private BufferedImage speedPowerUpImg;
    private BufferedImage turretPowerUpImg;
    private BufferedImage bulletImg;
    private TankGame.src.game.Tank t1;
    private TankGame.src.game.Tank t2;
    private final Launcher lf;
    private long tick = 0;
    private CopyOnWriteArrayList<Wall> walls;
    private CopyOnWriteArrayList<PowerUps> powerUps;
    CopyOnWriteArrayList<Bullet> bullets;

    private static GameWorld instance;

    private String winner = " ";

    private static AudioManager audioManager;

    public static GameWorld getInstance() {
        if (instance == null) {
            instance = new GameWorld(new Launcher());
        }
        return instance;
    }

    public GameWorld(Launcher lf) {
        this.lf = lf;
        this.bullets = new CopyOnWriteArrayList<>();
        instance = this;

        audioManager = new AudioManager();
        audioManager.loadBackgroundMusic("out/production/csc413-tankgame-juliabui/images/Music.mid");
        audioManager.playBackgroundMusic();

        audioManager.loadSoundEffect("bullet", "TankGame/resources/cartoon-shooting.wav");

        audioManager.loadSoundEffect("powerUp", "TankGame/resources/converted_power_up_noise.wav");
    }

    public static AudioManager getAudioManager() {
        return audioManager;
    }

    public void tankCollisions() {
        Tank t1 = this.t1;
        Tank t2 = this.t2;

        Rectangle t1HitBox = t1.getHitBox();
        Rectangle t2HitBox = t2.getHitBox();

        if (t1HitBox.intersects(t2HitBox)) {
            t1.setX(t1.getPreviousX());
            t1.setY(t1.getPreviousY());
        }

        if (t2HitBox.intersects(t1HitBox)) {
            t2.setX(t2.getPreviousX());
            t2.setY(t2.getPreviousY());

        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    audioManager.stopBackgroundMusic();
                    audioManager.close();
                }));
                this.tick++;
                t1.setPreviousPosition();
                t2.setPreviousPosition();

                this.t1.update(walls, powerUps); // update tank
                this.t2.update(walls, powerUps);

                tankCollisions();

                //  Update and remove bullets
                CopyOnWriteArrayList<Bullet> bulletsToRemove = new CopyOnWriteArrayList<>();
                for (Bullet bullet : bullets) {
                    bullet.update();
                    if (bullet.getX() < 0 || bullet.getX() > GameConstants.GAME_SCREEN_WIDTH ||
                            bullet.getY() < 0 || bullet.getY() > GameConstants.GAME_SCREEN_HEIGHT) {
                        bulletsToRemove.add(bullet);
                    } else {
                        for (Wall wall : walls) {
                            if (bullet.getHitBox().intersects(wall.getHitBox())) {

                                // Handle bullet-wall collision
                                if (wall instanceof BreakableWall) {
                                    walls.remove(wall);
                                }
                                bulletsToRemove.add(bullet);
                                break;

                            }
                        }
                        if (bullet.getHitBox().intersects(t1.getHitBox()) && bullet.getOwner() != t1) {
                            if (t1.getHealth1() > 0) {
                                t1.decreaseHealth1(Bullet.getDamage());
                                if (t1.getHealth1() == 0) {
//                                    this.t1.setX(0);
//                                    this.t1.setY(GameConstants.GAME_WORLD_LENGTH - 15);
                                }
                                System.out.println("t2 bar 1 decrease health // damage: " + Bullet.getDamage());
                                break;
                            } else if (t1.getHealth2() > 0) {
                                t1.decreaseHealth2(Bullet.getDamage());
                                if (t1.getHealth2() == 0) {
//                                    this.t2.setX(GameConstants.GAME_WORLD_WIDTH - 15);
//                                    this.t2.setY(GameConstants.GAME_WORLD_LENGTH - 15);
                                }
                                System.out.println("t1 bar 2 decrease health // damage: " + Bullet.getDamage());
                                break;
                            } else if (t1.getHealth3() > 0) {
                                t1.decreaseHealth3(Bullet.getDamage());
                                if (t1.getHealth3() == 0 && t1.getExtraHealth() == 0) {
//                                    this.t2.setX(GameConstants.GAME_WORLD_WIDTH - 15);
//                                   this.t2.setY(GameConstants.GAME_WORLD_LENGTH - 15);
                                    winner = "Player 2 wins!";
                                }
                                System.out.println("t2 bar 3 decrease health// damage:" + Bullet.getDamage());
                                break;
                            } else if (t1.getExtraHealth() > 0) {
                                t1.decreaseExtraHealth(Bullet.getDamage());
                                if (t2.getExtraHealth() == 0) {
                                    winner = "Player 2 wins!";
                                }
                                System.out.println("t2 extra bar decrease health// damage:" + Bullet.getDamage());
                                break;
                            }
                        } else if (bullet.getHitBox().intersects(t2.getHitBox()) && bullet.getOwner() != t2) {
                            if (t2.getHealth1() > 0) {
                                t2.decreaseHealth1(Bullet.getDamage());
                                if (t2.getHealth1() == 0) {
//                                    this.t2.setX(GameConstants.GAME_WORLD_WIDTH - 15);
//                                    this.t2.setY(GameConstants.GAME_WORLD_LENGTH - 15);
                                }
                                System.out.println("t2 bar 1 decrease health // damage: " + Bullet.getDamage());
                                break;
                            } else if (t2.getHealth2() > 0) {
                                t2.decreaseHealth2(Bullet.getDamage());
                                if (t2.getHealth2() == 0) {
//                                   this.t2.setX(GameConstants.GAME_WORLD_WIDTH - 15);
//                                    this.t2.setY(GameConstants.GAME_WORLD_LENGTH - 15);
                                }
                                System.out.println("t2 bar 2 decrease health // damage: " + Bullet.getDamage());
                                break;
                            } else if (t2.getHealth3() > 0) {
                                t2.decreaseHealth3(Bullet.getDamage());
                                if (t2.getHealth3() == 0 && t2.getExtraHealth() == 0) {
//                                    this.t1.setX(0);
//                                    this.t1.setY(GameConstants.GAME_WORLD_LENGTH - 15);
                                    winner = "Player 1 wins!";
                                }
                                System.out.println("t2 bar 3 decrease health// damage:" + Bullet.getDamage());
                                break;
                            } else if (t2.getExtraHealth() > 0) {
                                t2.decreaseExtraHealth(Bullet.getDamage());
                                if (t1.getExtraHealth() == 0) {
                                    winner = "Player 1 wins!";
                                }
                                System.out.println("t2 extra bar decrease health// damage:" + Bullet.getDamage());
                                break;
                            }
                        }
                    }
                }
                bullets.removeAll(bulletsToRemove);


                for (PowerUps powerUp : powerUps) {
                    if (t1.getHitBox().intersects(powerUp.getHitBox())) {
                        GameWorld.getAudioManager().playSoundEffect("powerUp");
                        powerUp.applyEffect(t1);
                        powerUps.remove(powerUp);
                        break;
                    } else if (t2.getHitBox().intersects(powerUp.getHitBox())) {
                        GameWorld.getAudioManager().playSoundEffect("powerUp");
                        powerUp.applyEffect(t2);
                        powerUps.remove(powerUp);
                        break;
                    }
                }

                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our
                 * loop run at a fixed rate per/sec.
                 */

                if (isGameOver() || this.tick > 30000) {
                    this.lf.setFrame("end");
                    return;
                }

                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    public String getWinner() {
        return winner;
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_LENGTH,
                BufferedImage.TYPE_INT_RGB);

        BufferedImage t1img = null;
        BufferedImage t2img = null;


        try {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory. When running a jar, class loaders will read from within the jar.
             */
            t1img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("images/tank1.png"),
                            "Could not find tank1.png")
            );

            t2img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("images/tank2.png"),
                            "Could not find tank2.png")
            );

            backgroundImage = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("images/floor.jpg"),
                            "Could not find floor.jpg")
            );

            wallImg1 = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("images/wall1.png"),
                            "Could not find wall1.png")
            );

            wallImg2 = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("images/wall2.png"),
                            "Could not find wall2.png")
            );

            healthPowerUpImg = resizeImage(
                    ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("images/healthpowerup.png"),
                            "Could not find healthpowerup.png")),
                    50, 50
            );

            speedPowerUpImg = resizeImage(
                    ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("images/speedpowerup.png"),
                            "Could not find speedpowerup.png")),
                    50, 50
            );
            turretPowerUpImg = resizeImage(
                    ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("images/turretpowerup.png"),
                            "Could not find turretpowerup.png")),
                    50, 50
            );
            bulletImg = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("images/bullet.png"),
                            "Could not find bullet.png")
            );
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        t1 = new Tank((float) world.getWidth() / 2 - 500, (float) world.getHeight() / 2, 0, 0, (short) 0, t1img, bulletImg);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank((float) world.getWidth() / 2 + 500, (float) world.getHeight() / 2, 0, 0, (short) 180, t2img, bulletImg);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);

        walls = new CopyOnWriteArrayList<>();

        int xPosition;
        int yPosition;
        int wallWidth = wallImg1.getWidth();
        int wallHeight = wallImg1.getHeight();
        int centerX = world.getWidth() / 2;
        int centerY = world.getHeight() / 2;

        // Left-side

        for (int row = 0; row < 8; row++) {
            xPosition = 0;
            yPosition = centerY;
            yPosition = yPosition - 4 * wallHeight;
            for (int col = 0; col < 1; col++) {
                int x = xPosition + col * wallWidth;
                int y = yPosition + row * wallHeight;
                walls.add(new UnbreakableWall(x, y, wallImg1, false));
            }
        }

        for (int row = 0; row < 1; row++) {
            xPosition = 0;
            yPosition = centerY;
            yPosition = yPosition - (4 * wallHeight);
            for (int col = 0; col < 8; col++) {
                int x = xPosition + col * wallWidth;
                int y = yPosition + row * wallHeight;
                walls.add(new UnbreakableWall(x, y, wallImg1, false));
            }
        }

        for (int row = 0; row < 1; row++) {
            xPosition = 0;
            yPosition = centerY;
            yPosition = yPosition + (4 * wallHeight);
            for (int col = 0; col < 8; col++) {
                int x = xPosition + col * wallWidth;
                int y = yPosition + row * wallHeight;
                walls.add(new UnbreakableWall(x, y, wallImg1, false));
            }
        }

        // Right-side
        for (int row = 0; row < 8; row++) {
            xPosition = world.getWidth() - wallWidth;
            yPosition = centerY;
            yPosition = yPosition - 4 * wallHeight;
            for (int col = 0; col < 1; col++) {
                int x = xPosition + col * wallWidth;
                int y = yPosition + row * wallHeight;
                walls.add(new UnbreakableWall(x, y, wallImg1, false));
            }
        }

        for (int row = 0; row < 1; row++) {
            xPosition = world.getWidth() - 8 * wallWidth;
            yPosition = centerY;
            yPosition = yPosition - (4 * wallHeight);
            for (int col = 0; col < 8; col++) {
                int x = xPosition + col * wallWidth;
                int y = yPosition + row * wallHeight;
                walls.add(new UnbreakableWall(x, y, wallImg1, false));
            }
        }

        for (int row = 0; row < 1; row++) {
            xPosition = world.getWidth() - 8 * wallWidth;
            yPosition = centerY;
            yPosition = yPosition + (4 * wallHeight);
            for (int col = 0; col < 8; col++) {
                int x = xPosition + col * wallWidth;
                int y = yPosition + row * wallHeight;
                walls.add(new UnbreakableWall(x, y, wallImg1, false));
            }
        }

        // Box
        for (int i = 0; i < 5; i++) {
            int x = centerX - (5 / 2 * wallWidth) + i * wallWidth;
            int y = centerY - (5 / 2 * wallHeight);
            walls.add(new BreakableWall(x, y, wallImg2, true));
        }

        // Bottom side of the square
        for (int i = 0; i < 5; i++) {
            int x = centerX - (5 / 2 * wallWidth) + i * wallWidth;
            int y = centerY + (5 / 2 * wallHeight);
            walls.add(new BreakableWall(x, y, wallImg2, true));
        }

        // Left side of the square
        for (int i = 0; i < 5; i++) {
            int x = centerX - (5 / 2 * wallWidth);
            int y = centerY - (5 / 2 * wallHeight) + i * wallHeight;
            walls.add(new BreakableWall(x, y, wallImg2, true));
        }

        // Right side of the square
        for (int i = 0; i < 5; i++) {
            int x = centerX + (5 / 2 * wallWidth);
            int y = centerY - (5 / 2 * wallHeight) + i * wallHeight;
            walls.add(new BreakableWall(x, y, wallImg2, true));
        }

        // Power ups
        powerUps = new CopyOnWriteArrayList<>();
        powerUps.add(new HealthPowerUp(world.getWidth() / 2, world.getHeight() / 2, healthPowerUpImg));
        powerUps.add(new SpeedPowerUp(200, 400, speedPowerUpImg));
        powerUps.add(new TurretPowerUp(1000, 500, turretPowerUpImg));
    }

    public boolean isGameOver() {
        return (t1.getHealth3() <= 0 && t1.getExtraHealth() <= 0) ||
                (t2.getHealth3() <= 0 && t2.getExtraHealth() <= 0);
    }


    public void draw() {
        Graphics2D buffer = world.createGraphics();

        // Floor
        for (int x = 0; x < world.getWidth(); x += backgroundImage.getWidth()) {
            for (int y = 0; y < world.getHeight(); y += backgroundImage.getHeight()) {
                buffer.drawImage(backgroundImage, x, y, null);
            }
        }
        // Power ups
        for (PowerUps powerUp : powerUps) {
            powerUp.drawImage(buffer);
        }

        // Walls
        for (Wall wall : walls) {
            wall.drawImage(buffer);
        }
    }

    private void displayMiniMap(Graphics2D g2, int x, int y) {

        double scaler = 0.1;

        int miniMapWidth = (int) (GameConstants.GAME_WORLD_WIDTH * scaler);
        int miniMapLength = (int) (GameConstants.GAME_WORLD_LENGTH * scaler);

        g2.drawImage(world, x, y, miniMapWidth, miniMapLength, null);
    }


    private void displaySplitScreen(Graphics2D g2, Tank tank, int offsetX, int offsetY) {
        int screenWidth = GameConstants.GAME_SCREEN_WIDTH / 2;
        int screenHeight = GameConstants.GAME_SCREEN_HEIGHT;
        int screenX = tank.getScreen_x();
        int screenY = tank.getScreen_y();

        // Ensure the subimage bounds are within the world image bounds
        if (screenX < 0) screenX = 0;
        if (screenY < 0) screenY = 0;
        if (screenX + screenWidth > world.getWidth()) screenX = world.getWidth() - screenWidth;
        if (screenY + screenHeight > world.getHeight()) screenY = world.getHeight() - screenHeight;

        // Get the subimage and draw it
        BufferedImage subImage = world.getSubimage(screenX, screenY, screenWidth, screenHeight);
        g2.drawImage(subImage, offsetX, offsetY, null);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.draw();

        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        this.t1.drawHealthBar(buffer);
        this.t2.drawHealthBar(buffer);

        displaySplitScreen(g2, t1, 0, 0);
        displaySplitScreen(g2, t2, GameConstants.GAME_SCREEN_WIDTH / 2 + 4, 0);

        for (Bullet bullet : bullets) {
            bullet.drawImage(g2);
        }

        this.displayMiniMap(g2, 0, 0);
        this.displayMiniMap(g2, GameConstants.GAME_SCREEN_WIDTH - 180, 0);
    }
}

