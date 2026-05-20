package dev.jhoserbriceno.latinoamerica.model.service;

import dev.jhoserbriceno.latinoamerica.model.dto.TestimonialDTO;
import dev.jhoserbriceno.latinoamerica.model.entity.Testimonial;
import dev.jhoserbriceno.latinoamerica.model.repository.TestimonialRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestimonialService {

    private final TestimonialRepository repository;

    public TestimonialService(TestimonialRepository repository) {
        this.repository = repository;
    }

    public List<Testimonial> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Testimonial> findById(Long id) {
        return repository.findById(id);
    }

    public void create(TestimonialDTO dto) {
        repository.save(dto.toEntity());
    }

    public void update(Long id, TestimonialDTO testimonialDTO) {
        Testimonial testimonial = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Testimonial with id " + id + " not found"));
        testimonial.replaceFieldsWith(testimonialDTO);
        repository.save(testimonial);
    }

    public void delete(Long id) {
        Testimonial t = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Testimonial with id " + id + " not found"));
        repository.delete(t);
    }
}