package dev.jhoserbriceno.latinoamerica.controller;

import dev.jhoserbriceno.latinoamerica.model.service.ContactRequestService;
import dev.jhoserbriceno.latinoamerica.model.service.NewsService;
import dev.jhoserbriceno.latinoamerica.model.service.TestimonialService;
import dev.jhoserbriceno.latinoamerica.model.service.UserAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final ContactRequestService contactRequestService;
    private final TestimonialService testimonialService;
    private final NewsService newsService;
    private final UserAdminService userAdminService;

    public AdminDashboardController(
            ContactRequestService contactRequestService,
            TestimonialService testimonialService,
            NewsService newsService,
            UserAdminService userAdminService) {

        this.contactRequestService = contactRequestService;
        this.testimonialService = testimonialService;
        this.newsService = newsService;
        this.userAdminService = userAdminService;
    }

    @GetMapping
    public String dashboard(Model model) {

        //  Conteos generales
        int totalContacts     = contactRequestService.findAll().size();
        int totalTestimonials = testimonialService.findAll().size();
        int totalNews         = newsService.findAll().size();
        int totalUsers        = userAdminService.findAll().size();

        // Noticias publicadas
        int publishedNews = newsService.findPublished().size();
        int newsPercent   = totalNews == 0 ? 0 : (publishedNews * 100) / totalNews;

        //  Meta mensual de contactos (lógica en el servicio)
        int contactGoal = ContactRequestService.CONTACT_GOAL;
        int goalPercent = contactRequestService.calcGoalPercent(totalContacts);

        // Últimas 5 solicitudes
        List<?> latestContacts = contactRequestService.findAll()
                .stream()
                .limit(5)
                .toList();

        //  Datos mensuales para la gráfica
        List<Object[]> monthlyContacts = contactRequestService.countContactsByMonth();
        String bestMonth = contactRequestService.findBestMonth(monthlyContacts);
        long   bestCount = contactRequestService.findBestCount(monthlyContacts);

        // Exponer atributos al modelo
        model.addAttribute("totalContacts",    totalContacts);
        model.addAttribute("totalTestimonials",totalTestimonials);
        model.addAttribute("totalNews",        totalNews);
        model.addAttribute("totalUsers",       totalUsers);
        model.addAttribute("publishedNews",    publishedNews);
        model.addAttribute("newsPercent",      newsPercent);
        model.addAttribute("contactGoal",      contactGoal);
        model.addAttribute("goalPercent",      goalPercent);
        model.addAttribute("latestContacts",   latestContacts);
        model.addAttribute("monthlyContacts",  monthlyContacts);
        model.addAttribute("bestMonth",        bestMonth);
        model.addAttribute("bestCount",        bestCount);

        return "admin/dashboard";
    }
}