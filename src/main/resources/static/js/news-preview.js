/* Vista previa en tiempo real para crear/editar noticias.
   Usado en: admin/news/create.html, admin/news/update.html */

function updatePreview() {
    const title    = document.querySelector('[name="title"]')?.value?.trim()    ?? '';
    const summary  = document.querySelector('[name="summary"]')?.value?.trim()  ?? '';
    const imageUrl = document.querySelector('[name="imageUrl"]')?.value?.trim() ?? '';

    document.getElementById('previewTitle').textContent   = title   || 'Título de la noticia';
    document.getElementById('previewSummary').textContent = summary || 'El resumen de la noticia aparecerá aquí.';

    const img         = document.getElementById('previewImage');
    const placeholder = document.getElementById('previewImagePlaceholder');

    if (imageUrl) {
        img.src                  = imageUrl;
        img.style.display        = 'block';
        placeholder.style.display = 'none';
    } else {
        img.style.display        = 'none';
        placeholder.style.display = 'flex';
    }
}

/* Inicializar la vista previa al cargar (útil en update) */
document.addEventListener('DOMContentLoaded', updatePreview);
