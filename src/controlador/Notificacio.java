package controlador;

/**
 * Enumeració que defineix els diferents tipus de notificacions utilitzades
 * en el patró d'esdeveniments per comunicar canvis entre el Model, la Vista i
 * el Controlador.
 *
 * @author tonitorres
 */
public enum Notificacio {

    ARRANCAR, // Indica que el procés de col·locació de trominos ha de començar.
    ATURAR, // Indica que el procés ha de ser aturat.
    PINTAR, // Indica que la vista ha de redibuixar el tauler.
    FINALITZA, // Indica que el procés ha finalitzat correctament.
    SELECCIONA_FORAT // Indica que l'usuari ha seleccionat un forat en el tauler.
}
