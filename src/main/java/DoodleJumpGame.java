import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

import builds.Platform;

public class DoodleJumpGame extends JFrame implements KeyListener, ActionListener {
    private int characterX = 100;
    private int characterY = 500;

    private int platformY = 650;
    private boolean jumping = false;
    private int jumpCount = 0;
    private int fallSpeed = 10;
    private int platformCount = 1;
    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private Random random = new Random();
    private Timer timer;
    public DoodleJumpGame() {
        setTitle("Doodle Jump");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        this.timer = new Timer(50, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.RED);
        g.fillRect(characterX, characterY, 50, 50);

        for (Platform platform : platforms) {
            g.setColor(Color.BLUE);
            g.fillRect(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
        }
    }

    public boolean collision(int x1, int y1, int width1, int height1, int x2, int y2, int radius, int platformWidth) {
        int dx = x1 + width1 / 2 - x2 - platformWidth / 2;
        int dy = y1 + height1 / 2 - y2;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < radius + Math.max(width1, height1) / 2) {
            return true;
        }
        return false;
    }



    public void gameOver() {
        timer.stop();

        JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);

        dispose();
    }

    public void jump() {
        if (jumping) {
            if (jumpCount < 50) {
                characterY -= 10;
                jumpCount++;
            } else {
                jumping = false;
                jumpCount = 0;
            }
        } else {
            characterY += fallSpeed;
        }

        if (characterY > getHeight()) {
            gameOver();
        }

        for (Platform platform : platforms) {
            if (collision(characterX, characterY, 50, 50, platform.getX() + 50, platform.getY() + 5, 5, 50)) {
                jumping = true;
                fallSpeed = -40;
                break;
            }
        }

        if (platforms.size() < 5) {
            while (true) {
                int platformX = random.nextInt(getWidth() - 100);
                boolean validPlatform = true;
                for (Platform platform : platforms) {
                    if (collision(platformX, platformY, platform.getWidth(), platform.getHeight(), platform.getX(), platform.getY(), 5, 50)) {
                        validPlatform = false;
                        break;
                    }
                }
                if (validPlatform) {
                    platforms.add(new Platform(platformX, platformY, 100, 10));
                    break;
                }
            }
        }

        for (int i = 0; i < platforms.size(); i++) {
            Platform platform = platforms.get(i);
            platform.move(0, 5);
            if (platform.getY() > getHeight()) {
                platforms.remove(platform);
                i--;
            }
        }

        repaint();
    }



    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE && !jumping) {
            jumping = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        jump();
    }

    public static void main(String[] args) {
        new DoodleJumpGame();
    }
}