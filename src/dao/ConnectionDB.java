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
                    insertarDatosEjemplo(stmt);
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

    private static void insertarDatosEjemplo(Statement stmt) throws SQLException {
        // Insertar POBLACIONS
        stmt.execute("INSERT OR IGNORE INTO poblacions (id_poblacio, nom) VALUES (1, 'Barcelona')");
        stmt.execute("INSERT OR IGNORE INTO poblacions (id_poblacio, nom) VALUES (2, 'Montserrat')");
        stmt.execute("INSERT OR IGNORE INTO poblacions (id_poblacio, nom) VALUES (3, 'Camarasa')");
        stmt.execute("INSERT OR IGNORE INTO poblacions (id_poblacio, nom) VALUES (4, 'Siurana')");
        stmt.execute("INSERT OR IGNORE INTO poblacions (id_poblacio, nom) VALUES (5, 'Margalef')");

        // Insertar ESCOLES
        stmt.execute("INSERT OR IGNORE INTO escoles (id_escola, nom, aproximacio, popularitat, restriccions) " +
                "VALUES (1, 'Montserrat', '1 hora de Barcelona', 'Alta', 'Ninguna')");
        stmt.execute("INSERT OR IGNORE INTO escoles (id_escola, nom, aproximacio, popularitat, restriccions) " +
                "VALUES (2, 'Camarasa', '1.5 horas de Barcelona', 'Mitjana', 'Ninguna')");
        stmt.execute("INSERT OR IGNORE INTO escoles (id_escola, nom, aproximacio, popularitat, restriccions) " +
                "VALUES (3, 'Siurana', '2 horas de Barcelona', 'Alta', 'Ninguna')");
        stmt.execute("INSERT OR IGNORE INTO escoles (id_escola, nom, aproximacio, popularitat, restriccions) " +
                "VALUES (4, 'Margalef', '2.5 horas de Barcelona', 'Mitjana', 'Ninguna')");

        // Insertar SECTORS
        stmt.execute("INSERT OR IGNORE INTO sectors (id_sector, nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola) " +
                "VALUES (1, 'La Marana', 41.8383, 1.8367, 'Fácil acceso', 'Alta', 'Ninguna', 1)");
        stmt.execute("INSERT OR IGNORE INTO sectors (id_sector, nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola) " +
                "VALUES (2, 'Sector Comercial', 41.8367, 1.8350, 'Acceso rápido', 'Alta', 'Ninguna', 1)");
        stmt.execute("INSERT OR IGNORE INTO sectors (id_sector, nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola) " +
                "VALUES (3, 'Ribera', 42.0467, 0.8333, 'Cerca del río', 'Mitjana', 'Ninguna', 2)");
        stmt.execute("INSERT OR IGNORE INTO sectors (id_sector, nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola) " +
                "VALUES (4, 'Montsant', 41.5000, 0.8733, 'Vistas impresionantes', 'Alta', 'Protegida en época de cría', 3)");
        stmt.execute("INSERT OR IGNORE INTO sectors (id_sector, nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola) " +
                "VALUES (5, 'Pared Este', 41.5167, 0.9233, 'Acceso sencillo', 'Mitjana', 'Ninguna', 4)");

        // Insertar ESCALADORES
        stmt.execute("INSERT OR IGNORE INTO escalador (id_escalador, nom, alias, edat, nivell_max, estil_preferit) " +
                "VALUES (1, 'Joan Miquel Garcia', 'JM_Escalada', 28, '7a+', 'Esportiva')");
        stmt.execute("INSERT OR IGNORE INTO escalador (id_escalador, nom, alias, edat, nivell_max, estil_preferit) " +
                "VALUES (2, 'Maria Bonet Pujol', 'MariaB', 25, '6c+', 'Clàssica')");
        stmt.execute("INSERT OR IGNORE INTO escalador (id_escalador, nom, alias, edat, nivell_max, estil_preferit) " +
                "VALUES (3, 'Pere Rovira Martinez', 'PereRM', 35, '7b', 'Esportiva')");
        stmt.execute("INSERT OR IGNORE INTO escalador (id_escalador, nom, alias, edat, nivell_max, estil_preferit) " +
                "VALUES (4, 'Anna Puig Lopez', 'AnnaP', 22, '6b+', 'Gel')");
        stmt.execute("INSERT OR IGNORE INTO escalador (id_escalador, nom, alias, edat, nivell_max, estil_preferit) " +
                "VALUES (5, 'Jaume Costa Vidal', 'JaumeCV', 45, '6a', 'Clàssica')");

        // Insertar VIES
        stmt.execute("INSERT OR IGNORE INTO vies (id_via, nom, grau, orientacio, estat, data_estat, tipus, id_creador, id_sector, id_escola, restriccions) " +
                "VALUES (1, 'La Aguja', '6a', 'SE', 'Apte', '2024-01-15', 'Esportiva', 1, 1, 1, 'Ninguna')");
        stmt.execute("INSERT OR IGNORE INTO vies (id_via, nom, grau, orientacio, estat, data_estat, tipus, id_creador, id_sector, id_escola, restriccions) " +
                "VALUES (2, 'El Abuelo', '6c+', 'SO', 'Apte', '2024-02-10', 'Esportiva', 3, 2, 1, 'Ninguna')");
        stmt.execute("INSERT OR IGNORE INTO vies (id_via, nom, grau, orientacio, estat, data_estat, tipus, id_creador, id_sector, id_escola, restriccions) " +
                "VALUES (3, 'Rencor', '7a', 'E', 'Apte', '2023-12-01', 'Esportiva', 1, 4, 3, 'Ninguna')");
        stmt.execute("INSERT OR IGNORE INTO vies (id_via, nom, grau, orientacio, estat, data_estat, tipus, id_creador, id_sector, id_escola, restriccions) " +
                "VALUES (4, 'Repens Original', '5', 'N', 'Apte', '2024-01-20', 'Classica', 2, 1, 1, 'Solo grupos pequeños')");
        stmt.execute("INSERT OR IGNORE INTO vies (id_via, nom, grau, orientacio, estat, data_estat, tipus, id_creador, id_sector, id_escola, restriccions) " +
                "VALUES (5, 'Cascada de Hielo', '4+', 'NO', 'Construccio', '2024-01-10', 'Gel', 4, 4, 3, 'En mantenimiento')");

        // Insertar VIES ESPORTIVA
        stmt.execute("INSERT OR IGNORE INTO vies_esportiva (id_via, llargada) VALUES (1, 35)");
        stmt.execute("INSERT OR IGNORE INTO vies_esportiva (id_via, llargada) VALUES (2, 40)");
        stmt.execute("INSERT OR IGNORE INTO vies_esportiva (id_via, llargada) VALUES (3, 45)");

        // Insertar VIES CLASSICA
        stmt.execute("INSERT OR IGNORE INTO vies_classica (id_via, ancoratges_permesos) VALUES (4, 'Spits, Friends, Tascons')");

        // Insertar VIES GEL
        stmt.execute("INSERT OR IGNORE INTO vies_gel (id_via) VALUES (5)");

        // Insertar TRAMS
        // La Aguja (3 largos)
        stmt.execute("INSERT OR IGNORE INTO trams (id_tram, num_llarg, llarg, grau_dificultat, id_via) " +
                "VALUES (1, 1, 12, '5+', 1)");
        stmt.execute("INSERT OR IGNORE INTO trams (id_tram, num_llarg, llarg, grau_dificultat, id_via) " +
                "VALUES (2, 2, 12, '6a', 1)");
        stmt.execute("INSERT OR IGNORE INTO trams (id_tram, num_llarg, llarg, grau_dificultat, id_via) " +
                "VALUES (3, 3, 11, '5+', 1)");

        // El Abuelo (2 largos)
        stmt.execute("INSERT OR IGNORE INTO trams (id_tram, num_llarg, llarg, grau_dificultat, id_via) " +
                "VALUES (4, 1, 20, '6b+', 2)");
        stmt.execute("INSERT OR IGNORE INTO trams (id_tram, num_llarg, llarg, grau_dificultat, id_via) " +
                "VALUES (5, 2, 20, '6c+', 2)");

        // Rencor (2 largos)
        stmt.execute("INSERT OR IGNORE INTO trams (id_tram, num_llarg, llarg, grau_dificultat, id_via) " +
                "VALUES (6, 1, 22, '6c', 3)");
        stmt.execute("INSERT OR IGNORE INTO trams (id_tram, num_llarg, llarg, grau_dificultat, id_via) " +
                "VALUES (7, 2, 23, '7a', 3)");

        // Insertar REGISTROS (ASCENSOS)
        stmt.execute("INSERT OR IGNORE INTO registres (id_registre, id_escalador, id_via, data_ascensio, estil) " +
                "VALUES (1, 1, 1, '2024-02-15', 'Esportiva')");
        stmt.execute("INSERT OR IGNORE INTO registres (id_registre, id_escalador, id_via, data_ascensio, estil) " +
                "VALUES (2, 2, 4, '2024-02-14', 'Clàssica')");
        stmt.execute("INSERT OR IGNORE INTO registres (id_registre, id_escalador, id_via, data_ascensio, estil) " +
                "VALUES (3, 3, 2, '2024-02-10', 'Esportiva')");
        stmt.execute("INSERT OR IGNORE INTO registres (id_registre, id_escalador, id_via, data_ascensio, estil) " +
                "VALUES (4, 1, 3, '2024-02-08', 'Esportiva')");
        stmt.execute("INSERT OR IGNORE INTO registres (id_registre, id_escalador, id_via, data_ascensio, estil) " +
                "VALUES (5, 4, 5, '2024-01-20', 'Gel')");
    }
}