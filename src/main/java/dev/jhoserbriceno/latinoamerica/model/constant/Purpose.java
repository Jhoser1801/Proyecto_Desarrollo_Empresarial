package dev.jhoserbriceno.latinoamerica.model.constant;


public enum Purpose {

    PURPOSE_SERVICE("Servicio"),
    PURPOSE_EDIFICA_PROGRAM("Programa EDIFICA"),
    PURPOSE_SHOWS_AND_CONFERENCES("Shows y conferencias");

    private final String displayName;

    Purpose(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
