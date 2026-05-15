# Aplicació de Gestió d'Escalada — Pillam Ltd. Co.

Projecte de persistència de dades (JDBC + SQLite) per a la gestió de vies d'escalada, escoles, sectors i escaladors. Desenvolupat com a pràctica del mòdul **MS005 - Persistència de dades**.

---

## Descripció general

L'aplicació permet gestionar tot l'ecosistema d'escalada de l'empresa **Pillam Ltd. Co.** a través d'una interfície de menú de consola. El sistema cobreix tres modalitats de via (Esportiva, Clàssica i Gel), organitzades en Escoles i Sectors, amb gestió d'escaladors i un historial d'ascensions.

---

## Estructura del projecte

```
src/
├── Main.java                          # Punt d'entrada
├── dao/
│   ├── ConnectionDB.java              # Connexió SQLite + creació de taules + seeds
│   ├── dao.java                       # Interfície genèrica CRUD
│   └── sqlite/
│       ├── EscolaDAO.java
│       ├── SectorDAO.java
│       ├── ViaDAO.java
│       ├── ViaEsportivaDAO.java
│       ├── ViaClassicaDAO.java
│       ├── ViaGelDAO.java
│       ├── TramDAO.java
│       ├── EscaladorDAO.java
│       ├── RegistreDAO.java
│       └── PoblacioDAO.java
├── model/
│   ├── Via.java                       # Classe abstracta base
│   ├── ViaEsportiva.java
│   ├── ViaClassica.java
│   ├── ViaGel.java
│   ├── Escola.java
│   ├── Sector.java
│   ├── Escalador.java
│   ├── Tram.java
│   ├── Registre.java
│   ├── Poblacio.java
│   └── enums/
│       ├── EstatVia.java
│       ├── Estil.java
│       ├── GrauDificultat.java
│       ├── Orientacio.java
│       ├── Popularitat.java
│       ├── TipusAncoratge.java
│       ├── TipusRoca.java
│       └── TipusVia.java
├── controlador/
│   ├── ViaController.java
│   ├── EscolaController.java
│   ├── SectorController.java
│   ├── EscaladorController.java
│   └── CercaController.java
├── vista/
│   ├── Vista.java
│   └── Menu/
│       ├── Menu.java
│       ├── MenuVies.java
│       ├── MenuEscola.java
│       ├── MenuSector.java
│       ├── MenuEscaladors.java
│       └── MenuCerca.java
├── helpers/
│   ├── AuxVia.java
│   ├── AuxSector.java
│   ├── AuxEscola.java
│   ├── AuxEscalador.java
│   └── AuxCerca.java
└── excepcions/
    ├── EntradaNoValidaException.java
    └── Validacions.java

bdd/
├── escalada.db                        # Base de dades SQLite 
├── eer-escaladaMija.png               # Diagrama EER exportat
└── eer-escaladaMija.dia               # Fitxer editable del diagrama EER

lib/
└── sqlite-jdbc-3.53.0.0.jar
```

---

## Arquitectura i patrons de disseny

### Patró MVC
L'aplicació segueix una arquitectura **Model-Vista-Controlador** clara:
- **Model**: classes `model/` representen les entitats del domini.
- **Vista**: classes `vista/Menu/` s'encarreguen únicament de mostrar menús i llegir entrades de l'usuari.
- **Controlador**: classes `controlador/` contenen tota la lògica de negoci i coordinen model i DAO.

### Patró DAO (Data Access Object)
Cada entitat té el seu propi DAO que implementa la interfície genèrica `dao<T, K>`:

```java
public interface dao<T, K> {
    void insert(T t);
    T findById(K id);
    List<T> findAll();
    void update(T t);
    void delete(K id);
}
```

Això desacobla completament la lògica de negoci de la capa de persistència. Si en un futur es vol canviar de SQLite a MySQL, només caldria reimplementar els DAOs sense tocar controllers ni model.

### Herència i polimorfisme
`Via` és una **classe abstracta** de la qual hereten `ViaEsportiva`, `ViaClassica` i `ViaGel`. Cada subtipus afegeix els seus atributs específics:
- `ViaEsportiva`: llargada (5-30m)
- `ViaClassica`: llista de `Tram`, ancoratges permesos, llargada total > 50m
- `ViaGel`: llista de `Tram` (sense restricció de llargada mínima total)

