package dev.jhoserbriceno.latinoamerica.model.entity;

import dev.jhoserbriceno.latinoamerica.model.dto.TestimonialDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "testimonial")
public class Testimonial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "testimonial_id")
    private Long testimonialId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "video_url", nullable = false, length = 300)
    private String videoUrl;

    @Column(name = "instagram_url", length = 300)
    private String instagramUrl;

    @Column(name = "facebook_url", length = 300)
    private String facebookUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Asignamos el createdAt y el updatedAt al momento de primera inserción
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }

    // Se actualiza automáticamente una vez modificada la entidad
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Testimonial() {}

    public Long getTestimonialId() { return testimonialId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getInstagramUrl() { return instagramUrl; }
    public void setInstagramUrl(String instagramUrl) { this.instagramUrl = instagramUrl; }

    public String getFacebookUrl() { return facebookUrl; }
    public void setFacebookUrl(String facebookUrl) { this.facebookUrl = facebookUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void replaceFieldsWith(TestimonialDTO testimonialDTO) {
        setName(testimonialDTO.name());
        setVideoUrl(testimonialDTO.videoUrl());
        setInstagramUrl(testimonialDTO.instagramUrl());
        setFacebookUrl(testimonialDTO.facebookUrl());
    }


    // ─────────────────────────────────────────────────────────────
    // @Transient: le dice a Hibernate que este método NO corresponde
    // a ninguna columna de la base de datos. Es un campo/método
    // calculado que solo existe en memoria Java, nunca se persiste.
    //
    // ¿Qué hace? Recibe el video_url del testimonio (que es una URL
    // completa de YouTube) y extrae solo el ID del video para
    // construir la URL de la miniatura (thumbnail) de YouTube.
    //
    // Ejemplo:
    //   videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
    //   resultado = "https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg"
    //
    // Thymeleaf llama a este método desde la vista con:
    //   th:src="${testimonial.youtubeThumbnailUrl}"
    // ─────────────────────────────────────────────────────────────
    @Transient
    public String getYoutubeThumbnailUrl() {

        // Si no hay URL de video, no hay thumbnail que calcular
        if (videoUrl == null || videoUrl.isBlank()) return null;

        // Lista de todos los formatos de URL que acepta YouTube
        String[] prefixes = {
                "https://www.youtube.com/watch?v=",  // formato estándar de escritorio
                "https://youtube.com/watch?v=",       // sin www
                "https://youtu.be/",                  // formato corto (links compartidos)
                "https://www.youtu.be/",              // formato corto con www
                "https://youtube.com/shorts/",        // videos cortos (Shorts)
                "https://www.youtube.com/shorts/",    // Shorts con www
                "https://youtube.com/embed/",         // formato embed (iframes)
                "https://www.youtube.com/embed/"      // embed con www
        };

        String id = videoUrl; // empezamos con la URL completa

        // Recorremos cada prefijo conocido para encontrar cuál aplica
        for (String prefix : prefixes) {
            if (id.contains(prefix)) {
                // Cortamos todo lo que está antes del prefijo (incluido el prefijo)
                // Lo que queda es el ID del video (más posibles parámetros extra)
                id = id.substring(id.indexOf(prefix) + prefix.length());
                break; // ya encontramos el formato, salimos del loop
            }
        }

        // Eliminamos parámetros extra que puedan venir después del ID:
        // Ejemplo: dQw4w9WgXcQ?si=abc123  →  dQw4w9WgXcQ
        if (id.contains("?")) id = id.substring(0, id.indexOf("?"));

        // Ejemplo: dQw4w9WgXcQ&t=30s  →  dQw4w9WgXcQ
        if (id.contains("&")) id = id.substring(0, id.indexOf("&"));

        // Ejemplo: dQw4w9WgXcQ/algo  →  dQw4w9WgXcQ
        if (id.contains("/")) id = id.substring(0, id.indexOf("/"));

        // Construimos y retornamos la URL pública del thumbnail de YouTube
        // hqdefault.jpg = calidad alta (480x360 px)
        return "https://img.youtube.com/vi/" + id + "/hqdefault.jpg";
    }


}