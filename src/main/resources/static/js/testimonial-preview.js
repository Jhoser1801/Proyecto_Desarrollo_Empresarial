/* Vista previa en tiempo real para crear/editar testimonios.
   Usado en: admin/testimonials/create.html,
             admin/testimonials/update.html
*/

/**
 * Extrae el ID de un video de YouTube desde distintos formatos de URL:
 * - https://www.youtube.com/watch?v=ID
 * - https://youtu.be/ID
 * - https://youtube.com/shorts/ID
 * - https://www.youtube.com/shorts/ID
 * Retorna null si no es una URL de YouTube válida.
 */
function extractYouTubeId(url) {
    if (!url) return null;
    const patterns = [
        /youtube\.com\/watch\?v=([a-zA-Z0-9_-]{11})/,
        /youtu\.be\/([a-zA-Z0-9_-]{11})/,
        /youtube\.com\/shorts\/([a-zA-Z0-9_-]{11})/,
        /youtube\.com\/embed\/([a-zA-Z0-9_-]{11})/
    ];
    for (const pattern of patterns) {
        const match = url.match(pattern);
        if (match) return match[1];
    }
    return null;
}

function updatePreview() {
    const name     = document.getElementById('nameInput')?.value?.trim()     ?? '';
    const videoUrl = document.getElementById('photoUrlInput')?.value?.trim() ?? '';

    const previewName     = document.getElementById('previewName');
    const previewPhoto    = document.getElementById('previewPhoto');
    const previewInitials = document.getElementById('previewInitials');
    const previewPlay     = document.getElementById('previewPlay');

    if (!previewName || !previewPhoto || !previewInitials) return;

    // Nombre e iniciales
    previewName.textContent = name || 'Nombre del testimonio';
    if (name) {
        const parts = name.split(' ');
        previewInitials.textContent = parts.length >= 2
            ? (parts[0][0] + parts[1][0]).toUpperCase()
            : name.substring(0, 2).toUpperCase();
    } else {
        previewInitials.textContent = '?';
    }

    // Miniatura de YouTube
    const ytId = extractYouTubeId(videoUrl);
    if (ytId) {
        previewPhoto.src              = `https://img.youtube.com/vi/${ytId}/hqdefault.jpg`;
        previewPhoto.style.display    = 'block';
        previewInitials.style.display = 'none';
        if (previewPlay) previewPlay.style.display = 'flex';
    } else {
        previewPhoto.style.display    = 'none';
        previewInitials.style.display = 'flex';
        if (previewPlay) previewPlay.style.display = 'none';
    }
}

document.addEventListener('DOMContentLoaded', updatePreview);
