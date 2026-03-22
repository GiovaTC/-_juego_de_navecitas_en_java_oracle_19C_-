import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    Timer timer;
    int playerX = 350;
    int score = 0;
    boolean gameOver = false;

    ArrayList<Rectangle> enemigos = new ArrayList<>();
    ArrayList<Rectangle> balas = new ArrayList<>();

    public GamePanel() {
        timer = new Timer(20, this);
        addKeyListener(this);
        setFocusable(true);

        // generar enemigos .
        for (int i = 0; i < 5; i++) {
            enemigos.add(new Rectangle(100 + i * 120, 50, 40, 40));
        }
    }

    public void startGame() {
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Jugador
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 500, 50, 20);

        // Balas
        g.setColor(Color.YELLOW);
        for (Rectangle b : balas) {
            g.fillRect(b.x, b.y, b.width, b.height);
        }

        // Enemigos
        g.setColor(Color.RED);
        for (Rectangle e : enemigos) {
            g.fillRect(e.x, e.y, e.width, e.height);
        }

        // Score
        g.setColor(Color.WHITE);
        g.drawString("Puntaje: " + score, 10, 20);

        if (gameOver) {
            g.drawString("GAME OVER", 350, 300);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Mover balas
        for (Rectangle b : balas) {
            b.y -= 10;
        }

        // Colisiones
        ArrayList<Rectangle> eliminar = new ArrayList<>();

        for (Rectangle b : balas) {
            for (Rectangle enemigo : enemigos) {
                if (b.intersects(enemigo)) {
                    eliminar.add(enemigo);
                    score += 10;
                }
            }
        }

        enemigos.removeAll(eliminar);

        // Fin del juego
        if (enemigos.isEmpty()) {
            gameOver = true;
            timer.stop();
            guardarPuntaje();
        }

        repaint();
    }

    // Guardar en Oracle
    private void guardarPuntaje() {
        String jugador = JOptionPane.showInputDialog("Nombre del jugador:");

        try (var con = ConexionDB.getConnection();
             var stmt = con.prepareCall("{call INSERTAR_PUNTAJE(?, ?)}")) {

            stmt.setString(1, jugador);
            stmt.setInt(2, score);

            stmt.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            playerX -= 20;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerX += 20;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            balas.add(new Rectangle(playerX + 20, 480, 5, 10));
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
