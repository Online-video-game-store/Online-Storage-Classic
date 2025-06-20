/**
 * Показывает модальное окно с сообщением.
 * @param {string} message - Сообщение, строки разделяются символом `$`
 * @param {string} type - Тип сообщения: error, warning, info, success
 * @param {boolean} autoClose - Нужно ли автоматически закрывать
 * @param {number} timeout - Через сколько мс закрывать (если autoClose)
 */
window.showToast = function(message, type = 'info', autoClose = false, timeout = 3000) {
    const backdrop = document.getElementById("modal-backdrop");
    const dialog = document.getElementById("modal-dialog");
    const iconEl = document.getElementById("modal-icon");
    const titleEl = document.getElementById("modal-title");
    const textEl = document.getElementById("modal-text");

    const lines = message.split('$');
    textEl.innerHTML = lines.map(line => `<div>${line}</div>`).join('');

    let icon = "💬";
    let title = "Сообщение";
    let borderColor = "#333";

    switch (type) {
        case "error":
            icon = "❌";
            title = "Ошибка";
            borderColor = "#f44336";
            break;
        case "warning":
            icon = "⚠️";
            title = "Внимание";
            borderColor = "#ffc107";
            break;
        case "success":
            icon = "✅";
            title = "Успешно";
            borderColor = "#4CAF50";
            break;
        case "info":
        default:
            icon = "ℹ️";
            title = "Информация";
            borderColor = "#007bff";
            break;
    }

    iconEl.textContent = icon;
    titleEl.textContent = title;
    dialog.style.border = `2px solid ${borderColor}`;

    backdrop.style.display = "flex";

    if (autoClose) {
        setTimeout(() => closeModal(), timeout);
    }
};

window.closeModal = function() {
    const backdrop = document.getElementById("modal-backdrop");
    backdrop.style.display = "none";
};

// Escape — закрытие
document.addEventListener("keydown", function(e) {
    if (e.key === "Escape") {
        closeModal();
    }
});

// Загрузка
window.showLoading = function(show) {
    const overlay = document.getElementById("loading-overlay");
    overlay.style.display = show ? "flex" : "none";
};
