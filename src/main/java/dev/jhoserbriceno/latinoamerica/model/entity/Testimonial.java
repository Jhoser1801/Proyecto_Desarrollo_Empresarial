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

    @Transient
    public String getYoutubeThumbnailUrl() {
        if (videoUrl == null || videoUrl.isBlank()) return null;
        String[] prefixes = {
                "https://www.youtube.com/watch?v=",
                "https://youtube.com/watch?v=",
                "https://youtu.be/",
                "https://www.youtu.be/",
                "https://youtube.com/shorts/",
                "https://www.youtube.com/shorts/",
                "https://youtube.com/embed/",
                "https://www.youtube.com/embed/"
        };
        String id = videoUrl;
        for (String prefix : prefixes) {
            if (id.contains(prefix)) {
                id = id.substring(id.indexOf(prefix) + prefix.length());
                break;
            }
        }
        // Cortar parámetros extra como ?si=... o &t=30s
        if (id.contains("?")) id = id.substring(0, id.indexOf("?"));
        if (id.contains("&")) id = id.substring(0, id.indexOf("&"));
        if (id.contains("/")) id = id.substring(0, id.indexOf("/"));
        return "https://img.youtube.com/vi/" + id + "/hqdefault.jpg";
    }
}