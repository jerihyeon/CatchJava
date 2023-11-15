import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BlockGame extends JFrame {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;
    private static final int BALL_SIZE = 20;
    private static final int BRICK_WIDTH = 50;
    private static final int BRICK_HEIGHT = 20; // 수정된 부분
    private static final int NUM_BRICKS = 40;

    private int paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
    private int ballX = WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = HEIGHT - PADDLE_HEIGHT - BALL_SIZE;
    private int ballSpeedX = 3;
    private int ballSpeedY = -3;

    private boolean leftKeyPressed = false;
    private boolean rightKeyPressed = false;

    private boolean[] bricks;

    public BlockGame() {
        setTitle("Block Breaker Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftKeyPressed = true;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightKeyPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftKeyPressed = false;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightKeyPressed = false;
                }
            }
        });

        bricks = new boolean[NUM_BRICKS];
        for (int i = 0; i < NUM_BRICKS; i++) {
            bricks[i] = true;
        }

        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        timer.start();
    }

    private void update() {
        if (leftKeyPressed && paddleX > 0) {
            paddleX -= 5;
        }
        if (rightKeyPressed && paddleX < WIDTH - PADDLE_WIDTH) {
            paddleX += 5;
        }

        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballX <= 0 || ballX >= WIDTH - BALL_SIZE) {
            ballSpeedX = -ballSpeedX;
        }
        if (ballY <= 0) {
            ballSpeedY = -ballSpeedY;
        }

        if (ballY >= HEIGHT - BALL_SIZE - PADDLE_HEIGHT && ballX >= paddleX && ballX <= paddleX + PADDLE_WIDTH) {
            ballSpeedY = -ballSpeedY;
        }

        for (int i = 0; i < NUM_BRICKS; i++) {
            if (bricks[i] && ballX >= i * BRICK_WIDTH && ballX <= (i + 1) * BRICK_WIDTH && ballY >= 0 && ballY <= BRICK_HEIGHT) {
                ballSpeedY = -ballSpeedY;
                bricks[i] = false;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw paddle
        g.fillRect(paddleX, HEIGHT - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw ball
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Draw bricks
        for (int i = 0; i < NUM_BRICKS; i++) {
            if (bricks[i]) {
                g.fillRect(i * BRICK_WIDTH, 0, BRICK_WIDTH, BRICK_HEIGHT);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BlockGame().setVisible(true);
            }
        });
    }
}
