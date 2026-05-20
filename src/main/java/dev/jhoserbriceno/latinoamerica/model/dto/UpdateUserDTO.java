package dev.jhoserbriceno.latinoamerica.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
        String name,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Debe ser un correo electrónico válido")
        @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
        String email,

        // Opcional al editar: vacío = no cambiar contraseña
        String password
) {}