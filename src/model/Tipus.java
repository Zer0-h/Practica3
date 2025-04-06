package model;

/**
 * @author tonitorres
 */
public enum Tipus {
    PROPER("Parella més propera"),
    LLUNY("Parella més llunyana");

    private final String description;

    Tipus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
