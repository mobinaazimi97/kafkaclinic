function formatDateTime(dateTimeStr) {
    const lang = currentLang;
    const dt = new Date(dateTimeStr);
    if (lang === 'fa') {
        return dt.toLocaleDateString('fa-IR') + ' ' + dt.toLocaleTimeString('fa-IR', {hour: '2-digit', minute:'2-digit'});
    } else {
        return dt.toLocaleString('en-US', {dateStyle: 'medium', timeStyle: 'short'});
    }
}

function showMessage(container, message, isError = false) {
    container.style.color = isError ? 'red' : 'green';
    container.innerText = message;
}
