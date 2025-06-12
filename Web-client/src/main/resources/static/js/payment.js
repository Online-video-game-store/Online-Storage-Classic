function updatePaymentUI() {
    let selectedPayment = document.querySelector('input[name="paymentMethod"]:checked');
    let cardSection = document.getElementById("cardSection");
    cardSection.style.display = selectedPayment && selectedPayment.dataset.supportsCards === "true" ? "block" : "none";
}

function formatCardNumber(input) {
    input.value = input.value.replace(/\D/g, "").replace(/(.{4})/g, "$1 ").trim().substring(0, 19);
}

function formatExpiryDate(input) {
    input.value = input.value.replace(/\D/g, "").replace(/(\d{2})(\d{0,2})/, "$1/$2").substring(0, 5);
}

function validateCVV(input) {
    input.value = input.value.replace(/\D/g, "").substring(0, 3);
}


async function addNewCard() {
    const cardNumber = document.getElementById("newCardNumber").value;
    const expiryDate = document.getElementById("newCardExpiry").value;
    const cvv = document.getElementById("newCardCvv").value;

    try {
        const response = await fetch("/pk8000/catalog/payment/add-card", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ cardNumber, expiryDate, cvv })
        });
        if (response.ok) {
            location.reload();
        } else {
            const error = await response.json();
            alert(error.message || "Произошла ошибка при добавлении карты");
        }
    } catch (e) {
        // Сетевые ошибки или проблемы с JSON
        console.error("Ошибка запроса на добавление карты:", e);
        alert("Не удалось связаться с сервером.");
    }
}


async function processPayment() {
    const selectedPayment = document.querySelector('input[name="paymentMethod"]:checked');
    const selectedCard = document.querySelector('input[name="selectedCard"]:checked');

    if (!selectedPayment) {
        alert("Выберите платежную систему");
        return;
    }

    let paymentData = {
        paymentMethodId: selectedPayment.value,
        cardId: selectedCard ? selectedCard.value : null
    };

    try {
        const response = await fetch("/pk8000/catalog/payment/process", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(paymentData)
        });
        if (response.ok) {
            window.location.href = "/pk8000/catalog/index";
        } else {
            const error = await response.json();
            alert(error.message || "Произошла ошибка при обработке платежа");
        }
    } catch (e) {
        // Сетевые ошибки или проблемы с JSON
        console.error("Ошибка запроса на создание заказа:", e);
        alert("Не удалось связаться с сервером.");
    }
}
