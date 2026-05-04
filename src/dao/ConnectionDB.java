package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class ConnectionDB {

    private static final String URL = "jdbc:sqlite:bdd/escalada.db";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL);
            if (connection != null) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                    inicialitzarBaseDades(stmt);
                }
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("Error de connexió a SQLite: " + e.getMessage());
            return null;
        }
    }

    private static void inicialitzarBaseDades(Statement stmt) throws SQLException {
        stmt.execute("CREATE TABLE IF NOT EXISTS poblacions (" +
                "id_poblacio INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT NOT NULL UNIQUE)");

        stmt.execute("CREATE TABLE IF NOT EXISTS escoles (" +
                "id_escola INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT NOT NULL UNIQUE, " +
                "aproximacio TEXT, " +
                "popularitat TEXT, " +
                "restriccions TEXT)");

        stmt.execute("CREATE TABLE IF NOT EXISTS sectors (" +
                "id_sector INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT NOT NULL, " +
                "latitud REAL, " +
                "longitud REAL, " +
                "aproximacio TEXT, " +
                "popularitat TEXT, " +
                "restriccions TEXT, " +
                "id_escola INTEGER NOT NULL, " +
                "UNIQUE(nom, id_escola), " +
                "FOREIGN KEY (id_escola) REFERENCES escoles(id_escola) ON DELETE CASCADE)");

        stmt.execute("CREATE TABLE IF NOT EXISTS escalador (" +
                "id_escalador INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT NOT NULL, " +
                "alias TEXT NOT NULL, " +
                "edat INTEGER, " +
                "nivell_max TEXT, " +
                "estil_preferit TEXT, " +
                "id_via_max INTEGER)");

        stmt.execute("CREATE TABLE IF NOT EXISTS vies (" +
                "id_via INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT NOT NULL, " +
                "grau TEXT, " +
                "orientacio TEXT, " +
                "estat TEXT, " +
                "data_estat TEXT, " +
                "tipus TEXT, " +
                "ancoratges TEXT, " +
                "tipus_roca TEXT, " +
                "id_creador INTEGER, " +
                "id_sector INTEGER NOT NULL, " +
                "id_escola INTEGER NOT NULL, " +
                "restriccions TEXT, " +
                "UNIQUE(nom, id_escola), " +
                "FOREIGN KEY (id_sector) REFERENCES sectors(id_sector) ON DELETE CASCADE, " +
                "FOREIGN KEY (id_escola) REFERENCES escoles(id_escola) ON DELETE CASCADE, " +
                "FOREIGN KEY (id_creador) REFERENCES escalador(id_escalador) ON DELETE SET NULL)");

        stmt.execute("CREATE TABLE IF NOT EXISTS vies_esportiva (" +
                "id_via INTEGER PRIMARY KEY, " +
                "llargada INTEGER NOT NULL, " +
                "FOREIGN KEY (id_via) REFERENCES vies(id_via) ON DELETE CASCADE)");

        stmt.execute("CREATE TABLE IF NOT EXISTS vies_classica (" +
                "id_via INTEGER PRIMARY KEY, " +
                "ancoratges_permesos TEXT, " +
                "FOREIGN KEY (id_via) REFERENCES vies(id_via) ON DELETE CASCADE)");

        stmt.execute("CREATE TABLE IF NOT EXISTS vies_gel (" +
                "id_via INTEGER PRIMARY KEY, " +
                "FOREIGN KEY (id_via) REFERENCES vies(id_via) ON DELETE CASCADE)");

        stmt.execute("CREATE TABLE IF NOT EXISTS trams (" +
                "id_tram INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "num_llarg INTEGER, " +
                "llarg INTEGER, " +
                "grau_dificultat TEXT, " +
                "id_via INTEGER NOT NULL, " +
                "FOREIGN KEY (id_via) REFERENCES vies(id_via) ON DELETE CASCADE)");

        stmt.execute("CREATE TABLE IF NOT EXISTS registres (" +
                "id_registre INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_escalador INTEGER NOT NULL, " +
                "id_via INTEGER NOT NULL, " +
                "data_ascensio TEXT, " +
                "estil TEXT, " +
                "FOREIGN KEY (id_escalador) REFERENCES escalador(id_escalador) ON DELETE CASCADE, " +
                "FOREIGN KEY (id_via) REFERENCES vies(id_via) ON DELETE CASCADE)");
    }
}