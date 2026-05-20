package dev.jhoserbriceno.latinoamerica.model.service;

import dev.jhoserbriceno.latinoamerica.model.constant.NewsStatus;
import dev.jhoserbriceno.latinoamerica.model.dto.NewsDTO;
import dev.jhoserbriceno.latinoamerica.model.entity.News;
import dev.jhoserbriceno.latinoamerica.model.repository.NewsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NewsService {

    private final NewsRepository repository;

    public NewsService(NewsRepository repository) {
        this.repository = repository;
    }

    // Retorna TODAS las noticias (publicadas y borradores) ordenadas de más reciente a más antigua.
    public List<News> findAll() {
        return repository.findAllByOrderByPublicationDateDesc();
    }

    // Retorna solo las noticias con estado PUBLISHED, ordenadas de más reciente a más antigua.
    // Lo usa el home público para mostrar únicamente lo que el admin ha publicado.
    // Se filtra por NEWS_STATUS_PUBLISHED para que los borradores no sean visibles al público.
    public List<News> findPublished() {
        return repository.findByStateOrderByPublicationDateDesc(NewsStatus.NEWS_STATUS_PUBLISHED);
    }

    // Busca una noticia por su ID. Si no existe, lanza EntityNotFoundException
    // en lugar de retornar null, lo que permite manejar el error limpiamente en el controlador.
    public News findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + id + " not found"));
    }

    // Recibe un DTO desde el formulario de creación, lo convierte a entidad y lo guarda.
    // El @PrePersist de la entidad News asigna automáticamente publicationDate y updatedAt.
    public void create(NewsDTO dto) {
        repository.save(dto.toEntity());
    }

    // Busca la noticia existente por ID, reemplaza sus campos con los datos del DTO
    // y guarda los cambios. El @PreUpdate de la entidad actualiza updatedAt automáticamente.
    // Si no existe la noticia, lanza EntityNotFoundException.
    public void update(Long id, NewsDTO newsDTO) {
        News news = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + id + " not found"));
        news.replaceFieldsWith(newsDTO);
        repository.save(news);
    }

    // Busca la noticia por ID y la elimina de la base de datos.
    // Si no existe, lanza EntityNotFoundException antes de intentar borrar.
    public void delete(Long id) {
        News n = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + id + " not found"));
        repository.delete(n);
    }
}