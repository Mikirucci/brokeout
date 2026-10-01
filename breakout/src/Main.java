import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) throws Exception {
    JFrame frame = new JFrame("Breakout Game OOP");
        GamePanel panel = new GamePanel();

        frame.add(panel);
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
