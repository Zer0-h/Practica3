package model;

public enum Distribucio {
    GAUSSIAN("Gaussiana"),
    EXPONENCIAL("Exponencial"),
    UNIFORME("Uniforme");

    private final String description;

    Distribucio(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
