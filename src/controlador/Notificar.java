package controlador;

/**
 * Interfície Notificar: Defineix el comportament que han de seguir les classes
 * que volen rebre notificacions d'esdeveniments.
 *
 * @author tonitorres
 */
public interface Notificar {

    /**
     * Mètode per rebre notificacions d'esdeveniments.
     *
     * @param n El tipus de notificació a gestionar.
     */
    void notificar(Notificacio n);
}
