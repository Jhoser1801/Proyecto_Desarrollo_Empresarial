package dev.jhoserbriceno.latinoamerica.model.repository;

import dev.jhoserbriceno.latinoamerica.model.constant.Purpose;
import dev.jhoserbriceno.latinoamerica.model.entity.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {
    List<ContactRequest> findByPurpose(Purpose purpose);//para el filtro de finalidades

    List<ContactRequest> findAllByOrderByCreatedAtDesc();// retorna las solicitudes ordenadas
    // de más reciente a más antigua

    @Query("""
    SELECT EXTRACT(MONTH FROM c.createdAt), COUNT(c)
    FROM ContactRequest c
    WHERE EXTRACT(YEAR FROM c.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE)
    GROUP BY EXTRACT(MONTH FROM c.createdAt)
    ORDER BY EXTRACT(MONTH FROM c.createdAt)
""")
    List<Object[]> countContactsByMonth();
}
