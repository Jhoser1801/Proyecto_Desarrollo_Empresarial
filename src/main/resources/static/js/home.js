/* Scripts de la página pública de inicio.
   Usado en: public/home.html
 */

/* Menú móvil */
const menuBtn    = document.getElementById('menuBtn');
const mobileMenu = document.getElementById('mobileMenu');
const menuIcon   = menuBtn ? menuBtn.querySelector('.material-symbols-outlined') : null;

if (menuBtn) {
    menuBtn.addEventListener('click', () => {
        const isOpen = !mobileMenu.classList.contains('hidden');
        mobileMenu.classList.toggle('hidden');
        if (menuIcon) menuIcon.textContent = isOpen ? 'menu' : 'close';
    });
}

function closeMobileMenu() {
    if (!mobileMenu) return;
    mobileMenu.classList.add('hidden');
    if (menuIcon) menuIcon.textContent = 'menu';
}

window.addEventListener('scroll', () => {
    if (mobileMenu && !mobileMenu.classList.contains('hidden')) {
        closeMobileMenu();
    }
}, { passive: true });

/*  Slider testimonios  */
function scrollSlider(direction) {
    const slider = document.getElementById('slider');
    if (!slider) return;
    const cardWidth = (slider.querySelector('div')?.offsetWidth ?? 320) + 32;
    slider.scrollBy({ left: direction * cardWidth, behavior: 'smooth' });
}

/*  Slider noticias */
function scrollNews(direction) {
    const slider = document.getElementById('newsSlider');
    if (!slider) return;
    const cardWidth = (slider.querySelector('div')?.offsetWidth ?? 340) + 32;
    slider.scrollBy({ left: direction * cardWidth, behavior: 'smooth' });
}

/* Contador animado  */
function animateCounter(counter) {
    const target    = parseFloat(counter.dataset.target);
    const prefix    = counter.dataset.prefix || '';
    const suffix    = counter.dataset.suffix || '';
    let   count     = 0;
    const increment = target / 80;

    const update = () => {
        count += increment;
        if (count < target) {
            const value = target < 10 ? count.toFixed(1) : Math.floor(count);
            counter.innerText = prefix + value + suffix;
            requestAnimationFrame(update);
        } else {
            counter.innerText = prefix + target + suffix;
        }
    };
    update();
}

const counters = document.querySelectorAll('.counter');
if (counters.length) {
    const counterSection = counters[0].closest('section');
    if (counterSection) {
        new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    counters.forEach(counter => {
                        if (!counter.classList.contains('done')) {
                            animateCounter(counter);
                            counter.classList.add('done');
                        }
                    });
                }
            });
        }, { threshold: 0.5 }).observe(counterSection);
    }
}

/*  Reveal on scroll para secciones */
const sections        = document.querySelectorAll('section');
const observerReveal  = new IntersectionObserver((entries, obs) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('show');
            obs.unobserve(entry.target);
        }
    });
}, { threshold: 0.12 });

sections.forEach(section => {
    section.classList.add('reveal');
    observerReveal.observe(section);
});

/*  Compartir página (footer)  */
async function compartirPagina() {
    const datos = {
        title: 'Ecuador Comparte',
        text:  'Conoce Ecuador Comparte — conectando artesanos ecuatorianos con el mercado global.',
        url:   window.location.href
    };
    if (navigator.share) {
        try { await navigator.share(datos); } catch (e) { /* usuario canceló */ }
    } else {
        try {
            await navigator.clipboard.writeText(window.location.href);
            const btn = document.querySelector('[onclick="compartirPagina()"] span');
            if (btn) {
                const original = btn.textContent;
                btn.textContent = 'check';
                setTimeout(() => btn.textContent = original, 2000);
            }
        } catch (e) {
            alert('Copia este enlace: ' + window.location.href);
        }
    }
}
// ============================================================
// SCRIPT: FIX MÓVIL PARA SLIDERS (Migrado desde home.html)
// ============================================================

// Ocultar scrollbar visual en navegadores webkit (Chrome, Safari)
const sliderStyle = document.createElement('style');
sliderStyle.textContent = '#slider::-webkit-scrollbar, #newsSlider::-webkit-scrollbar { display: none; }';
document.head.appendChild(sliderStyle);

// Función que agrega soporte de swipe táctil a un elemento slider
function addSwipeSupport(el) {
    if (!el) return;
    let startX = 0;      // posición X donde empieza el toque
    let scrollStart = 0; // scroll inicial del contenedor al tocar

    // Al tocar la pantalla, guardar posición inicial
    el.addEventListener('touchstart', e => {
        startX = e.touches[0].clientX;
        scrollStart = el.scrollLeft;
    }, { passive: true });

    // Al mover el dedo, calcular cuánto se movió y aplicar al scroll
    el.addEventListener('touchmove', e => {
        const delta = startX - e.touches[0].clientX;
        el.scrollLeft = scrollStart + delta;
    }, { passive: true });
}

// Inicializar el soporte táctil de manera segura una vez cargue el DOM
document.addEventListener("DOMContentLoaded", () => {
    const testimonialSlider = document.getElementById('slider');
    const newsSlider = document.getElementById('newsSlider');

    if (testimonialSlider) addSwipeSupport(testimonialSlider);
    if (newsSlider) addSwipeSupport(newsSlider);
});
