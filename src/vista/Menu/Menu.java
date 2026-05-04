package vista.Menu;

import dao.ConnectionDB;
import dao.sqlite.EscaladorDAO;
import dao.sqlite.EscolaDAO;
import dao.sqlite.PoblacioDAO;
import dao.sqlite.RegistreDAO;
import dao.sqlite.SectorDAO;
import dao.sqlite.ViaClassicaDAO;
import dao.sqlite.ViaDAO;
import dao.sqlite.ViaEsportivaDAO;
import dao.sqlite.ViaGelDAO;
import excepcions.Validacions;
import model.Via;
import model.enums.EstatVia;
import vista.Vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Connection;
import java.util.Scanner;

public class Menu {

    private final Scanner sc = new Scanner(System.in);
    private final ViaDAO viaDAO;

    private final MenuEscola menuEscola;
    private final MenuSector menuSector;
    private final MenuVies menuVia;
    private final MenuEscaladors menuEscalador;
    private final MenuCerca menuCerca;

    public Menu() {
        EscolaDAO escolaDAO = new EscolaDAO();
        SectorDAO sectorDAO = new SectorDAO();
        this.viaDAO = new ViaDAO();
        ViaEsportivaDAO viaEsportivaDAO = new ViaEsportivaDAO();
        ViaClassicaDAO viaClassicaDAO = new ViaClassicaDAO();
        ViaGelDAO viaGelDAO = new ViaGelDAO();
        EscaladorDAO escaladorDAO = new EscaladorDAO();
        PoblacioDAO poblacioDAO = new PoblacioDAO();
        RegistreDAO registreDAO = new RegistreDAO();

        Connection conn = ConnectionDB.getConnection();
        if (conn != null) {
            escolaDAO.setConnection(conn);
            sectorDAO.setConnection(conn);
            poblacioDAO.setConnection(conn);
            registreDAO.setConnection(conn);
        }

        menuEscola = new MenuEscola(sc, escolaDAO);
        menuSector = new MenuSector(sc, sectorDAO, escolaDAO);
        menuVia = new MenuVies(sc, viaDAO, viaEsportivaDAO, viaClassicaDAO, viaGelDAO, escolaDAO, sectorDAO, escaladorDAO);
        menuEscalador = new MenuEscaladors(sc, escaladorDAO, viaDAO);
        menuCerca = new MenuCerca(sc, viaDAO);
    }

    public void menu() {
        int op;

        Vista.intro();

        do {
            actualitzarEstatsCaducats();
            Vista.menuPrincipal();
            op = Validacions.llegirOpcio(sc, "Opció: ", 0, 5);

            switch (op) {
                case 1 -> menuEscola.menu();
                case 2 -> menuSector.menu();
                case 3 -> menuVia.menu();
                case 4 -> menuEscalador.menu();
                case 5 -> menuCerca.menu();
                case 0 -> System.out.println("Adeu!");
            }

        } while (op != 0);
    }

    private void actualitzarEstatsCaducats() {
        for (Via via : viaDAO.findAll()) {
            if (via.getDataEstat() == null || via.getDataEstat().isBlank()) {
                continue;
            }

            if (!EstatVia.APTE.getValor().equalsIgnoreCase(via.getEstat())) {
                try {
                    LocalDate dataFi = LocalDate.parse(via.getDataEstat(), DateTimeFormatter.ISO_LOCAL_DATE);
                    if (!dataFi.isAfter(LocalDate.now())) {
                        via.setEstat(EstatVia.APTE.getValor());
                        via.setDataEstat(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                        viaDAO.update(via);
                    }
                } catch (DateTimeParseException ignored) {
                    // si la data no és correcta, no faig res i continuo
                }
            }
        }
    }
}