package TankGame.src.menus;

import TankGame.src.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EndGamePanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;
    private String winnerMessage = " ";
    private JLabel backgroundLabel;

    public EndGamePanel(Launcher lf) {
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("TankGame/resources/title.png"));
        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        ImageIcon fireworkGif = new ImageIcon("out/production/csc413-tankgame-juliabui/images/fireworks.gif");
        if (fireworkGif.getIconWidth() == -1) {
            System.out.println("Fireworks GIF not found.");
        }
        backgroundLabel = new JLabel(fireworkGif);
        backgroundLabel.setBounds(0, 0, fireworkGif.getIconWidth(), fireworkGif.getIconHeight());
        this.add(backgroundLabel);

        JButton restart = new JButton("Restart");
        restart.setFont(new Font("Courier New", Font.BOLD, 24));
        restart.setBounds(150, 300, 250, 50);
        restart.addActionListener((actionEvent -> this.lf.restartGame()));
        this.add(restart);

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(150, 400, 250, 50);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));
        this.add(exit);

    }

    public void setWinnerMessage(String winnerMessage) {
        this.winnerMessage = winnerMessage;
        this.repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, null);



        g2.setColor(Color.RED);
        g2.setFont(new Font("Courier New", Font.BOLD, 36));
        g2.drawString(winnerMessage, 150, 200);
    }
}


