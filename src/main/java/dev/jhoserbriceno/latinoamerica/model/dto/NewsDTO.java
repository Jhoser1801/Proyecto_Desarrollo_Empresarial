package dev.jhoserbriceno.latinoamerica.model.dto;

import dev.jhoserbriceno.latinoamerica.model.constant.NewsStatus;
import dev.jhoserbriceno.latinoamerica.model.entity.News;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewsDTO (

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 200, message = "El título no puede superar los 200 caracteres")
        String title,

        @NotBlank(message = "El resumen es obligatorio")
        @Size(max = 500, message = "El resumen no puede superar los 500 caracteres")
        String summary,

        @NotBlank(message = "El contenido es obligatorio")
        @Size(max = 20000, message = "El contenido no puede superar los 20000 caracteres")
        String content,

        @NotBlank(message = "La URL es obligatoria")
        @Size(max = 300, message = "La URL no puede superar los 300 caracteres")
        @Pattern(
                regexp = "^(http|https)://.*$",
                message = "Debe ser una URL válida (http o https)"
        )
        String imageUrl,

        @NotBlank(message = "El autor es obligatorio")
        @Size(max = 100, message = "El autor no puede superar los 100 caracteres")
        String author,

        @NotNull(message = "El estado es obligatorio")
        NewsStatus status
)

{
    public News toEntity() {
        News n = new News();
        n.setTitle(this.title);
        n.setSummary(this.summary);
        n.setContent(this.content);
        n.setImageUrl(this.imageUrl);
        n.setAuthor(this.author);
        n.setState(this.status);
        return n;
    }
  /*  // función para aplicar los cambios para una entidad existente que es la news
    public void applyTo(News n) {
        n.setTitle(this.title);
        n.setSummary(this.summary);
        n.setContent(this.content);
        n.setImageUrl(this.imageUrl);
        n.setAuthor(this.author);
        n.setState(this.status);
    }*/
}