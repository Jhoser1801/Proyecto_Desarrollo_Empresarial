package dev.jhoserbriceno.latinoamerica.controller;

import dev.jhoserbriceno.latinoamerica.model.constant.NewsStatus;
import dev.jhoserbriceno.latinoamerica.model.dto.NewsDTO;
import dev.jhoserbriceno.latinoamerica.model.entity.News;
import dev.jhoserbriceno.latinoamerica.model.service.NewsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/news")
public class NewsController {

    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    // HU-11: Listar todas las noticias
    @GetMapping
    public String list(Model model) {
        model.addAttribute("news", service.findAll());
        return "admin/news/list";
    }

    // HU-08: Formulario crear
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("newsRequest",
                new NewsDTO("", "", "", "", "", null));
        model.addAttribute("states", NewsStatus.values());
        return "admin/news/create";
    }

    // HU-08: Guardar nueva noticia
    @PostMapping
    public String create(@Valid @ModelAttribute("newsRequest") NewsDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("states", NewsStatus.values());
            return "admin/news/create";
        }

        service.create(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Noticia creada exitosamente.");
        return "redirect:/admin/news";
    }

    // HU-09: Formulario editar
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        News n;
        try {
            n = service.findByIdOrThrow(id);
        } catch (EntityNotFoundException e) {
            model.addAttribute("title", "Editar Noticias");
            return "error/not-found";
        }

        model.addAttribute("news", n);
        model.addAttribute("newsRequest", new NewsDTO(
                n.getTitle(),
                n.getSummary(),
                n.getContent(),
                n.getImageUrl(),
                n.getAuthor(),
                n.getState()
        ));
        model.addAttribute("statuses", NewsStatus.values());

        return "admin/news/update";
    }

    // HU-09: Guardar cambios
    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("newsRequest") NewsDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", NewsStatus.values());
            try {
                model.addAttribute("news", service.findByIdOrThrow(id));
            } catch (EntityNotFoundException e) {
                model.addAttribute("title", "Editar Noticias");
                return "error/not-found";
            }
            return "admin/news/update";
        }

        try {
            service.update(id, dto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("title", "Editar Noticias");
            return "error/not-found";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Noticia actualizada exitosamente.");
        return "redirect:/admin/news";
    }

    // HU-10: Confirmar eliminación
    @GetMapping("/delete/{id}")
    public String confirmDelete(@PathVariable Long id, Model model) {

        News n;
        try {
            n = service.findByIdOrThrow(id);
        } catch (EntityNotFoundException e) {
            model.addAttribute("title", "Eliminar Noticias");
            return "error/not-found";
        }

        model.addAttribute("news", n);
        return "admin/news/delete";
    }

    // HU-10: Ejecutar eliminación
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
        } catch (EntityNotFoundException e) {
            model.addAttribute("title", "Eliminar Noticias");
            return "error/not-found";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Noticia eliminada exitosamente.");
        return "redirect:/admin/news";
    }
}