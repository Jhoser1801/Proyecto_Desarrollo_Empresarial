package dev.jhoserbriceno.latinoamerica.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");

        if (statusCode != null && statusCode == 404) {
            model.addAttribute("title", "Página no encontrada");
        } else if (statusCode != null && statusCode == 403) {
            model.addAttribute("title", "No tienes permiso para acceder aquí");
        } else {
            model.addAttribute("title", "Ocurrió un error inesperado");
        }

        return "error/not-found";
    }
}
