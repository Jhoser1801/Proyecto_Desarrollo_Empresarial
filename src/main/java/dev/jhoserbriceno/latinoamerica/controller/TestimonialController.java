package dev.jhoserbriceno.latinoamerica.controller;

import dev.jhoserbriceno.latinoamerica.model.dto.TestimonialDTO;
import dev.jhoserbriceno.latinoamerica.model.entity.Testimonial;
import dev.jhoserbriceno.latinoamerica.model.service.TestimonialService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // <-- AÑADIDO

import java.util.Optional;

@Controller
@RequestMapping("/admin/testimonials")
public class TestimonialController {
    private final TestimonialService service;

    public TestimonialController(TestimonialService service) {
        this.service = service;
    }

    // HU-07: Listar testimonios
    @GetMapping
    public String list(Model model) {
        model.addAttribute("testimonials", service.findAll());
        return "admin/testimonials/list";
    }

    // HU-04: Crear formulario
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("testimonialRequest", new TestimonialDTO("", "", "", ""));
        return "admin/testimonials/create";
    }

    // HU-04: Guardar nuevo testimonio
    @PostMapping
    public String create(
            @Valid @ModelAttribute("testimonialRequest") TestimonialDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) { // <-- AÑADIDO RedirectAttributes
        //guardamos los errores con bindin...
        if (result.hasErrors()) {
            return "admin/testimonials/create"; // Regresa al form con errores
        }
        service.create(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Testimonio creado correctamente."); // <-- AÑADIDO
        return "redirect:/admin/testimonials";
    }

    // HU-05: Editar formulario
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Testimonial> result = service.findById(id);
        if (result.isEmpty()) {
            model.addAttribute("title", "Editar Testimonio");
            return "error/not-found";
        }
        Testimonial t = result.get();
        model.addAttribute("testimonial", t);
        model.addAttribute("testimonialRequest", new TestimonialDTO(
                t.getName(), t.getVideoUrl(), t.getInstagramUrl(), t.getFacebookUrl()));
        return "admin/testimonials/update";
    }

    // HU-05: Guardar cambios
    @PutMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("testimonialRequest") TestimonialDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) { // <-- AÑADIDO RedirectAttributes

        if (result.hasErrors()) {
            // Necesitamos el testimonial para mantener el ID en el formulario
            service.findById(id).ifPresent(t -> model.addAttribute("testimonial", t));
            return "admin/testimonials/update";
        }
        try {
            service.update(id, dto); // Si es válido llama y redirige admin/testimonials
        } catch (EntityNotFoundException e) {
            model.addAttribute("title", "Editar Testimonio");
            return "error/not-found";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Testimonio actualizado correctamente."); // <-- AÑADIDO
        return "redirect:/admin/testimonials";
    }

    // HU-06: Confirmar eliminación
    @GetMapping("/delete/{id}")
    public String confirmDelete(@PathVariable Long id, Model model) {
        Optional<Testimonial> result = service.findById(id);
        if (result.isEmpty()) {
            model.addAttribute("title", "Eliminar Testimonio");
            return "error/not-found";
        }
        model.addAttribute("testimonial", result.get());
        return "admin/testimonials/delete";
    }

    // HU-06: Ejecutar eliminación
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) { // <-- AÑADIDO RedirectAttributes
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Testimonio eliminado correctamente."); // <-- AÑADIDO
        } catch (EntityNotFoundException e) {
            model.addAttribute("title", "Eliminar Testimonio");
            return "error/not-found";
        }
        return "redirect:/admin/testimonials";
    }
}