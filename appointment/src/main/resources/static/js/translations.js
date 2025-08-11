let currentLang = 'fa';
let translations = {};

function setLanguage(lang) {
    currentLang = lang;
    loadTranslations();
}

function loadTranslations() {
    fetch(`lang/${currentLang}.json`)
        .then(res => res.json())
        .then(data => {
            translations = data;
            applyTranslations();
        });
}

function applyTranslations() {
    document.querySelectorAll('[data-i18n]').forEach(el => {
        const key = el.getAttribute('data-i18n');
        if (translations[key]) {
            el.innerText = translations[key];
        }
    });
}

document.addEventListener('DOMContentLoaded', loadTranslations);
