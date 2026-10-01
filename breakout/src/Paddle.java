import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Paddle {

    private int x, y;
    private int width, height;
    private int dx = 0;

    // costruttore
    public Paddle(int x, int y, int width, int height){
        this.x = x;
        this.y = y; 
        this.width = width;
        this.height = height;
    }

    //limiti dello schermo
    public void update(){
        x += dx;
        if (x < 0) {
            x = 0;
        }
        if (x > 600 - width - 15) { // 15px di compensazione bordi finestra
            x = 600 - width - 15;
        }
    }

    // disegna la racchetta
    public void draw(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(x, y, width, height);
    }
    
    // metodi per muovere la racchetta
    public void KeyPressed(KeyEvent e){
        int key = e.getKeyCode();

        // indichiamo che si puo muovere con la Freccia sx o A per andare a sinistra
        if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
            dx = -7;
        }
        // indichiamo che si puo muovere con la Freccia dx o D per andare a destra
        if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
            dx = 7; 
        }
    }

    // metodi per il rilascio di tasti
    public void keyReleased(KeyEvent e){
        // quando rilascio il tasto la racchetta si ferma
       int key = e.getKeyCode();
       if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
            dx = 0;
       }
    }

    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height);
    }
}

