import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SimpleGame extends JPanel implements ActionListener, KeyListener {
    private int playerX = 175;
    private int playerY = 480;
    private int playerSpeed = 20;
    private ArrayList<Integer> enemyX = new ArrayList<>();
    private ArrayList<Integer> enemyY = new ArrayList<>();
    private int enemySpeed = 15;
    private Timer timer;
    private boolean flagGameOver = false;
    private int score = 0;

    public SimpleGame() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(100, this);
        timer.start();
        spawnEnemy();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Game");
        SimpleGame game = new SimpleGame();
        frame.add(game);
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK); //поле
        g.fillRect(0, 0, 400, 600);

        g.setColor(Color.WHITE); //квадратик
        g.fillRect(playerX, playerY, 65, 65);

        for (int i = 0; i < enemyX.size(); i++) {
            g.setColor(Color.RED);
            g.fillOval(enemyX.get(i), enemyY.get(i), 20, 20);
        }

        g.setColor(Color.WHITE); //счет
        g.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        g.drawString("Score: " + score, 10, 30);

        if (flagGameOver) {
            g.setFont(new Font("Times New Roman", Font.PLAIN, 40));
            g.drawString("End of game", 90, 300);
            g.drawString("Enter Space to restart", 20, 340);
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Random rand = new Random();
        if (!flagGameOver) {
            for (int i = 0; i < enemyY.size(); i++) {
                enemyY.set(i, enemyY.get(i) + enemySpeed);
                if (enemyY.get(i) >= 600) {
                    enemyX.remove(i);
                    enemyY.remove(i);
                    score++;
                    i--;
                }
            }

            if (rand.nextInt(100) < 20) {
                spawnEnemy();
            }

            checkCollision();
            repaint();
        }
    }

    public void spawnEnemy() {
        Random rand = new Random();
        int x = rand.nextInt(350);
        int y = 0;
        enemyX.add(x);
        enemyY.add(y);
    }

    public void checkCollision() {
        Rectangle playerBounds = new Rectangle(playerX, playerY, 50, 50);
        for (int  i = 0; i < enemyX.size(); i++) {
            Rectangle enemyBounds = new Rectangle(enemyX.get(i), enemyY.get(i), 20, 20);
            if (playerBounds.intersects(enemyBounds)) {
                flagGameOver = true;
                break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!flagGameOver) {
            if (key == KeyEvent.VK_LEFT && playerX > 0) {
                playerX -= playerSpeed;
            }
            if (key == KeyEvent.VK_RIGHT && playerX < 350) {
                playerX += playerSpeed;
            }
        } else {
            if (key == KeyEvent.VK_SPACE) {
                resetGame();
            }
        }
    }

    public void resetGame() {
        playerX = 175;
        playerY = 480;
        enemyX.clear();
        enemyY.clear();
        score = 0;
        flagGameOver = false;
        timer.start();
        spawnEnemy();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}