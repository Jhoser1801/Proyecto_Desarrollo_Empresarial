package dev.jhoserbriceno.latinoamerica.model.repository;

import dev.jhoserbriceno.latinoamerica.model.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
    List<Testimonial> findAllByOrderByCreatedAtDesc();
    //Retorna todos los testimonios del más reciente al más antiguoList<Testimonial> findAllByOrderByCreatedAtDesc();
}
