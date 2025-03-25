package controlador;

/**
 * Interfície que defineix el patró d'esdeveniments per a la comunicació entre
 * el Model, la Vista i el Controlador.
 *
 * Les classes que implementin aquesta interfície hauran de gestionar
 * notificacions per reaccionar als esdeveniments generats durant l'execució del
 * programa.
 *
 * @author tonitorres
 */
public interface Notificar {

    /**
     * Mètode que rep una notificació i actua en conseqüència.
     *
     * @param n Tipus de notificació que indica l'acció a realitzar
     */
    void notificar(Notificacio n);
}
