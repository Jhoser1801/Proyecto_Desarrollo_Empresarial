package dev.jhoserbriceno.latinoamerica.controller;

import dev.jhoserbriceno.latinoamerica.model.dto.CreateUserDTO;
import dev.jhoserbriceno.latinoamerica.model.dto.UpdateUserDTO;
import dev.jhoserbriceno.latinoamerica.model.entity.Admin;
import dev.jhoserbriceno.latinoamerica.model.service.UserAdminService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserAdminService userAdminService;

    public UserAdminController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userAdminService.findAll());
        return "admin/users/list";
    }

    // Prepara el formulario de creación enviando un DTO vacío al modelo.
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("userRequest", new CreateUserDTO("", "", ""));
        return "admin/users/create";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("userRequest") CreateUserDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/users/create";
        }
        try {
            userAdminService.create(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Administrador creado exitosamente.");
            return "redirect:/admin/users";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/users/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Admin> result = userAdminService.findById(id);

        if (result.isEmpty()) {
            return "error/not-found";
        }

        Admin admin = result.get();
        model.addAttribute("user", admin);
        model.addAttribute("userRequest", new UpdateUserDTO(admin.getName(), admin.getEmail(), ""));
        return "admin/users/update";
    }

    @PutMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("userRequest") UpdateUserDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (dto.password() != null && !dto.password().isBlank() && dto.password().length() < 8) {
            result.rejectValue("password", "size", "La contraseña debe tener al menos 8 caracteres");
        }

        if (result.hasErrors()) {
            userAdminService.findById(id).ifPresent(a -> model.addAttribute("user", a));
            return "admin/users/update";
        }

        try {
            userAdminService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Administrador actualizado exitosamente.");
            return "redirect:/admin/users";
        } catch (EntityNotFoundException e) {
            return "error/not-found";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            userAdminService.findById(id).ifPresent(a -> model.addAttribute("user", a));
            return "admin/users/update";
        }
    }

    @GetMapping("/delete/{id}")
    public String confirmDelete(@PathVariable Long id, Model model) {
        Optional<Admin> result = userAdminService.findById(id);
        if (result.isEmpty()) {
            model.addAttribute("title", "Eliminar Administrador");
            return "error/not-found";
        }
        model.addAttribute("user", result.get());
        return "admin/users/delete";
    }

    // Se añade Model y captura de EntityNotFoundException para cubrir el caso de ID inexistente.
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            userAdminService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Administrador eliminado correctamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (EntityNotFoundException e) {
            model.addAttribute("title", "Eliminar Administrador");
            return "error/not-found";
        }
        return "redirect:/admin/users";
    }
}