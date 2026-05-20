/* Vista previa del perfil al editar administradores.
   Usado en: admin/users/update.html */

function updatePreview() {
    const name  = document.getElementById('nameInput')?.value?.trim()  ?? '';
    const email = document.getElementById('emailInput')?.value?.trim() ?? '';

    const parts    = name.split(' ').filter(p => p.length > 0);
    let   initials = '?';

    if (parts.length >= 2) {
        initials = (parts[0][0] + parts[1][0]).toUpperCase();
    } else if (parts.length === 1) {
        initials = parts[0].substring(0, 2).toUpperCase();
    }

    const avatar = document.getElementById('previewAvatar');
    const pName  = document.getElementById('previewName');
    const pEmail = document.getElementById('previewEmail');

    if (avatar) avatar.textContent = initials;
    if (pName)  pName.textContent  = name  || 'Nombre del administrador';
    if (pEmail) pEmail.textContent = email || 'Sin correo';
}

document.addEventListener('DOMContentLoaded', updatePreview);
