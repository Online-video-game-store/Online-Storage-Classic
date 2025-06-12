document.addEventListener("DOMContentLoaded", function () {
    const cartButton = document.querySelector(".cart-link");
    const cartPopup = document.getElementById("cart-popup");
    const closeButton = document.querySelector(".close-button");
    const cartItemsContainer = document.querySelector(".cart-items");
    const cartTotal = document.querySelector(".cart-total span");
    const checkoutButton = document.querySelector(".checkout-button");

    // Загрузка данных корзины
    function loadCartItems() {
        fetch("/pk8000/catalog/api/get-cart-items", { credentials: "include" })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Ошибка при получении данных корзины");
                }
                return response.json();
            })
            .then(cartItems => {
                displayCartItems(cartItems);
                updateCartCount(cartItems);
            })
            .catch(error => {
                console.error("Ошибка загрузки корзины:", error);
                cartItemsContainer.innerHTML = "<p>Ошибка загрузки корзины</p>";
                cartTotal.textContent = "$0.00";
                updateCartCount([]);
            });
    }

    // Обновление количества товаров в корзине (значок)
    function updateCartCount(cartItems) {
        let cartItemCount = cartItems.reduce((total, item) => total + item.quantity, 0);
        let cartCountElement = document.querySelector(".cart-count");
        if (cartCountElement) {
            cartCountElement.textContent = cartItemCount;
        }
    }

    // Отображение товаров в корзине
    function displayCartItems(cartItems) {
        cartItemsContainer.innerHTML = "";
        let total = 0;

        cartItems.forEach(item => {
            total += parseFloat(item.totalPrice);

            const cartItemElement = document.createElement("div");
            cartItemElement.classList.add("cart-item");
            cartItemElement.innerHTML = `
                <span>${item.productName} (x${item.quantity})</span>
                <span>$${item.totalPrice.toFixed(2)}</span>
                <span class="remove-button" data-item-id="${item.productId}">&times;</span>
            `;
            cartItemsContainer.appendChild(cartItemElement);
        });
        cartTotal.textContent = `$${total.toFixed(2)}`;
    }

    // Открытие всплывающего окна корзины
    function openCartPopup(event) {
        event.preventDefault();
        loadCartItems();
        cartPopup.style.display = "block";
    }

    // Закрытие всплывающего окна корзины
    function closeCartPopup() {
        cartPopup.style.display = "none";
    }

    // Закрытие по клику вне окна корзины
    function handleOutsideClick(event) {
        if (event.target === cartPopup) {
            closeCartPopup();
        }
    }

    // Удаление товара из корзины
    function removeCartItem(itemId) {
        fetch(`/pk8000/catalog/api/remove-cart-item/${itemId}`, {
            method: 'DELETE',
            credentials: "include"
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Ошибка при удалении товара из корзины");
                }
                loadCartItems();
            })
            .catch(error => {
                console.error("Ошибка при удалении товара:", error);
            });
    }

    // Обработчик клика по кнопке удаления
    document.addEventListener("click", function(event) {
        if (event.target.classList.contains("remove-button")) {
            const itemId = event.target.getAttribute("data-item-id");
            if (itemId && !isNaN(itemId)) {
                removeCartItem(itemId);
            } else {
                console.error("Ошибка: некорректный itemId", itemId);
            }
        }
    });

    //
    // Authenticated methods
    //
    async function checkAuthentication() {
        try {
            const response = await fetch("/pk8000/catalog/api/check", { credentials: "include" });
            return response.ok; // true, если статус 200, иначе false
        } catch (error) {
            console.error("Ошибка проверки авторизации:", error);
            return false;
        }
    }

    // Покупка товаров корзины
    async function processCheckout() {
        const isAuthenticated = await checkAuthentication();
        if (!isAuthenticated) {
            window.location.href = "/oauth2/authorization/online-store-client-id";
            return;
        }
        window.location.href = "/pk8000/catalog/payment";   // переходим контроллер
    }

    // Привязка обработчиков событий
    cartButton.addEventListener("click", openCartPopup);
    closeButton.addEventListener("click", closeCartPopup);
    window.addEventListener("click", handleOutsideClick);
    checkoutButton.addEventListener("click", processCheckout);
});

