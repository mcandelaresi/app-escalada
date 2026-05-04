import vista.Menu.Menu;
import dao.ConnectionDB;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conn = ConnectionDB.getConnection();
        if (conn == null) {
            System.out.println("Error de conexión");
            return;
        }
        System.out.println("Conexión OK\n");
        
        Menu menu = new Menu();
        menu.menu();
    }
}
