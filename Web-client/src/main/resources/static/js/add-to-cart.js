document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".buy-button").forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();

            let form = this.closest("form");
            let productId = form.querySelector("input[name='productId']").value;
            let quantity = form.querySelector("input[name='quantity']").value;

            let productItem = this.closest(".product-item");
            let productName = productItem.dataset.name;
            let productPrice = productItem.dataset.price;

            fetch("/pk8000/catalog/api/add-to-cart", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ productId, quantity })
            })
            .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        showNotification(`Добавлено: ${productName} x${quantity} (Сумма: $${productPrice*quantity})`);
                        updateCartCount();                           // Обновляем значок корзины
                    } else {
                        showNotification(`Ошибка: ${data.message}`, true);
                    }
                })
                .catch(error => {
                    showNotification("Ошибка при добавлении в корзину", true);
                    console.error("Ошибка запроса:", error);
                });
        });
    });
});

function updateCartCount() {
    fetch('/pk8000/catalog/api/get-cart-count')
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка при получении количества товаров');
            }
            return response.json(); // ожидаем ответ в виде числа
        })
        .then(count => {
            console.log(`В корзине ${count} товаров`);
            let cartCountElement = document.querySelector(".cart-count");
            if (cartCountElement) {
                cartCountElement.textContent = count;
            }
        })
        .catch(error => {
            console.error('Ошибка запроса:', error);
        });
}

function showNotification(message, isError = false) {
    let notification = document.createElement("div");
    notification.className = "notification" + (isError ? " error" : "");
    notification.textContent = message;
    document.body.appendChild(notification);
    setTimeout(() => notification.remove(), 3000);
}
