// Гарантирует, что DOM уже готов, иначе может быть ошибка, если скрипт загрузится слишком рано.
document.addEventListener("DOMContentLoaded", function () {

    function redirectToCategory(element = null) {
        const selectedCategory = element ? element : document.querySelector('.category-button.active-category');
        const categoryId = selectedCategory ? selectedCategory.getAttribute("data-category-id") : '';

        const productName = document.getElementById("productName").value;
        const minPrice = document.getElementById("minPrice").value;
        const maxPrice = document.getElementById("maxPrice").value;
        const elemsOfPage = 8;
        const pageNo = 0;

        const queryParams = new URLSearchParams({
            categoryId,
            elemsOfPage,
            pageNo,
            productName,
            minPrice,
            maxPrice
        });
        window.location.href = `/pk8000/catalog/index/setup?${queryParams.toString()}`;
    }

    let currentProductId = null;


    function openImageModal(productId) {
        currentProductId = productId;
        fetch(`/pk8000/catalog/api/product/${productId}`)
            .then(res => res.json())
            .then(images => {
                const imageList = document.getElementById("imageList");
                imageList.innerHTML = "";

                images.forEach(imageUrl => {
                    const fileName = extractFileName(imageUrl);

                    const container = document.createElement("div");
                    container.className = "image-preview";

                    const img = document.createElement("img");
                    img.src = imageUrl;
                    img.alt = fileName;
                    img.title = "Кликните для замены изображения";
                    img.onclick = () => {
                        const fileInput = document.getElementById("fileInput");
                        fileInput.dataset.replace = fileName;
                        fileInput.click();
                    };
                    const delBtn = document.createElement("button");
                    delBtn.className = "delete-button";
                    delBtn.innerHTML = "&times;";
                    delBtn.onclick = (e) => {
                        e.stopPropagation();
                        deleteImage(fileName);
                    };

                    container.appendChild(img);
                    container.appendChild(delBtn);
                    imageList.appendChild(container);
                });

                // Кнопка "добавить"
                const addBtn = document.createElement("div");
                addBtn.className = "add-image";
                addBtn.innerHTML = "+";
                addBtn.title = "Добавить новое изображение";
                addBtn.onclick = () => {
                    const fileInput = document.getElementById("fileInput");
                    delete fileInput.dataset.replace;
                    fileInput.click();
                };

                imageList.appendChild(addBtn);
                document.getElementById("imageModal").style.display = "block";
            })
            .catch(err => {
                alert("Ошибка при загрузке изображений");
                console.error(err);
            });
    }

    function closeModal() {
        document.getElementById("imageModal").style.display = "none";
    }

    document.getElementById("fileInput").addEventListener("change", function () {
        if (!this.files || this.files.length === 0)
            return;
        const file = this.files[0];
        const formData = new FormData();
        formData.append("file", file);

        let url = `/pk8000/catalog/api/product/${currentProductId}/upload`;

        // Получаем имя заменяемого файла, если есть
        const replaceRaw = this.dataset.replace;
        if (replaceRaw) {
            let replaceName = replaceRaw.split(/[/\\]/).pop();
            console.log("replaceName", replaceName);
            url += `?replace=${encodeURIComponent(replaceName)}`;
            console.log("url = " + url);
            delete this.dataset.replace; // Очищаем, чтобы не использовать повторно
        }

        fetch(url, {
            method: "POST",
            body: formData
        })
            .then(async response => {
                if (response.ok) {
                    this.value = ""; // Сброс input
                    openImageModal(currentProductId); // Перезагрузка изображений
                } else {
                    const contentType = response.headers.get("Content-Type");
                    let errorMessage = `Ошибка ${response.status}`;
                    if (contentType && contentType.includes("application/json")) {
                        // это JSON
                        try {
                            const errorJson = await response.json();
                            errorMessage = errorJson.message || JSON.stringify(errorJson);
                        } catch (e) {
                            errorMessage = "Ошибка при разборе JSON-ответа от сервера.";
                        }
                    } else {
                        // это просто текст
                        try {
                            const text = await response.text();
                            if (text.trim()) errorMessage = text;
                        } catch (e) {
                            errorMessage = "Ошибка при получении ответа от сервера.";
                        }
                    }
                    throw new Error(errorMessage);
                }
            })
            .catch(error => {
                console.error("Ошибка при загрузке изображения:", error);
                alert("Ошибка при загрузке изображения: " + error.message);
            });
    });

    // Вспомогательная функция для извлечения имени файла из URL
    function extractFileName(url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }


    // Удаление изображения по имени файла
    function deleteImage(fileName) {
        console.log("deleteImage", fileName);
        let replaceName = fileName.split(/[/\\]/).pop();

        fetch(`/pk8000/catalog/api/product/image/${currentProductId}/${encodeURIComponent(replaceName)}`, {
            method: "DELETE"
        })
            .then(async response => {
                if (response.ok) {
                    openImageModal(currentProductId); // перезагружаем список
                } else {
                    const contentType = response.headers.get("Content-Type");
                    let errorMessage = `Ошибка ${response.status}`;
                    if (contentType && contentType.includes("application/json")) {
                        // это JSON
                        try {
                            const errorJson = await response.json();
                            errorMessage = errorJson.message || JSON.stringify(errorJson);
                        } catch (e) {
                            errorMessage = "Ошибка при разборе JSON-ответа от сервера.";
                        }
                    } else {
                        // это просто текст
                        try {
                            const text = await response.text();
                            if (text.trim()) errorMessage = text;
                        } catch (e) {
                            errorMessage = "Ошибка при получении ответа от сервера.";
                        }
                    }
                    throw new Error(errorMessage);
                }
            })
            .catch(error => {
                console.error("Ошибка при удалении изображения:", error);
                alert("Ошибка при удалении изображения: " + error.message);
            });
    }


    function saveProduct(productId) {

        const nameInput = document.querySelector(`input[name="name_${productId}"]`);
        const priceInput = document.querySelector(`input[name="price_${productId}"]`);
        // const categoryInput = document.querySelector(`input[name="category_${productId}"]`);
        const categoryInput = document.querySelector(`select[name="category_${productId}"]`);
        const stockInput = document.querySelector(`input[name="stock_${productId}"]`);
        const descriptionInput = document.querySelector(`textarea[name="description_${productId}"]`);

        // Формируем поля запроса
        const formData = new FormData();
        formData.append("productId", productId);
        if (nameInput) formData.append("name", nameInput.value);
        if (priceInput) formData.append("price", priceInput.value);
        if (categoryInput) formData.append("category", categoryInput.value);
        if (stockInput) formData.append("stock", stockInput.value);
        if (descriptionInput) formData.append("description", descriptionInput.value);

        // Отправляем на сервер
        fetch("/pk8000/catalog/api/product/update", {
            method: "POST",
            body: formData
        })
            .then(async response => {
                if (response.ok) {
                    alert("Данные успешно изменены!");
                } else {
                    const contentType = response.headers.get("Content-Type");
                    let errorMessage = `Ошибка ${response.status}`;
                    if (contentType && contentType.includes("application/json")) {
                        // это JSON
                        try {
                            const errorJson = await response.json();
                            errorMessage = errorJson.message || JSON.stringify(errorJson);
                        } catch (e) {
                            errorMessage = "Ошибка при разборе JSON-ответа от сервера.";
                        }
                    } else {
                        // это просто текст
                        try {
                            const text = await response.text();
                            if (text.trim()) errorMessage = text;
                        } catch (e) {
                            errorMessage = "Ошибка при получении ответа от сервера.";
                        }
                    }
                    throw new Error(errorMessage);
                }
            })
            .catch(error => {
                console.error("Ошибка при обновлении продукта:", error);
                alert("Ошибка при обновлении: " + error.message);
            });
    }


    function addNewProduct() {
        const name = document.getElementById("new_name").value;
        const price = document.getElementById("new_price").value;
        const category = document.getElementById("new_category").value;
        const stock = document.getElementById("new_stock").value;
        const description = document.getElementById("new_description").value;

        const formData = new FormData();
        formData.append("name", name);
        formData.append("price", price);
        formData.append("category", category);
        formData.append("stock", stock);
        formData.append("description", description);

        fetch("/pk8000/catalog/api/product/create", {
            method: "POST",
            body: formData
        })
            .then(async response => {
                if (response.ok) {
                    alert("Товар добавлен!");
                    location.reload();
                } else {
                    const contentType = response.headers.get("Content-Type");
                    let errorMessage = `Ошибка ${response.status}`;
                    if (contentType && contentType.includes("application/json")) {
                        // это JSON
                        try {
                            const errorJson = await response.json();
                            errorMessage = errorJson.message || JSON.stringify(errorJson);
                        } catch (e) {
                            errorMessage = "Ошибка при разборе JSON-ответа от сервера.";
                        }
                    } else {
                        // это просто текст
                        try {
                            const text = await response.text();
                            if (text.trim()) errorMessage = text;
                        } catch (e) {
                            errorMessage = "Ошибка при получении ответа от сервера.";
                        }
                    }
                    throw new Error(errorMessage);
                }
            })
            .catch(error => {
                console.error("Ошибка при добавлении товара:", error);
                alert("Ошибка при добавлении: " + error.message);
            });
    }


    function deleteProduct(productId) {
        if (!confirm("Удалить этот товар?")) return;

        fetch(`/pk8000/catalog/api/product/delete/${productId}`, {
            method: "DELETE"
        })
            .then(async response => {
                if (response.ok) {
                    alert("Товар удалён.");
                    location.reload();
                } else {
                    const text = await response.text();
                    throw new Error(text);
                }
            })
            .catch(error => {
                console.error("Ошибка при удалении товара:", error);
                alert("Ошибка при удалении: " + error.message);
            });
    }


    window.redirectToCategory = redirectToCategory
    window.openImageModal = openImageModal
    window.closeModal = closeModal
    // window.deleteImage = deleteImage
    window.saveProduct = saveProduct
    window.addNewProduct = addNewProduct
    window.deleteProduct = deleteProduct

});
