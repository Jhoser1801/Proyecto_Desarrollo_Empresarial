/* tailwind-config.js — Latinoamerica Comparte
   Configuración centralizada de colores para Tailwind CSS CDN
   Ubicación: /static/js/tailwind-config.js
   Incluye colores del sistema de diseño + colores usados en JS (dashboard, contacts-list) */



tailwind.config = {
    darkMode: "class",
    theme: {
        extend: {
            colors: {
                primary: "#271b83",
                "primary-container": "#3e369a",
                "primary-fixed": "#e3dfff",
                "primary-fixed-dim": "#c4c0ff",
                "on-primary": "#ffffff",
                "on-primary-container": "#afaaff",
                "on-primary-fixed": "#130068",
                "on-primary-fixed-variant": "#3f389b",

                secondary: "#984700",
                "secondary-container": "#fd7c06",
                "secondary-fixed": "#ffdbc8",
                "secondary-fixed-dim": "#ffb68a",
                "on-secondary": "#ffffff",
                "on-secondary-container": "#5c2900",
                "on-secondary-fixed": "#321300",
                "on-secondary-fixed-variant": "#743500",

                tertiary: "#640021",
                "tertiary-container": "#8e0033",
                "tertiary-fixed": "#ffd9dd",
                "tertiary-fixed-dim": "#ffb2bc",
                "on-tertiary": "#ffffff",
                "on-tertiary-container": "#ff94a4",
                "on-tertiary-fixed": "#400012",
                "on-tertiary-fixed-variant": "#910034",

                surface: "#fef7ff",
                "surface-bright": "#fef7ff",
                "surface-dim": "#e5d2ff",
                "surface-variant": "#ecdcff",
                "surface-container-lowest": "#ffffff",
                "surface-container-low": "#faf0ff",
                "surface-container": "#f5eaff",
                "surface-container-high": "#f0e3ff",
                "surface-container-highest": "#ecdcff",
                "surface-tint": "#5851b5",

                "on-surface": "#22133a",
                "on-surface-variant": "#474552",
                "on-background": "#22133a",

                background: "#fef7ff",

                outline: "#787583",
                "outline-variant": "#c8c4d4",

                "inverse-surface": "#382950",
                "inverse-on-surface": "#f7edff",
                "inverse-primary": "#c4c0ff",

                error: "#ba1a1a",
                "error-container": "#ffdad6",
                "on-error": "#ffffff",
                "on-error-container": "#93000a",

                /*  Agregados desde login.html (mensaje de logout)  */
                "success-bg":     "#f0fdf4",
                "success-text":   "#15803d",
                "success-border": "#86efac",

                /*  Colores para gráfica (dashboard.js) */
                "chart-line":    "#6366f1",
                "chart-tick":    "#94a3b8",
                "chart-tooltip": "#111827",

                /* Colores para tarjetas de estadísticas (contacts-list.js)  */
                "stat-primary-text": "#271b83",
                "stat-purple-text":  "#6750a4",
                "stat-rose-text":    "#7d5260",

                /* Colores para botón play y video en general (home.html, create.html, update.html) */
                "yt-play-bg":    "rgba(0,0,0,0.60)",
                "yt-play-hover": "#FF0000",
                "yt-icon":       "#FF0000",
            },
            fontFamily: {
                headline: ["Manrope"],
                body: ["Inter"],
                label: ["Inter"]
            }
        }
    }
};

/*
   THEME_COLORS
   Los colores hardcodeados en dashboard.js y contacts-list.js deben
   leerse desde aquí.
   */
const THEME_COLORS = {
    /* Paleta principal */
    primary:          "#271b83",
    primaryContainer: "#3e369a",

    /* Gráfica — dashboard.js */
    chartLine:        "#6366f1",
    chartTick:        "#94a3b8",
    chartTooltip:     "#111827",
    chartGrid:        "rgba(148,163,184,0.08)",
    chartGradientTop: "rgba(99,102,241,0.35)",
    chartGradientBot: "rgba(99,102,241,0.02)",

    /* Tarjetas de estadísticas por finalidad — contacts-list.js */
    statCards: [
        { bg: "rgba(39,27,131,0.08)",  text: "#271b83", bar: "#271b83", icon: "work"     },
        { bg: "rgba(103,80,164,0.10)", text: "#6750a4", bar: "#6750a4", icon: "school"   },
        { bg: "rgba(125,82,96,0.10)",  text: "#7d5260", bar: "#7d5260", icon: "campaign" },
    ],

    /* Navbar news-detail.js */
    navbarSolid:  "rgba(255,255,255,0.92)",
    navbarShadow: "0 4px 24px rgba(39,27,131,0.08)",
    navbarLinks:  "#271b83",
};