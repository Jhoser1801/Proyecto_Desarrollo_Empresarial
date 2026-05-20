package dev.jhoserbriceno.latinoamerica.model.constant;

public enum NewsStatus {
    NEWS_STATUS_DRAFT("Borrador"),
    NEWS_STATUS_PUBLISHED("Publicado");

    private final String visualName;

    NewsStatus(String visualName) {
        this.visualName = visualName;
    }

    public String getVisualName() {
        return visualName;
    }
}
