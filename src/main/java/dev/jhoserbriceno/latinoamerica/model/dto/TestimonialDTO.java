package dev.jhoserbriceno.latinoamerica.model.dto;

import dev.jhoserbriceno.latinoamerica.model.entity.Testimonial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TestimonialDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @NotBlank(message = "La URL del video es obligatoria")
        @Size(max = 300, message = "La URL no puede superar los 300 caracteres")
        @Pattern(
                regexp = "^(http|https)://.*$",
                message = "Debe ser una URL válida (http o https)"
        )
        String videoUrl,

        @Size(max = 300, message = "La URL no puede superar los 300 caracteres")
        @Pattern(
                regexp = "^$|^(http|https)://.*$",
                message = "Debe ser una URL válida (http o https)"
        )
        String instagramUrl,

        @Size(max = 300, message = "La URL no puede superar los 300 caracteres")
        @Pattern(
                regexp = "^$|^(http|https)://.*$",
                message = "Debe ser una URL válida (http o https)"
        )
        String facebookUrl
){
    public Testimonial toEntity() {
        Testimonial t = new Testimonial();
        t.setName(this.name);
        t.setVideoUrl(this.videoUrl);
        t.setInstagramUrl(this.instagramUrl);
        t.setFacebookUrl(this.facebookUrl);
        return t;
    }

}