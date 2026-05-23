package dev.jhoserbriceno.latinoamerica.model.entity;

import dev.jhoserbriceno.latinoamerica.model.constant.Purpose;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_request")
public class ContactRequest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Purpose purpose;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Se ejecuta automáticamente antes de guardar
    @PrePersist
    public void prePersist() { this.createdAt = LocalDateTime.now(); }

    public ContactRequest() {}

    public Long getContactId() { return contactId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Purpose getPurpose() { return purpose; }
    public void setPurpose(Purpose purpose) { this.purpose = purpose; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    // Eliminé el setter de createdAt (no debe modificarse manualmente)
}