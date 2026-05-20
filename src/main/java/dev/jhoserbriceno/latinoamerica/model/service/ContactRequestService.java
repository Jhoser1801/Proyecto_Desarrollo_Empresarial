package dev.jhoserbriceno.latinoamerica.model.service;

import dev.jhoserbriceno.latinoamerica.model.constant.Purpose;
import dev.jhoserbriceno.latinoamerica.model.dto.ContactRequestDTO;
import dev.jhoserbriceno.latinoamerica.model.entity.ContactRequest;
import dev.jhoserbriceno.latinoamerica.model.repository.ContactRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactRequestService {

    /* Meta fija de contactos mensuales. */
    public static final int CONTACT_GOAL = 10;

    private static final String[] MESES = {
            "Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
    };

    private final ContactRequestRepository repository;

    public ContactRequestService(ContactRequestRepository repository) {
        this.repository = repository;
    }

    public List<ContactRequest> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public List<ContactRequest> findByPurpose(Purpose purpose) {
        return repository.findByPurpose(purpose);
    }

    public Optional<ContactRequest> findById(Long id) {
        return repository.findById(id);
    }

    /* Retorna los contactos agrupados por mes (solo el año actual). */
    public List<Object[]> countContactsByMonth() {
        return repository.countContactsByMonth();
    }

    /*
      Calcula el porcentaje de avance hacia la meta mensual.
      Nunca supera el 100 %.
     */
    public int calcGoalPercent(int totalContacts) {
        if (totalContacts == 0) return 0;
        return Math.min((totalContacts * 100) / CONTACT_GOAL, 100);
    }

    /*
      Determina el mes con más solicitudes a partir de los datos mensuales.
      Devuelve "Sin datos" si la lista está vacía.
     */
    public String findBestMonth(List<Object[]> monthlyContacts) {
        String bestMonth = "Sin datos";
        long bestCount = 0;

        for (Object[] row : monthlyContacts) {
            int mes = ((Number) row[0]).intValue();
            long cantidad = ((Number) row[1]).longValue();
            if (cantidad > bestCount) {
                bestCount = cantidad;
                bestMonth = MESES[mes - 1];
            }
        }
        return bestMonth;
    }

    /*
      Retorna la cantidad de solicitudes del mes con mayor actividad.
     */
    public long findBestCount(List<Object[]> monthlyContacts) {
        long bestCount = 0;
        for (Object[] row : monthlyContacts) {
            long cantidad = ((Number) row[1]).longValue();
            if (cantidad > bestCount) {
                bestCount = cantidad;
            }
        }
        return bestCount;
    }

    /* Crea un nuevo contacto a partir del DTO recibido del formulario. */
    public void create(ContactRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser null");
        }
        ContactRequest entity = dto.toEntity();
        repository.save(entity);
    }

    public void delete(Long id) {
        ContactRequest cr = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact request with id " + id + " not found"));
        repository.delete(cr);
    }
}