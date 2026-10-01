import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ball {
    private int x, y;
    private int size;
    private int dx = 3;
    private int dy = -4;

    public Ball(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void update() {
        x += dx;
        y += dy;

        // Rimbalzi sui bordi laterali
        if (x <= 0 || x >= 600 - size - 15) {
            dx = -dx;
        }
        // Rimbalzo sul bordo superiore
        if (y <= 0) {
            dy = -dy;
        }
    }

    // Ripristina la posizione e la direzione iniziale per il riavvio
        public void reset(int startX, int startY) {
            this.x = startX;
            this.y = startY;
            this.dx = 3;
            this.dy = -4;
        }

    public void reverseY() {
        dy = -dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, size, size);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public int getY() {
        return y;
    }
}
