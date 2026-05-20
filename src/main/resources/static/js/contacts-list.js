/*   Tabla de contactos: expand/collapse, filtros y estadísticas.
   Usado en: admin/contact-requests/list.html
   Requiere: tailwind-config.js (THEME_COLORS) cargado antes en el HTML */

/*  Expand / Collapse fila  */
function toggleExpand(row) {
    const expandId  = row.dataset.expandTarget;
    const expandRow = document.getElementById(expandId);
    const isOpen    = expandRow.classList.contains('open');

    document.querySelectorAll('.expand-row.open').forEach(r => r.classList.remove('open'));
    document.querySelectorAll('.contact-row.row-active').forEach(r => r.classList.remove('row-active'));

    if (!isOpen) {
        const name    = row.dataset.name;
        const email   = row.dataset.email;
        const phone   = row.dataset.phone;
        const purpose = row.dataset.purposeDisplay;
        const date    = row.dataset.date;
        const id      = row.dataset.id;

        const parts    = name.trim().split(' ');
        const initials = parts.length >= 2
            ? (parts[0][0] + parts[1][0]).toUpperCase()
            : name.substring(0, 2).toUpperCase();

        expandRow.querySelector('.expand-avatar').textContent  = initials;
        expandRow.querySelector('.expand-name').textContent    = name;
        expandRow.querySelector('.expand-purpose').textContent = purpose;

        const emailEl       = expandRow.querySelector('.expand-email');
        emailEl.textContent = email;
        emailEl.href        = 'mailto:' + email;

        expandRow.querySelector('.expand-phone').textContent  = phone;
        expandRow.querySelector('.expand-date').textContent   = date;
        expandRow.querySelector('.expand-delete').href        = `/admin/contacts/delete/${id}`;

        expandRow.classList.add('open');
        row.classList.add('row-active');
    }
}

/* Colores para tarjetas de estadísticas — definidos en tailwind-config.js (THEME_COLORS.statCards) */
const COLORS = THEME_COLORS.statCards;

/* Construir tarjetas de resumen por finalidad */
function buildStats() {
    const rows   = document.querySelectorAll('.contact-row');
    const total  = rows.length;
    const counts = {};

    rows.forEach(r => {
        const p     = r.dataset.purposeDisplay || 'Otro';
        counts[p]   = (counts[p] || 0) + 1;
    });

    const container   = document.getElementById('statsCards');
    container.innerHTML = '';

    Object.entries(counts).forEach(([purpose, count], i) => {
        const c   = COLORS[i % COLORS.length];
        const pct = total > 0 ? Math.round((count / total) * 100) : 0;

        const card                   = document.createElement('div');
        card.className               = 'stat-card rounded-2xl p-5 flex flex-col gap-3 select-none';
        card.style.background        = c.bg;
        card.dataset.purposeFilter   = purpose;

        card.innerHTML = `
            <div class="flex items-start justify-between">
                <div>
                    <p class="text-[10px] font-black uppercase tracking-widest mb-1" style="color:${c.text};opacity:.6">${purpose}</p>
                    <p class="text-3xl font-black" style="color:${c.text}">${count}</p>
                </div>
                <span class="material-symbols-outlined text-3xl opacity-50" style="color:${c.text};font-variation-settings:'FILL' 1">${c.icon}</span>
            </div>
            <div class="space-y-1">
                <div class="flex justify-between text-[10px] font-bold" style="color:${c.text};opacity:.5">
                    <span>del total</span><span>${pct}%</span>
                </div>
                <div class="h-1.5 rounded-full overflow-hidden" style="background:rgba(0,0,0,.08)">
                    <div class="stat-bar-fill h-full rounded-full" style="width:0%;background:${c.bar}"></div>
                </div>
            </div>
            <p class="text-[10px] font-bold opacity-60" style="color:${c.text}">Clic para filtrar →</p>
        `;

        card.addEventListener('click', () => filterByPurpose(purpose, card));
        container.appendChild(card);
        setTimeout(() => { card.querySelector('.stat-bar-fill').style.width = pct + '%'; }, 80 + i * 100);
    });
}

/* Estado del filtro activo */
let currentPurposeFilter = '';

function filterByPurpose(purpose, clickedCard) {
    if (currentPurposeFilter === purpose) { clearFilter(); return; }

    currentPurposeFilter = purpose;
    document.getElementById('clearFilterBtn').classList.remove('hidden');
    document.querySelectorAll('.stat-card').forEach(c => { c.classList.remove('active-filter'); c.style.opacity = '0.45'; });
    if (clickedCard) { clickedCard.classList.add('active-filter'); clickedCard.style.opacity = '1'; }
    applyFilters();
}

function clearFilter() {
    currentPurposeFilter = '';
    document.getElementById('clearFilterBtn').classList.add('hidden');
    document.querySelectorAll('.stat-card').forEach(c => { c.classList.remove('active-filter'); c.style.opacity = '1'; });
    applyFilters();
}

/* Filtrado combinado (búsqueda + finalidad)  */
function applyFilters() {
    const search = document.getElementById('searchInput').value.toLowerCase().trim();
    const rows   = document.querySelectorAll('.contact-row');
    let   visible = 0;

    document.querySelectorAll('.expand-row.open').forEach(r => r.classList.remove('open'));
    document.querySelectorAll('.contact-row.row-active').forEach(r => r.classList.remove('row-active'));

    rows.forEach(row => {
        const name    = (row.dataset.name    || '').toLowerCase();
        const email   = (row.dataset.email   || '').toLowerCase();
        const purDisp = (row.dataset.purposeDisplay || '').toLowerCase();
        const purpose = (row.dataset.purpose || '');

        const matchSearch  = !search || name.includes(search) || email.includes(search) || purDisp.includes(search);
        const matchPurpose = !currentPurposeFilter || purpose === currentPurposeFilter;
        const show         = matchSearch && matchPurpose;

        row.style.display = show ? '' : 'none';
        const expandRow = document.getElementById(row.dataset.expandTarget);
        if (expandRow) expandRow.style.display = show ? '' : 'none';
        if (show) visible++;
    });

    document.getElementById('noResults').classList.toggle('hidden', visible > 0);
}

/*  Init  */
document.addEventListener('DOMContentLoaded', () => {
    buildStats();
    document.getElementById('searchInput').addEventListener('input', applyFilters);
});