El `ViaDAO` reconstrueix el subtipus correcte en llegir de la BD gràcies a un `switch` sobre el camp `tipus`.

### Enums per a valors tancats
Tots els valors amb un conjunt fix de possibilitats estan modelats com a enums (`GrauDificultat`, `TipusAncoratge`, `EstatVia`, etc.), cosa que evita errors de string i facilita la validació.

---

## Base de dades

La BD es crea automàticament a `bdd/escalada.db` en el primer arrencada. `ConnectionDB` gestiona:
1. Creació de totes les taules (si no existeixen)
2. Activació de les claus foranes (`PRAGMA foreign_keys = ON`)
3. Inserció de **dades seed** per poder provar l'aplicació des del primer moment

### Esquema de taules principals

| Taula | Descripció |
|---|---|
| `escoles` | Escoles d'escalada (nom únic) |
| `sectors` | Sectors dins d'una escola (nom únic per escola) |
| `vies` | Taula base de totes les vies |
| `vies_esportiva` | Atributs específics de la via esportiva (llargada) |
| `vies_classica` | Atributs específics de la via clàssica (ancoratges permesos) |
| `vies_gel` | Taula de relació per a vies de gel |
| `trams` | Llargs de vies clàssiques i de gel |
| `escalador` | Escaladors registrats |
| `registres` | Historial d'ascensions per escalador |
| `poblacions` | Poblacions on hi ha escoles |

### Regles d'integritat implementades
- `UNIQUE(nom)` a `escoles` — cap escola pot tenir el mateix nom
- `UNIQUE(nom, id_escola)` a `sectors` — nom únic dins d'una escola
- `UNIQUE(nom, id_escola)` a `vies` — nom únic dins d'una escola
- `ON DELETE CASCADE` a sectors, vies i trams — eliminar una escola elimina tot en cascada
- `CHECK` en graus, estats, orientacions, ancoratges i popularitat

---

## Dades seed (precàrregades)

En iniciar l'aplicació per primera vegada es carreguen les dades següents:

**Escoles**: Montserrat, Siurana, Camarasa, Margalef

**Sectors**: La Marana, Sector Comercial (Montserrat), Montsant (Siurana), Ribera (Camarasa), Pared Este (Margalef)

**Escaladors**:
| Nom | Àlies | Nivell màx |
|---|---|---|
| Joan Miquel Garcia | JM_Escalada | 7a+ |
| Maria Bonet Pujol | MariaB | 6c+ |
| Pere Rovira Martinez | PereRM | 7b |
| Anna Puig Lopez | AnnaP | 6b+ |
| Jaume Costa Vidal | JaumeCV | 6a |

**Vies**:
| Nom | Tipus | Grau | Escola |
|---|---|---|---|
| La Aguja | Esportiva | 6a | Montserrat |
| El Abuelo | Esportiva | 6c+ | Montserrat |
| Rencor | Esportiva | 7a | Siurana |
| Repens Original | Clàssica | 5 | Montserrat |
| Cascada de Hielo | Gel | 4+ | Siurana |
| Via de la Pedra | Esportiva | 7b | Margalef |

---

## Funcionalitats implementades

### CRUD complet per a cada entitat
Cada mòdul (Escoles, Sectors, Vies, Escaladors) ofereix:
- **Crear** — amb validació d'entrades i unicitat
- **Modificar** — amb camps opcionals (deixar en blanc per mantenir el valor)
- **Veure un** — per ID, amb detalls complets
- **Llistar tots** — format tabular
- **Eliminar** — amb confirmació i CASCADE a la BD

### Cerques especials (les 8 de l'enunciat)

| # | Cerca | Implementació |
|---|---|---|
| 1 | Vies disponibles d'una escola | Filtra `estat = 'Apte'` per `id_escola` |
| 2 | Vies per rang de dificultat | Índex ordinal sobre l'array de graus |
| 3 | Vies per estat | Filtre directe per estat |
| 4 | Escoles amb restriccions actives | Detecta restriccions no buides ni "Cap" |
| 5 | Sectors amb més de X vies disponibles | Compta vies Apte per sector |
| 6 | Escaladors amb el mateix nivell màxim | Compara `nivell_max` d'un escalador de referència |
| 7 | Vies que han tornat a Apte recentment | Vies Apte amb `data_estat` dins dels últims 30 dies |
| 8 | Vies més llargues d'una escola (top N) | Ordena per llargada total descendent |

