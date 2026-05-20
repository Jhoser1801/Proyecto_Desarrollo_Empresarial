package dev.jhoserbriceno.latinoamerica.model.dto;

import dev.jhoserbriceno.latinoamerica.model.constant.Purpose;
import dev.jhoserbriceno.latinoamerica.model.entity.ContactRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContactRequestDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Correo inválido")
        @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
        String email,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
        String phone,

        @NotNull(message = "Debe seleccionar un propósito")
        Purpose purpose

) {
    public ContactRequest toEntity() {
        ContactRequest cr = new ContactRequest();
        cr.setName(this.name);
        cr.setEmail(this.email);
        cr.setPhone(this.phone);
        cr.setPurpose(this.purpose);
        return cr;
    }
}