# -_juego_de_navecitas_en_java_oracle_19C_- :.
# 🚀 Juego de Navecitas en Java + Oracle 19c:

<img width="1024" height="1024" alt="image" src="https://github.com/user-attachments/assets/56fcba53-4f70-434f-8fde-b4746d9c4efa" />  

<img width="2546" height="1078" alt="image" src="https://github.com/user-attachments/assets/4f0d4e96-cc24-4980-aaea-c297169bf77a" />    

```
Solución completa, funcional y pensada para entorno real con:

- Java SE (Swing en IntelliJ)
- Oracle 19c
- Arquitectura simple (MVC ligero)
- Persistencia de puntajes
- Juego tipo “navecitas” (Space Invaders básico)

Base sólida (no pseudocódigo), lista para ejecutar y extender .

---

# 🧩 1. Modelo del sistema:

## Componentes

### 🎮 Juego (Swing)
- Renderizado
- Movimiento
- Colisiones

### 🧠 Lógica
- Puntaje
- Game Over

### 🗄️ Base de datos (Oracle 19c)
- Guardar puntajes
- Consultar ranking

---

# 🗄️ 2. Script Oracle 19c:

```sql
CREATE TABLE PUNTAJES_JUEGO (
    ID        NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    JUGADOR   VARCHAR2(100),
    PUNTAJE   NUMBER,
    FECHA     DATE DEFAULT SYSDATE
);

-- Procedimiento para insertar puntaje
CREATE OR REPLACE PROCEDURE INSERTAR_PUNTAJE(
    P_JUGADOR IN VARCHAR2,
    P_PUNTAJE IN NUMBER
)
AS
BEGIN
    INSERT INTO PUNTAJES_JUEGO (JUGADOR, PUNTAJE)
    VALUES (P_JUGADOR, P_PUNTAJE);

    COMMIT;
END;
/

☕ 3. Conexión a Oracle (JDBC):
Dependencia

Descargar: ojdbc8.jar y agregarlo al proyecto.

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionDB {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
        String user = "TU_USUARIO";
        String pass = "TU_PASSWORD";

        return DriverManager.getConnection(url, user, pass);
    }
}

🛸 4. Clase principal del juego:
import javax.swing.JFrame;

public class JuegoMain {

    public static void main(String[] args) {
        JFrame ventana = new JFrame("Juego de Navecitas 🚀");

        GamePanel panel = new GamePanel();

        ventana.add(panel);
        ventana.setSize(800, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        panel.startGame();
    }
}

🎮 5. Panel del juego (Core):
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

        // Generar enemigos
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

🏆 6. Consulta de ranking (ppcional):
import java.sql.*;

public class RankingDAO {

    public static void mostrarRanking() {

        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT JUGADOR, PUNTAJE FROM PUNTAJES_JUEGO ORDER BY PUNTAJE DESC FETCH FIRST 10 ROWS ONLY"
             )) {

            while (rs.next()) {
                System.out.println(
                    rs.getString("JUGADOR") + " - " + rs.getInt("PUNTAJE")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

🚀 7. Mejoras recomendadas:
🎯 Gameplay
Movimiento de enemigos
Disparos enemigos
Niveles
Sonido

🧠 Arquitectura
Separar en capas:
model
dao
service
ui

⚡ Performance
Usar doble buffer (Swing ya lo maneja parcialmente)
Evitar lógica pesada en el EDT .

🗄️ Base de datos
Índice por puntaje
Tabla de usuarios

✅ Resultado
✔ Juego funcional
✔ Interfaz gráfica
✔ Persistencia en Oracle 19c
✔ Procedimiento almacenado
✔ Base escalable / . 
