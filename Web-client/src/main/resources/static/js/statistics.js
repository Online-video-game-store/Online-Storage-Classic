document.addEventListener("DOMContentLoaded", function () {

    function redirectToCategory(element = null) {
        const selectedCategory = element ? element : document.querySelector('.category-button.active-category');
        const categoryId = selectedCategory ? selectedCategory.getAttribute("data-category-id") : '';
        const productName = document.getElementById("productName").value;
        const elemsOfPage = 32;
        const pageNo = 0;

        const from = document.getElementById("from").value;
        const to = document.getElementById("to").value;

        const queryParams = new URLSearchParams({
            categoryId,
            elemsOfPage,
            pageNo,
            productName,
            from,
            to
        });
        window.location.href = `/pk8000/catalog/index/statistics?${queryParams.toString()}`;
    }


    function showProducts(element) {
        const orderId = element.getAttribute("data-order-id");

        fetch(`/pk8000/catalog/api/product/statistics/get-products-form-order/${orderId}`)
            .then(response => {
                if (!response.ok) throw new Error("Ошибка при загрузке товаров");
                return response.json();
            })
            .then(products => {
                const tbody = document.querySelector('#productTable tbody');
                tbody.innerHTML = '';

                // Обновим заголовок с номером заказа
                const modalTitle = document.getElementById('modalTitle');
                modalTitle.textContent = `Список товаров (Заказ № ${orderId})`;

                products.forEach(p => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                    <td>${p.name}</td>
                    <td>${p.quantity}</td>
                    <td>${p.price} ₽</td>
                `;
                    tbody.appendChild(row);
                });

                document.getElementById('productModal').style.display = 'block';
            })
            .catch(error => {
                alert("Не удалось загрузить товары: " + error.message);
            });
    }

    function closeModal() {
        document.getElementById('productModal').style.display = 'none';
    }



    // function showProducts(element) {
    //     console.log("show products")
    //     const orderId = element.getAttribute("data-order-id");
    //
    //     fetch(`/pk8000/catalog/api/product/statistics/get-products-form-order/${orderId}`)
    //         .then(response => {
    //             if (!response.ok) throw new Error("Ошибка при загрузке товаров");
    //             return response.json();
    //         })
    //         .then(products => {
    //             const tbody = document.querySelector('#productTable tbody');
    //             tbody.innerHTML = ''; // Очистим прошлое содержимое
    //
    //             products.forEach(p => {
    //                 const row = document.createElement('tr');
    //                 row.innerHTML = `
    //                 <td>${p.name}</td>
    //                 <td>${p.quantity}</td>
    //                 <td>${p.price} ₽</td>
    //             `;
    //                 tbody.appendChild(row);
    //             });
    //
    //             document.getElementById('productModal').style.display = 'flex';
    //         })
    //         .catch(error => {
    //             alert("Не удалось загрузить товары: " + error.message);
    //         });
    // }
    //
    //
    // function closeModal() {
    //     document.getElementById('productModal').style.display = 'none';
    // }

    // Закрытие по клику вне окна
    window.addEventListener('click', function (event) {
        const modal = document.getElementById('productModal');
        if (event.target === modal) {
            closeModal();
        }
    });

// Закрытие по клавише Escape
    document.addEventListener('keydown', function (event) {
        if (event.key === 'Escape') {
            closeModal();
        }
    });

    window.redirectToCategory = redirectToCategory
    window.showProducts = showProducts
    window.closeModal = closeModal

});

