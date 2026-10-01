import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private Paddle paddle;
    private Ball ball;
    private Brick[][] bricks;
    private Timer timer;

    private final int ROWS = 3;
    private final int COLS = 7;

    // Stato del gioco
    private boolean isGameOver = false;
    private boolean isGameWon = false;
    private int score = 0;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        initGame();

        timer = new Timer(16, this);
        timer.start();
    }

    // Inizializza o reinizia la partita
    private void initGame() {
        paddle = new Paddle(250, 330, 80, 15);
        ball = new Ball(290, 200, 12);
        
        bricks = new Brick[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int bx = c * 75 + 30;
                int by = r * 25 + 40;
                bricks[r][c] = new Brick(bx, by, 70, 20);
            }
        }
        
        isGameOver = false;
        isGameWon = false;
        score = 0;
    }

    // Controlla se tutti i mattoncini sono stati distrutti
    private boolean checkWinCondition() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (!bricks[r][c].isDestroyed()) {
                    return false; // Almeno un mattoncino è ancora sano
                }
            }
        }
        return true; // Tutti i mattoncini sono stati distrutti
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Disegna gli elementi di gioco
        paddle.draw(g);
        ball.draw(g);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                bricks[r][c].draw(g);
            }
        }

        // --- INTERFACCIA UTENTE (UI) ---
        // Disegna il Punteggio in alto a sinistra
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Punti: " + score, 20, 25);

        // --- SCHERMATA GAME OVER ---
        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("GAME OVER", 190, 200);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Premi SPAZIO per Riavviare", 185, 240);
        }

        // --- SCHERMATA VITTORIA ---
        if (isGameWon) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("HAI VINTO!", 205, 200);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Punteggio finale: " + score, 215, 230);
            g.drawString("Premi SPAZIO per Rigiocare", 185, 260);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Aggiorna il gioco solo se la partita è in corso
        if (!isGameOver && !isGameWon) {
            paddle.update();
            ball.update();

            // 1. Controllo caduta pallina (Game Over)
            if (ball.getY() > 400) {
                isGameOver = true;
            }

            // 2. Collisione Pallina - Racchetta
            if (ball.getBounds().intersects(paddle.getBounds())) {
                ball.reverseY();
            }

            // 3. Collisione Pallina - Mattoncini
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    Brick brick = bricks[r][c];
                    if (!brick.isDestroyed() && ball.getBounds().intersects(brick.getBounds())) {
                        brick.setDestroyed(true);
                        ball.reverseY();
                        score += 10; // Aggiunge 10 punti per ogni mattoncino

                        // Controllo Vittoria
                        if (checkWinCondition()) {
                            isGameWon = true;
                        }
                    }
                }
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!isGameOver && !isGameWon) {
            paddle.KeyPressed(e);
        } else {
            // Se la partita è finita (vittoria o sconfitta), SPAZIO riavvia
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                initGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!isGameOver && !isGameWon) {
            paddle.keyReleased(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}