package dev.jhoserbriceno.latinoamerica.model.repository;

import dev.jhoserbriceno.latinoamerica.model.constant.NewsStatus;
import dev.jhoserbriceno.latinoamerica.model.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    // Busca todas las noticias que tengan un estado específico (ej: PUBLISHED)
    // y las ordena por fecha de publicación de más reciente a más antigua (Desc).
    // Spring Data JPA genera el SQL automáticamente a partir del nombre del método:
    // SELECT * FROM news WHERE state = ? ORDER BY publication_date DESC
    // Se usa en el home público para mostrar solo las noticias publicadas.
    // El "Desc" es importante: sin él, PostgreSQL puede ordenar de forma inconsistente.
    List<News> findByStateOrderByPublicationDateDesc(NewsStatus state);

    // Busca TODAS las noticias sin filtrar por estado y las ordena de más reciente a más antigua.
    // Se usa en el panel de administración donde el admin necesita ver tanto
    // las publicadas como los borradores.
    // SELECT * FROM news ORDER BY publication_date DESC
    List<News> findAllByOrderByPublicationDateDesc();
}
