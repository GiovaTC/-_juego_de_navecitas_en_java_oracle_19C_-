import javax.swing.JFrame;

public class JuegoMain {

    public static void main(String[] args) {
        JFrame ventana = new JFrame("Juego de Navecitas 🚀");

        GamePanel panel = new GamePanel();

     //   ventana.add(panel);
        ventana.setSize(800,600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

     //   panel.startGame();

    }
}