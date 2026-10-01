import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Brick {
    private int x, y;
    private int width, height;
    private boolean destroyed = false; // integri inizialmente

    public Brick(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        if (!destroyed) {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
            
            // Bordo nero per separare i mattoncini
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}