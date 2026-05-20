/* dashboard.js
   Requiere: tailwind-config.js (THEME_COLORS) cargado antes en el HTML */

document.addEventListener('DOMContentLoaded', function () {

    const {
        monthlyContacts = [],
        contactGoal     = 10,
        totalContacts   = 0,
        bestMonth       = 'Sin datos',
        bestCount       = 0
    } = window.__dashboardData ?? {};

    const MESES = [
        'Enero','Febrero','Marzo','Abril','Mayo','Junio',
        'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'
    ];

    // Construir mapa mes -> cantidad desde los datos reales
    const dataMap = {};
    monthlyContacts.forEach(item => {
        dataMap[item[0]] = item[1]; // item[0] = número de més (1-12)
    });

    // Determinar cuántos puntos mostrar (= contactGoal, máximo 12)
    const totalPuntos = Math.min(contactGoal, 12);

    const labels = [];
    const values = [];
    for (let mes = 1; mes <= totalPuntos; mes++) {
        labels.push(MESES[mes - 1]);
        values.push(dataMap[mes] ?? 0);
    }

    // Línea de meta: valor constante = contactGoal
    const goalLine = Array(totalPuntos).fill(contactGoal);

    //  Renderizar el canvas
    const canvas = document.getElementById('statsChart');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');

    const gradient = ctx.createLinearGradient(0, 0, 0, 400);
    gradient.addColorStop(0, THEME_COLORS.chartGradientTop);
    gradient.addColorStop(1, THEME_COLORS.chartGradientBot);

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Solicitudes',
                    data: values,
                    borderColor: THEME_COLORS.chartLine,
                    backgroundColor: gradient,
                    fill: true,
                    tension: 0.45,
                    borderWidth: 4,
                    pointRadius: 5,
                    pointHoverRadius: 8,
                    pointBackgroundColor: THEME_COLORS.chartLine,
                    pointHoverBackgroundColor: THEME_COLORS.chartLine,
                    pointHoverBorderColor: '#ffffff',
                    pointHoverBorderWidth: 3
                },
                {
                    label: 'Meta mensual (' + contactGoal + ')',
                    data: goalLine,
                    borderColor: 'rgba(234,179,8,0.8)',
                    backgroundColor: 'transparent',
                    borderWidth: 2,
                    borderDash: [6, 4],
                    pointRadius: 0,
                    fill: false,
                    tension: 0
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            animation: { duration: 2200, easing: 'easeOutQuart' },
            plugins: {
                legend: {
                    display: true,
                    labels: { color: THEME_COLORS.chartTick, font: { size: 12 } }
                },
                tooltip: {
                    backgroundColor: THEME_COLORS.chartTooltip,
                    padding: 14,
                    cornerRadius: 14,
                    displayColors: true,
                    callbacks: {
                        afterBody: function (items) {
                            const solicitudes = items.find(i => i.datasetIndex === 0)?.raw ?? 0;
                            const pct = Math.min(Math.round((solicitudes / contactGoal) * 100), 100);
                            return ['Avance: ' + pct + '% de la meta'];
                        }
                    }
                }
            },
            scales: {
                x: { grid: { display: false }, ticks: { color: THEME_COLORS.chartTick } },
                y: {
                    beginAtZero: true,
                    suggestedMax: contactGoal + Math.ceil(contactGoal * 0.2),
                    ticks: { stepSize: 1, precision: 0, color: THEME_COLORS.chartTick },
                    grid: { color: THEME_COLORS.chartGrid }
                }
            }
        }
    });

    //  Actualizar textos del panel "Resumen Analítico" dinámicamente
    document.querySelectorAll('[data-dyn="bestMonth"]').forEach(el    => { el.textContent = bestMonth; });
    document.querySelectorAll('[data-dyn="bestCount"]').forEach(el    => { el.textContent = bestCount; });
    document.querySelectorAll('[data-dyn="totalContacts"]').forEach(el => { el.textContent = totalContacts; });
});