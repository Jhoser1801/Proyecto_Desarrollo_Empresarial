package dev.jhoserbriceno.latinoamerica.model.service;

import dev.jhoserbriceno.latinoamerica.model.dto.CreateUserDTO;
import dev.jhoserbriceno.latinoamerica.model.dto.UpdateUserDTO;
import dev.jhoserbriceno.latinoamerica.model.entity.Admin;
import dev.jhoserbriceno.latinoamerica.model.repository.AdminRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserAdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAdminService(AdminRepository adminRepository,
                            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    @Transactional
    public Admin create(CreateUserDTO dto) {
        if (adminRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un administrador con el correo: " + dto.email());
        }
        Admin admin = new Admin(
                dto.name(),
                dto.email(),
                passwordEncoder.encode(dto.password())
        );
        return adminRepository.save(admin);
    }

    @Transactional
    public Admin update(Long id, UpdateUserDTO dto) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrador con id " + id + " no encontrado."));

        adminRepository.findByEmail(dto.email()).ifPresent(existing -> {
            if (!existing.getAdminId().equals(id)) {
                throw new IllegalArgumentException("El correo " + dto.email() + " ya está en uso por otro administrador.");
            }
        });

        admin.setName(dto.name());
        admin.setEmail(dto.email());

        if (dto.password() != null && !dto.password().isBlank()) {
            admin.setPassword(passwordEncoder.encode(dto.password()));
        }

        return adminRepository.save(admin);
    }

    // Se añade existsById() porque deleteById() no lanza excepción si el ID no existe.
    // Primero se valida existencia, luego la regla de negocio del último admin.
    @Transactional
    public void delete(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new EntityNotFoundException("Administrador con id " + id + " no encontrado.");
        }
        if (adminRepository.count() <= 1) {
            throw new IllegalStateException("No se puede eliminar el único administrador del sistema.");
        }
        adminRepository.deleteById(id);
    }
}