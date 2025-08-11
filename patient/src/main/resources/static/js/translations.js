let currentLang = 'fa';
let translations = {};

function loadTranslations(lang) {
    return fetch(`lang/${lang}.json`)
        .then(res => res.json())
        .then(data => {
            translations = data;
            currentLang = lang;
            applyTranslations();
        });
}

function applyTranslations() {
    document.querySelectorAll('[data-i18n]').forEach(elem => {
        const key = elem.getAttribute('data-i18n');
        if (translations[key]) {
            elem.textContent = translations[key];
        }
    });
}

function setLanguage(lang) {
    loadTranslations(lang);
}
