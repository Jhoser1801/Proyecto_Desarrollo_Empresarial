/* Scripts reutilizables compartidos entre múltiples páginas. */

/*  Toggle visibilidad contraseña
   Usado en: login.html, admin/users/create.html,
             admin/users/update.html
   Requiere: input#passwordInput o input#password
             + span#toggleIcon  */
function togglePassword(inputId, iconId) {
    inputId = inputId || 'password';
    iconId  = iconId  || 'toggleIcon';

    const input = document.getElementById(inputId);
    const icon  = document.getElementById(iconId);
    if (!input || !icon) return;

    if (input.type === 'password') {
        input.type = 'text';
        icon.textContent = 'visibility_off';
    } else {
        input.type = 'password';
        icon.textContent = 'visibility';
    }
}
