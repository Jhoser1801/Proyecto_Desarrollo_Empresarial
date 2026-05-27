package dev.jhoserbriceno.latinoamerica.config;

import dev.jhoserbriceno.latinoamerica.model.constant.NewsStatus;
import dev.jhoserbriceno.latinoamerica.model.constant.Purpose;
import dev.jhoserbriceno.latinoamerica.model.entity.*;
import dev.jhoserbriceno.latinoamerica.model.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataSeed implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final TestimonialRepository testimonialRepository;
    private final NewsRepository newsRepository;
    private final ContactRequestRepository contactRequestRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    public DataSeed(AdminRepository adminRepository,
                    TestimonialRepository testimonialRepository,
                    NewsRepository newsRepository,
                    ContactRequestRepository contactRequestRepository,
                    PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.testimonialRepository = testimonialRepository;
        this.newsRepository = newsRepository;
        this.contactRequestRepository = contactRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Guard: si ya hay datos en la BD, no volver a sembrar
        if (adminRepository.count() > 0) {
            return;
        }

        // 1. Crear administrador por defecto
        Admin admin = new Admin(
                "Administrador",
                adminEmail,
                passwordEncoder.encode(adminPassword)
        );
        adminRepository.save(admin);

        // 2. Testimonios — videos reales de Colombia Comparte
        Testimonial t1 = new Testimonial();
        t1.setName("Andres Rodriguez");
        t1.setVideoUrl("https://youtube.com/shorts/lYeEKG9Gr38");
        t1.setInstagramUrl("https://instagram.com/colombiacomparte");
        t1.setFacebookUrl("https://facebook.com/colombiacomparte");
        testimonialRepository.save(t1);

        Testimonial t2 = new Testimonial();
        t2.setName("Andrea Carolina Aponte");
        t2.setVideoUrl("https://youtube.com/shorts/llTZr8jl6HM");
        t2.setInstagramUrl("https://instagram.com/colombiacomparte");
        t2.setFacebookUrl("https://facebook.com/colombiacomparte");
        testimonialRepository.save(t2);

        Testimonial t3 = new Testimonial();
        t3.setName("Video Expansión");
        t3.setVideoUrl("https://youtube.com/shorts/DQzp7-0SBhI");
        t3.setInstagramUrl("https://instagram.com/colombiacomparte");
        t3.setFacebookUrl("https://facebook.com/colombiacomparte");
        testimonialRepository.save(t3);

        Testimonial t4 = new Testimonial();
        t4.setName("Enrique Amores");
        t4.setVideoUrl("https://youtube.com/shorts/-A6rql7lPD8");
        t4.setInstagramUrl("https://instagram.com/colombiacomparte");
        t4.setFacebookUrl("https://facebook.com/colombiacomparte");
        testimonialRepository.save(t4);

        Testimonial t5 = new Testimonial();
        t5.setName("Diana Lindo");
        t5.setVideoUrl("https://youtube.com/shorts/yeFS-x_5ZHA");
        t5.setInstagramUrl("https://instagram.com/colombiacomparte");
        t5.setFacebookUrl("https://facebook.com/colombiacomparte");
        testimonialRepository.save(t5);

        // 3. Noticias de ejemplo
        News n1 = new News();
        n1.setTitle("Lanzamiento del programa EDIFICA en Ecuador");
        n1.setSummary("El programa EDIFICA llega a Ecuador con nuevas oportunidades de emprendimiento para jóvenes.");
        n1.setContent("El programa EDIFICA ha sido lanzado oficialmente en Ecuador, ofreciendo formación, mentoría y financiamiento a jóvenes emprendedores de todo el país. Esta iniciativa busca transformar vidas a través del emprendimiento sostenible.");
        n1.setImageUrl("https://picsum.photos/seed/edifica/800/400");
        n1.setAuthor("Equipo Comparte");
        n1.setState(NewsStatus.NEWS_STATUS_PUBLISHED);
        newsRepository.save(n1);

        News n2 = new News();
        n2.setTitle("Shows y conferencias: agenda 2025");
        n2.setSummary("Conoce nuestra agenda de eventos y conferencias para este año.");
        n2.setContent("Latinoamérica Comparte presenta su agenda completa de shows y conferencias para 2025, con presencia en más de 10 ciudades del país. Cada evento es una oportunidad única para conectar, aprender y crecer.");
        n2.setImageUrl("https://picsum.photos/seed/shows/800/400");
        n2.setAuthor("Equipo Comparte");
        n2.setState(NewsStatus.NEWS_STATUS_PUBLISHED);
        newsRepository.save(n2);

        News n3 = new News();
        n3.setTitle("Próximo taller de liderazgo (Borrador)");
        n3.setSummary("Taller de liderazgo en preparación.");
        n3.setContent("Contenido en preparación para el taller de liderazgo...");
        n3.setImageUrl("https://picsum.photos/seed/liderazgo/800/400");
        n3.setAuthor("Equipo Comparte");
        n3.setState(NewsStatus.NEWS_STATUS_DRAFT);
        newsRepository.save(n3);

        News n4 = new News();
        n4.setTitle("Jornada solidaria en comunidades rurales");
        n4.setSummary("Nueva jornada de impacto social.");
        n4.setContent("Contenido de ejemplo.");
        n4.setImageUrl("https://tse1.mm.bing.net/th/id/OIP.hDdj-qXeLG3nhdnhFdQ-xgHaFk?rs=1&pid=ImgDetMain&o=7&rm=3");
        n4.setAuthor("Equipo Comparte");
        n4.setState(NewsStatus.NEWS_STATUS_PUBLISHED);
        newsRepository.save(n4);

        News n5 = new News();
        n5.setTitle("Convocatoria abierta para nuevos líderes");
        n5.setSummary("Inscripciones disponibles.");
        n5.setContent("Contenido de ejemplo.");
        n5.setImageUrl("https://tse1.mm.bing.net/th/id/OIP.7MAnvO0hmWsy45WHKc5UGQHaE6?rs=1&pid=ImgDetMain&o=7&rm=3");
        n5.setAuthor("Equipo Comparte");
        n5.setState(NewsStatus.NEWS_STATUS_PUBLISHED);
        newsRepository.save(n5);

        // 4. Solicitudes de contacto de ejemplo
        ContactRequest cr1 = new ContactRequest();
        cr1.setName("Pedro Rojas");
        cr1.setEmail("pedro.rojas@mail.com");
        cr1.setPhone("0991234567");
        cr1.setPurpose(Purpose.PURPOSE_SERVICE);
        contactRequestRepository.save(cr1);

        ContactRequest cr2 = new ContactRequest();
        cr2.setName("Lucía Vega");
        cr2.setEmail("lucia.vega@mail.com");
        cr2.setPhone("0987654321");
        cr2.setPurpose(Purpose.PURPOSE_EDIFICA_PROGRAM);
        contactRequestRepository.save(cr2);

        ContactRequest cr3 = new ContactRequest();
        cr3.setName("Miguel Herrera");
        cr3.setEmail("miguel@mail.com");
        cr3.setPhone("0977777777");
        cr3.setPurpose(Purpose.PURPOSE_SERVICE);
        contactRequestRepository.save(cr3);

        ContactRequest cr4 = new ContactRequest();
        cr4.setName("Valentina Cruz");
        cr4.setEmail("valentina@mail.com");
        cr4.setPhone("0966666666");
        cr4.setPurpose(Purpose.PURPOSE_EDIFICA_PROGRAM);
        contactRequestRepository.save(cr4);
    }
}
