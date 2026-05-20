package dev.jhoserbriceno.latinoamerica.controller;

import dev.jhoserbriceno.latinoamerica.model.constant.Purpose;
import dev.jhoserbriceno.latinoamerica.model.dto.ContactRequestDTO;
import dev.jhoserbriceno.latinoamerica.model.entity.News;
import dev.jhoserbriceno.latinoamerica.model.service.ContactRequestService;
import dev.jhoserbriceno.latinoamerica.model.service.NewsService;
import dev.jhoserbriceno.latinoamerica.model.service.TestimonialService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;



@Controller
public class HomeController {

    private final TestimonialService testimonialService;
    private final NewsService newsService;
    private final ContactRequestService contactRequestService;

    public HomeController(TestimonialService testimonialService,
                          NewsService newsService,
                          ContactRequestService contactRequestService) {
        this.testimonialService = testimonialService;
        this.newsService = newsService;
        this.contactRequestService = contactRequestService;
    }

    // HU-13, HU-14 (vista Pública)
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("testimonials", testimonialService.findAll());
        model.addAttribute("news", newsService.findPublished());
        model.addAttribute("purposes", Purpose.values());
        model.addAttribute("contactRequest", new ContactRequestDTO("", "", "", null));
        return "public/home";
    }

    // HU-12: Página pública de contacto(Vista dedicada)
    @GetMapping("/contact")
    public String contactPage(Model model) {
        model.addAttribute("purposes", Purpose.values());
        model.addAttribute("contactRequest", new ContactRequestDTO("", "", "", null));
        return "public/contact";
    }

    // HU-12: Usuario Envía formulario de contacto
    @PostMapping("/contact")
    public String submitContact(
            @Valid
            @ModelAttribute("contactRequest") ContactRequestDTO dto, BindingResult result,
            // Bindin... Contiene los errores de validación (si algún campo no cumple las reglas)

            Model model) {
        // result... Si hay errores de validación (campos vacíos, formato inválido, etc.)
        if (result.hasErrors()) {

            // Se vuelven a cargar datos necesarios para el formulario (como el select de propósito)
            model.addAttribute("purposes", Purpose.values());

            // Se retorna la misma vista para mostrar los errores al usuario
            return "public/contact";
        }
        // Si NO hay errores:
        // Se transforma el DTO en entidad y se guarda en la base de datos
        contactRequestService.create(dto);

        // Se redirige a la página de contacto con un parámetro (?success)
        // Esto permite mostrar un mensaje de éxito y evita reenviar el formulario al recargar (PRG pattern)
        // evitamos que el usuario envíe de nuevo el formulario al recargar la página
        return "redirect:/contact?success";
    }
    // HU-14: Detalle público de una noticia
    @GetMapping("/noticias/{id}")
    public String newsDetail(@PathVariable Long id, Model model) {
        try {
            News news = newsService.findByIdOrThrow(id);
            model.addAttribute("news", news);
            return "public/news-detail";
        } catch (EntityNotFoundException e) {
            model.addAttribute("title", "Noticia no encontrada");
            return "error/not-found";
        }
    }
}