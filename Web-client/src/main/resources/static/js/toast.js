window.showToast = function(message, type = "info") {
    const toast = document.getElementById("toast");
    toast.textContent = message;

    let bgColor;
    switch (type) {
        case "success": bgColor = "#4CAF50"; break;
        case "error": bgColor = "#f44336"; break;
        case "info": default: bgColor = "#333"; break;
    }

    toast.style.backgroundColor = bgColor;
    toast.style.display = "block";
    toast.style.opacity = "1";

    setTimeout(() => {
        toast.style.opacity = "0";
        setTimeout(() => {
            toast.style.display = "none";
        }, 500);
    }, 3000); // Показать на 3 секунды
}

window.showLoading = function(show) {
    const overlay = document.getElementById("loading-overlay");
    overlay.style.display = show ? "flex" : "none";
}
