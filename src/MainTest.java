import dao.ConnectionDB;
import java.sql.Connection;

public class MainTest {
    public static void main(String[] args) {

        Connection conn = ConnectionDB.getConnection();

        if (conn != null) {
            System.out.println("Conexión OK a SQLite");
        } else {
            System.out.println("Error de conexión");
        }
    }
}