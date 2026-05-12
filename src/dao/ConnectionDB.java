package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class ConnectionDB {

    private static final String URL = "jdbc:sqlite:bdd/escalada.db";
    private static boolean inicialitzada = false;

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL);

            if (connection != null) {
                try (Statement stmt = connection.createStatement()) {

                    stmt.execute("PRAGMA foreign_keys = ON");

                    if (!inicialitzada) {
                        inicialitzarBaseDades(stmt);
                        inicialitzarDadesSeed(stmt);
                        inicialitzada = true;
                    }
                }
            }

            return connection;

        } catch (SQLException e) {
            System.err.println("Error de connexió SQLite: " + e.getMessage());
            return null;
        }
    }

    private static void inicialitzarBaseDades(Statement stmt) throws SQLException {

        // =========================
        // POBLACIONS
        // =========================
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS poblacions (
                id_poblacio INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL UNIQUE
            )
        """);

        // =========================
        // ESCOLES
        // =========================
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS escoles (
                id_escola INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL UNIQUE,
                aproximacio TEXT,
                popularitat TEXT CHECK(popularitat IN ('Baixa','Mitjana','Alta','baixa','mitjana','alta')),
                restriccions TEXT
            )
        """);

        // =========================
        // SECTORS
        // =========================
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS sectors (
                id_sector INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                latitud REAL,
                longitud REAL,
                aproximacio TEXT,
                popularitat TEXT CHECK(popularitat IN ('Baixa','Mitjana','Alta','baixa','mitjana','alta')),
                restriccions TEXT,
                tipus_sector TEXT CHECK(tipus_sector IN ('ESPORTIVA_CLASICA','GEL')),
                id_escola INTEGER NOT NULL,
                UNIQUE(nom, id_escola),
                FOREIGN KEY (id_escola) REFERENCES escoles(id_escola) ON DELETE CASCADE
            )
        """);

        // =========================
        // ESCALADORS
        // =========================
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS escalador (
                id_escalador INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                alias TEXT NOT NULL UNIQUE,
                edat INTEGER CHECK(edat > 0),
                nivell_max TEXT NOT NULL,
                estil_preferit TEXT CHECK(estil_preferit IN ('Esportiva','Clàssica','Classica','Gel')),
                id_via_max INTEGER,
                FOREIGN KEY (id_via_max) REFERENCES vies(id_via) ON DELETE SET NULL
            )
        """);

        // =========================
        // VIES
        // =========================
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS vies (
                id_via INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                grau TEXT CHECK(grau IN
                ('4','4+','5','5+','6a','6a+','6b','6b+','6c','6c+',
                 '7a','7a+','7b','7b+','7c','7c+','8a','8a+','8b',
                 '8b+','8c','8c+','9a','9a+','9b','9b+','9c','9c+')),

                orientacio TEXT CHECK(orientacio IN ('N','NE','NO','SE','SO','E','O','S')),

                estat TEXT CHECK(estat IN ('Apte','Construccio','Tancada')),

                data_estat TEXT,

                tipus TEXT CHECK(tipus IN ('Esportiva','Clàssica','Classica','Gel')),

                ancoratges TEXT CHECK(ancoratges IN
                ('Spits','Parabolts','Quimics',
                 'Friends','Tascons','Bagues','Pitons','Tricams','BigBros')),

                tipus_roca TEXT CHECK(tipus_roca IN
                ('Conglomerat','Granit','Calcaria','Arenisca','Altres')),

                id_creador INTEGER,
                id_sector INTEGER NOT NULL,
                id_escola INTEGER NOT NULL,

                restriccions TEXT,

                UNIQUE(nom, id_escola),

                FOREIGN KEY (id_sector) REFERENCES sectors(id_sector) ON DELETE CASCADE,
                FOREIGN KEY (id_escola) REFERENCES escoles(id_escola) ON DELETE CASCADE,
                FOREIGN KEY (id_creador) REFERENCES escalador(id_escalador) ON DELETE SET NULL
            )
        """);

        // =========================
        // ESPECÍFIQUES TIPUS VIES
        // =========================

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS vies_esportiva (
                id_via INTEGER PRIMARY KEY,
                llargada INTEGER CHECK(llargada BETWEEN 5 AND 30),
                FOREIGN KEY (id_via) REFERENCES vies(id_via) ON DELETE CASCADE
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS vies_classica (
                id_via INTEGER PRIMARY KEY,
                ancoratges_permesos TEXT,
                FOREIGN KEY (id_via) REFERENCES vies(id_via) ON DELETE CASCADE
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS vies_gel (
                id_via INTEGER PRIMARY KEY,
                FOREIGN KEY (id_via) REFERENCES vies(id_via) ON DELETE CASCADE
            )
        """);

        // =========================
        // TRAMS
        // =========================
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS trams (
                id_tram INTEGER PRIMARY KEY AUTOINCREMENT,
                num_llarg INTEGER,
                llarg INTEGER CHECK(llarg BETWEEN 15 AND 30),
                grau_dificultat TEXT,
                id_via INTEGER NOT NULL,
                FOREIGN KEY (id_via) REFERENCES vies(id_via) ON DELETE CASCADE
            )
        """);

        // =========================
        // REGISTRES
        // =========================
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS registres (
                id_registre INTEGER PRIMARY KEY AUTOINCREMENT,
                id_escalador INTEGER NOT NULL,
                id_via INTEGER NOT NULL,
                data_ascensio TEXT,
                estil TEXT,
                FOREIGN KEY (id_escalador) REFERENCES escalador(id_escalador) ON DELETE CASCADE,
                FOREIGN KEY (id_via) REFERENCES vies(id_via) ON DELETE CASCADE
            )
        """);
    }

    // =========================
    // DADES INICIALS (SEEDS)
    // =========================
    private static void inicialitzarDadesSeed(Statement stmt) throws SQLException {

        // Comprovar si ja hi ha dades
        var rs = stmt.executeQuery("SELECT COUNT(*) FROM escoles");
        if (rs.next() && rs.getInt(1) > 0) return;
        rs.close();

        // --- POBLACIONS ---
        stmt.execute("INSERT OR IGNORE INTO poblacions (nom) VALUES ('Montserrat')");
        stmt.execute("INSERT OR IGNORE INTO poblacions (nom) VALUES ('Collbató')");
        stmt.execute("INSERT OR IGNORE INTO poblacions (nom) VALUES ('Ordino')");

        // --- ESCOLES ---
        stmt.execute("""
            INSERT OR IGNORE INTO escoles (nom, aproximacio, popularitat, restriccions)
            VALUES ('Montserrat', 'Agafar la cremallera fins a la muntanya', 'Alta', 'Veda ocells: 1 febrer - 30 juny')
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO escoles (nom, aproximacio, popularitat, restriccions)
            VALUES ('Pedra Blanca', 'Carretera B-224 sortida km 12, pista forestal 2km', 'Mitjana', 'Cap')
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO escoles (nom, aproximacio, popularitat, restriccions)
            VALUES ('Coll de Nargo', 'N-260 fins a Coll de Nargo, sender marcat', 'Baixa', 'Cap')
        """);

        // --- SECTORS ---
        stmt.execute("""
            INSERT OR IGNORE INTO sectors (nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola)
            VALUES ('Vinya Nova', 41.5932, 1.8318, '10 min a peu des del monestir', 'Alta', 'Cap', 1)
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO sectors (nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola)
            VALUES ('Collbató', 41.5710, 1.8100, '15 min des del poble', 'Mitjana', 'Cap', 1)
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO sectors (nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola)
            VALUES ('Sector Nord', 41.5800, 1.8200, '20 min a peu des del parking', 'Baixa', 'Cap', 2)
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO sectors (nom, latitud, longitud, aproximacio, popularitat, restriccions, id_escola)
            VALUES ('Gel del Nargo', 42.1900, 1.1350, 'Pista forestal fins al salt', 'Baixa', 'Cap', 3)
        """);

        // --- ESCALADORS ---
        stmt.execute("""
            INSERT OR IGNORE INTO escalador (nom, alias, edat, nivell_max, estil_preferit)
            VALUES ('Jordi Puigdomènech', 'JordiP', 35, '8a', 'Esportiva')
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO escalador (nom, alias, edat, nivell_max, estil_preferit)
            VALUES ('Miquel Ferrando', 'MiquelF', 42, '7c+', 'Clàssica')
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO escalador (nom, alias, edat, nivell_max, estil_preferit)
            VALUES ('Anna Soler', 'AnnaS', 28, '8a', 'Esportiva')
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO escalador (nom, alias, edat, nivell_max, estil_preferit)
            VALUES ('Marc Ribas', 'MarcR', 31, '7b', 'Gel')
        """);

        // --- VIES ESPORTIVES (sector 1 - Vinya Nova) ---
        stmt.execute("""
            INSERT OR IGNORE INTO vies (nom, grau, orientacio, estat, tipus, ancoratges, tipus_roca, id_creador, id_sector, id_escola, restriccions)
            VALUES ('La Directa', '7a', 'S', 'Apte', 'Esportiva', 'Parabolts', 'Conglomerat', 1, 1, 1, 'Cap')
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO vies_esportiva (id_via, llargada)
            VALUES (last_insert_rowid(), 18)
        """);

        stmt.execute("""
            INSERT OR IGNORE INTO vies (nom, grau, orientacio, estat, tipus, ancoratges, tipus_roca, id_creador, id_sector, id_escola, restriccions)
            VALUES ('El Diedre', '6b', 'SE', 'Apte', 'Esportiva', 'Spits', 'Conglomerat', 1, 1, 1, 'Cap')
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO vies_esportiva (id_via, llargada)
            VALUES (last_insert_rowid(), 12)
        """);

        stmt.execute("""
            INSERT OR IGNORE INTO vies (nom, grau, orientacio, estat, data_estat, tipus, ancoratges, tipus_roca, id_creador, id_sector, id_escola, restriccions)
            VALUES ('La Tecnica', '7b+', 'S', 'Construccio', '2026-07-01', 'Esportiva', 'Quimics', 'Conglomerat', 2, 1, 1, 'En rehabilitació')
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO vies_esportiva (id_via, llargada)
            VALUES (last_insert_rowid(), 25)
        """);

        // --- VIA ESPORTIVA (sector 2 - Collbató) ---
        stmt.execute("""
            INSERT OR IGNORE INTO vies (nom, grau, orientacio, estat, tipus, ancoratges, tipus_roca, id_creador, id_sector, id_escola, restriccions)
            VALUES ('El Xamineu', '8a', 'E', 'Apte', 'Esportiva', 'Spits', 'Calcaria', 3, 2, 1, 'Cap')
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO vies_esportiva (id_via, llargada)
            VALUES (last_insert_rowid(), 22)
        """);

        // --- VIA CLÀSSICA (sector 2 - Collbató) ---
        stmt.execute("""
            INSERT OR IGNORE INTO vies (nom, grau, orientacio, estat, tipus, ancoratges, tipus_roca, id_creador, id_sector, id_escola, restriccions)
            VALUES ('Ruta dels Gegants', '6c', 'N', 'Apte', 'Clàssica', 'Friends', 'Granit', 2, 2, 1, 'Cap')
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO vies_classica (id_via, ancoratges_permesos)
            VALUES (last_insert_rowid(), 'Friends,Tascons')
        """);
        // Trams de la via clàssica
        stmt.execute("""
            INSERT OR IGNORE INTO trams (num_llarg, llarg, grau_dificultat, id_via)
            VALUES (1, 25, '6b', (SELECT id_via FROM vies WHERE nom = 'Ruta dels Gegants' LIMIT 1))
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO trams (num_llarg, llarg, grau_dificultat, id_via)
            VALUES (2, 28, '6c', (SELECT id_via FROM vies WHERE nom = 'Ruta dels Gegants' LIMIT 1))
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO trams (num_llarg, llarg, grau_dificultat, id_via)
            VALUES (3, 20, '6a', (SELECT id_via FROM vies WHERE nom = 'Ruta dels Gegants' LIMIT 1))
        """);

        // --- VIA GEL (sector 4 - Gel del Nargo) ---
        stmt.execute("""
            INSERT OR IGNORE INTO vies (nom, grau, orientacio, estat, tipus, ancoratges, tipus_roca, id_creador, id_sector, id_escola, restriccions)
            VALUES ('Cascada Blava', '5', 'N', 'Apte', 'Gel', 'Pitons', 'Arenisca', 4, 4, 3, 'Tancada fora de temporada (juny-setembre)')
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO vies_gel (id_via)
            VALUES (last_insert_rowid())
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO trams (num_llarg, llarg, grau_dificultat, id_via)
            VALUES (1, 20, '5', (SELECT id_via FROM vies WHERE nom = 'Cascada Blava' LIMIT 1))
        """);
        stmt.execute("""
            INSERT OR IGNORE INTO trams (num_llarg, llarg, grau_dificultat, id_via)
            VALUES (2, 15, '5+', (SELECT id_via FROM vies WHERE nom = 'Cascada Blava' LIMIT 1))
        """);
    }
}
