/*
   Usado en: public/news-detail.html
   Requiere: tailwind-config.js (THEME_COLORS) cargado antes en el HTML
 */


/* Referencias al DOM
   Se obtienen una sola vez al cargar el script.
   Así no se buscan en el DOM en cada scroll. */
const heroBg     = document.getElementById('heroBg');
const hero       = document.getElementById('heroParallax');
const scrollHint = document.getElementById('scrollHint');
const navbar     = document.getElementById('navbar');
const article    = document.getElementById('articleContent');
const readBar    = document.getElementById('readProgress');

/* Elementos reveal cacheados una sola vez.
   Antes se hacía querySelectorAll dentro del scroll,
   lo que recorría el DOM entero en cada frame. */
const revealEls  = document.querySelectorAll('.reveal');
let ticking = false;

function onScroll() {
    if (ticking) return; // ya hay un frame pendiente, ignorar

    ticking = true;
    requestAnimationFrame(() => {
        processScroll();
        ticking = false; // liberar para el próximo frame
    });
}

function processScroll() {
    const scrollY = window.scrollY;
    const heroH   = hero ? hero.offsetHeight : 0;

    if (hero && heroBg && scrollY < heroH) {
        heroBg.style.transform = `scale(1.05) translateY(${scrollY * 0.4 * 0.95}px)`;

        /* Desvanece el hint de scroll al bajar */
        if (scrollHint) {
            scrollHint.style.opacity = Math.max(0, 1 - scrollY / 120);
        }
    }
    if (navbar) {
        if (scrollY > 80) {
            navbar.style.background     = THEME_COLORS.navbarSolid;
            navbar.style.backdropFilter = 'blur(20px)';
            navbar.style.boxShadow      = THEME_COLORS.navbarShadow;
            navbar.querySelectorAll('a:not(.portal-btn)').forEach(a => {
                a.style.color = THEME_COLORS.navbarLinks;
            });
        } else {
            navbar.style.background     = 'transparent';
            navbar.style.backdropFilter = 'none';
            navbar.style.boxShadow      = 'none';
            navbar.querySelectorAll('a:not(.portal-btn)').forEach(a => {
                a.style.color = '';
            });
        }
    }

    /*  Barra de progreso de lectura
       Calcula qué porcentaje del artículo ya pasó por
       la pantalla y actualiza el ancho de #readProgress.*/
    if (article && readBar) {
        const start  = article.offsetTop;
        const height = article.offsetHeight;
        const pct    = Math.min(100, Math.max(0,
            ((scrollY - start + window.innerHeight) / height) * 100
        ));
        readBar.style.width = pct + '%';
    }

    /*  Reveal en scroll
       Usa los elementos cacheados en revealEls (arriba),
       en vez de buscarlos en el DOM en cada frame.
       Solo procesa los que aún no son visibles. */
    revealEls.forEach(el => {
        if (!el.classList.contains('visible') &&
            el.getBoundingClientRect().top < window.innerHeight - 60) {
            el.classList.add('visible');
        }
    });
}

window.addEventListener('scroll', onScroll, { passive: true });


onScroll();


document.addEventListener('DOMContentLoaded', () => {

    /* Tiempo de lectura
       Cuenta las palabras del artículo y el resumen,
       y estima el tiempo a ~200 palabras por minuto.
       */
    const body      = document.getElementById('articleBody');
    const summary   = document.querySelector('.pull-quote');
    const wordCount = (
        (body    ? body.innerText    : '') + ' ' +
        (summary ? summary.innerText : '')
    ).trim().split(/\s+/).length;

    const mins  = Math.max(1, Math.ceil(wordCount / 200));
    const label = mins + ' min de lectura';

    const rt = document.getElementById('readTime');
    const rs = document.getElementById('readTimeSidebar');
    if (rt) rt.textContent = label;
    if (rs) rs.textContent = label;

    const authorName = document.getElementById('authorAvatar')?.dataset.author
        || document.querySelector('[data-author]')?.dataset.author
        || document.querySelector('h1')?.textContent?.trim()
        || 'A';

    const avatar = document.getElementById('authorAvatar');
    if (avatar) avatar.textContent = authorName.charAt(0).toUpperCase();

    /* Re-ejecutar scroll para calcular estado inicial correcto */
    onScroll();
});


/* Compartir artículo
   Usa la Web Share API nativa si está disponible (móvil).
   Si no, copia la URL al portapapeles y muestra feedback. */
function shareArticle() {
    if (navigator.share) {
        navigator.share({ title: document.title, url: window.location.href })
            .catch(() => {}); // el usuario canceló, ignorar
    } else {
        navigator.clipboard.writeText(window.location.href).then(() => {
            const btn = document.querySelector('[onclick="shareArticle()"]');
            if (btn) {
                /* Feedback visual: cambia el ícono por un check 2 segundos */
                btn.innerHTML = '<span class="material-symbols-outlined text-base">check</span>';
                setTimeout(() => {
                    btn.innerHTML = '<span class="material-symbols-outlined text-base">share</span>';
                }, 2000);
            }
        });
    }
}