### Gestió automàtica d'estats caducats
Cada vegada que s'obre el menú principal, el sistema executa `actualitzarEstatsCaducats()`: comprova totes les vies en estat `Construccio` o `Tancada` i, si la `data_estat` ja ha passat, les torna automàticament a `Apte`.

### Restriccions de tipus d'ancoratge per via
- **Esportiva**: Spits, Parabolts, Químics
- **Clàssica**: Friends, Tascons, Bagues, Pitons, Tricams, BigBros, Spits, Parabolts, Químics
- **Gel**: Friends, Tascons, Bagues, Pitons, Tricams, BigBros

### Restriccions de grau per modalitat
- **Esportiva**: de 4 a 9c+
- **Clàssica**: de 4 a 8b (màxim del sistema de graus clàssics)
- **Gel**: de 4 a 8b (igual que clàssica)

### Restriccions de sectors
Un sector no pot combinar vies de Gel amb Clàssica o Esportiva. Aquesta regla s'aplica tant al model (`Sector.potAfegirVia()`) com implícitament en la creació de vies.

### Registre d'ascensions (Historial)
Cada escalador té un historial accessible des del seu menú. Es pot registrar una nova ascensió indicant la via, la data i l'estil.

---

## Validació d'entrada

La classe `Validacions` centralitza tota la lectura de dades per consola:
- `llegirOpcio(min, max)` — enter en rang, reintenta si no és vàlid
- `llegirTextNoBuit()` — no accepta cadenes buides
- `llegirTextOpcional()` — permet deixar buit per mantenir el valor actual
- `llegirEnterNoNegatiu()` — enter ≥ 0
- `llegirDouble()` — decimal per coordenades

Totes llancen `EntradaNoValidaException` en cas d'error i mostren un missatge clar a l'usuari sense que el programa peti.

---

## Requisits tècnics

- **Java 21** (o superior, es fan servir switch expressions i text blocks)
- **SQLite JDBC 3.53.0.0** (inclòs a `lib/`)
- No requereix instal·lació de cap base de dades externa — SQLite és autocontingut

### Compilació i execució

```bash
# Compilar
javac -cp "lib/sqlite-jdbc-3.53.0.0.jar" -d out/production/app-escalada $(find src -name "*.java")

# Executar
java -cp "out/production/app-escalada:lib/sqlite-jdbc-3.53.0.0.jar" Main
```

En Windows, substituir `:` per `;` al classpath.

La BD s'autogenera a `bdd/escalada.db` en el primer arrencada. Per reiniciar les dades, esborrar el fitxer i tornar a executar.

---

## Decisions de disseny justificades

**Per què la connexió és estàtica (`ConnectionDB.getConnection()`)?**
Cada operació CRUD obre i tanca la seva pròpia connexió. Amb SQLite en mode fitxer local, el cost de connexió és negligible i evita problemes de concurrència i connexions obertes sense usar. Un pool de connexions seria excessiu per a una aplicació de consola monousuari.

**Per què `Via` és abstracta i no una interfície?**
Les tres modalitats comparteixen molts atributs comuns (nom, grau, estat, orientació, etc.). Usar una classe abstracta evita duplicar tot aquest codi. Una interfície s'hauria d'usar si les classes no compartissin implementació.

**Per què hi ha `vies_esportiva`, `vies_classica` i `vies_gel` separades?**
Segueix el patró de taula per subtipus (*Table Per Subtype*). La taula `vies` té els atributs comuns; les taules específiques afegeixen els atributs propis de cada modalitat. Alternativa descartada: una sola taula amb moltes columnes nullables (antipatró).

**Per què els enums no s'usen directament als DAO?**
Els enums s'usen per validar i normalitzar, però la BD guarda strings. Això permet consultar la BD directament (per exemple amb `sqlite3`) sense necessitat de decodificar valors numèrics.

**Per què `Escola` no té camp `num_vies`?**
El nombre de vies és un valor derivat que es calcula a partir dels sectors i les seves vies. Guardar-lo a la BD introduiria redundància i possibles inconsistències. S'implementa com a mètode calculat `getNumVies()`.

---

## Autor

Candelaresi Ruypérez, Christopher Mijael
MS005 - Persistència de dades — Curs 2025/2026
