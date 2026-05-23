package dev.jhoserbriceno.latinoamerica.model.entity;

import dev.jhoserbriceno.latinoamerica.model.constant.NewsStatus;
import dev.jhoserbriceno.latinoamerica.model.dto.NewsDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")

public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long newsId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 500)
    private String summary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url", nullable = false, length = 600)
    private String imageUrl;

    @Column(nullable = false, length = 100)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NewsStatus state;

    @Column(name = "publication_date", nullable = false, updatable = false)
    private LocalDateTime publicationDate;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Asignamos el PublicationDate y el updatedAt al momento de primera creación
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.publicationDate == null) {
            this.publicationDate = now;
        }
        this.updatedAt = now;
    }

    // Actualizamos el updatedAt automáticamente una vez modificada la entidad
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public News() {}

    public Long getNewsId() { return newsId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public NewsStatus getState() { return state; }
    public void setState(NewsStatus state) { this.state = state; }

    public LocalDateTime getPublicationDate() { return publicationDate; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void replaceFieldsWith(NewsDTO newsDTO) {
        setTitle(newsDTO.title());
        setSummary(newsDTO.summary());
        setContent(newsDTO.content());
        setImageUrl(newsDTO.imageUrl());
        setAuthor(newsDTO.author());
        setState(newsDTO.status());
    }
}