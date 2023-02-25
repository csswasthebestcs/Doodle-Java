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
    private int characterX = 100; // Position horizontale du personnage
    private int characterY = 500; // Position verticale du personnage

    private int platformY = 650; // Position verticale de la plateforme
    private boolean jumping = false; // Indique si le personnage est en train de sauter
    private int jumpCount = 0; // Compteur pour le saut du personnage
    private int fallSpeed = 10; // Vitesse de chute du personnage
    private int platformCount = 1; // Compteur pour la génération de plateformes
    private ArrayList<Platform> platforms = new ArrayList<Platform>(); // Liste des plateformes
    private Random random = new Random(); // Objet Random pour la génération aléatoire de plateformes
    private Timer timer;
    public DoodleJumpGame() {
        // Initialisation de la fenêtre de jeu
        setTitle("Doodle Jump");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700); // Définissez la taille de la fenêtre de jeu ici
        setLocationRelativeTo(null); // Centrez la fenêtre sur l'écran
        setResizable(false); // Empêchez la redimension de la fenêtre
        setVisible(true); // Rendre la fenêtre visible

        // Ajoutez le KeyListener pour gérer les entrées de l'utilisateur
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Démarrez le Timer pour la gestion de la logique du jeu
        this.timer = new Timer(50, this); // fix: add "this." before "timer" to initialize the class variable
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Dessinez le personnage
        g.setColor(Color.RED);
        g.fillRect(characterX, characterY, 50, 50); // Dessinez un rectangle rouge pour représenter le personnage

        // Dessinez les plateformes
        for (Platform platform : platforms) {
            g.setColor(Color.BLUE);
            g.fillRect(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
        }
    }

    public boolean collision(int x1, int y1, int width1, int height1, int x2, int y2, int radius, int platformWidth) {
        // Calculez la distance entre le centre du cercle et le centre du rectangle
        int dx = x1 + width1 / 2 - x2 - platformWidth / 2;
        int dy = y1 + height1 / 2 - y2;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Vérifiez si le cercle et le rectangle se chevauchent
        if (distance < radius + Math.max(width1, height1) / 2) {
            return true;
        }
        return false;
    }



    public void gameOver() {
        // Arrêtez le Timer pour la gestion de la logique du jeu
        timer.stop();

        // Affichez un message de fin de jeu
        JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);

        // Fermez la fenêtre de jeu
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

        // Vérifiez si le personnage est tombé en bas de la fenêtre de jeu
        if (characterY > getHeight()) {
            gameOver();
        }

        // Vérifiez si le personnage a atteint la plateforme
        for (Platform platform : platforms) {
            if (collision(characterX, characterY, 50, 50, platform.getX() + 50, platform.getY() + 5, 5, 50)) {
                jumping = true;
                fallSpeed = -40;
                break; // exit the loop if a collision is detected
            }
        }

        // Générez de nouvelles plateformes aléatoirement
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

        // Déplacez les plateformes vers le bas
        for (int i = 0; i < platforms.size(); i++) {
            Platform platform = platforms.get(i);
            platform.move(0, 5); // Déplacez la plateforme vers le bas
            if (platform.getY() > getHeight()) {
                platforms.remove(platform);
                i--;
            }
        }

        // Redessinez la fenêtre de jeu
        repaint();
    }



    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE && !jumping) { // Définissez la touche de saut ici
            jumping = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Ne faites rien ici
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Ne faites rien ici
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        jump();
    }

    public static void main(String[] args) {
        new DoodleJumpGame();
    }
}