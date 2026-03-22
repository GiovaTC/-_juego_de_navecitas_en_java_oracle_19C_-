import java.sql.*;

public class RankingDAO
{
    public static void mostrarRanking()
    {
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
