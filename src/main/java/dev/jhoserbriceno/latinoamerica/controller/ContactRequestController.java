package dev.jhoserbriceno.latinoamerica.controller;

import dev.jhoserbriceno.latinoamerica.model.constant.Purpose;
import dev.jhoserbriceno.latinoamerica.model.entity.ContactRequest;
import dev.jhoserbriceno.latinoamerica.model.service.ContactRequestService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // <-- AÑADIDO

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/contacts")
public class ContactRequestController {
    private final ContactRequestService service;

    public ContactRequestController(ContactRequestService service) {
        this.service = service;
    }

    // HU-01: Listar solicitudes con filtro opcional por finalidad
    @GetMapping
    public String list(@RequestParam(required = false) Purpose purpose, Model model) {
        List<ContactRequest> contacts = (purpose != null)
                ? service.findByPurpose(purpose)
                : service.findAll();
        model.addAttribute("contacts", contacts);
        model.addAttribute("purposes", Purpose.values());
        model.addAttribute("selectedPurpose", purpose);
        return "admin/contact-requests/list";
    }

    // HU-02: Ver detalle de una solicitud
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<ContactRequest> result = service.findById(id);
        if (result.isEmpty()) {
            model.addAttribute("title", "Detalle de la solicitud de contacto");
            return "error/not-found";
        }
        model.addAttribute("contact", result.get());
        return "admin/contact-requests/detail";
    }

    // HU-03: Confirmar eliminación
    @GetMapping("/delete/{id}")
    public String confirmDelete(@PathVariable Long id, Model model) {
        Optional<ContactRequest> result = service.findById(id);
        if (result.isEmpty()) {
            model.addAttribute("title", "Eliminar solicitud de contacto");
            return "error/not-found";
        }
        model.addAttribute("contact", result.get());
        return "admin/contact-requests/delete";
    }

    // HU-03: Ejecutar eliminación
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) { // <-- AÑADIDO RedirectAttributes
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Solicitud eliminada correctamente."); // <-- AÑADIDO: mensaje flash que sobrevive el redirect
        } catch (EntityNotFoundException e) {
            model.addAttribute("title", "Eliminar solicitud de contacto");
            return "error/not-found";
        }
        return "redirect:/admin/contacts";
    }
}