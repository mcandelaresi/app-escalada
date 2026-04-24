package dao;

import java.util.List;

/**
 * Interfície genèrica DAO (Data Access Object).
 *
 * Aquesta interfície defineix les operacions bàsiques (CRUD)
 * que qualsevol DAO ha d'implementar per accedir a la base de dades.
 *
 * @param <T> tipus de l'objecte (Via, Escalador, Sector...)
 * @param <K> tipus de la clau primària (ex: Integer per id)
 */
public interface dao<T, K> {

    /**
     * Insereix un nou objecte a la base de dades.
     *
     * @param t objecte a inserir
     */
    void insert(T t);

    /**
     * Retorna un objecte de la base de dades a partir del seu ID.
     *
     * @param id identificador de l'objecte
     * @return objecte trobat o null si no existeix
     */
    T findById(K id);

    /**
     * Retorna tots els objectes de la taula.
     *
     * @return llista d'objectes
     */
    List<T> findAll();

    /**
     * Actualitza la informació d'un objecte existent a la base de dades.
     *
     * @param t objecte amb les dades actualitzades
     */
    void update(T t);

    /**
     * Elimina un objecte de la base de dades a partir del seu ID.
     *
     * @param id identificador de l'objecte a eliminar
     */
    void delete(K id);
}