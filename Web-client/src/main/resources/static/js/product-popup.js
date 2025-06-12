document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("product-modal");
    const modalImage = document.getElementById("modal-image");
    const modalTitle = document.getElementById("modal-title");
    const modalPrice = document.getElementById("modal-price");
    const modalDescription = document.getElementById("modal-description");
    const modalCategory = document.getElementById("modal-category");
    const closeModal = document.querySelector(".close-modal");

    let currentImageIndex = 0;
    let currentImages = [];

    // Вешаем обработчик только на блок с изображением
    document.querySelectorAll('.product-item .product-image-container').forEach(imageContainer => {
        imageContainer.addEventListener('click', function (event) {
            event.stopPropagation(); // Не даём всплыть событию

            const item = this.closest('.product-item');

            const productName = item.dataset.name;
            const productPrice = item.dataset.price;
            const productDesc = item.querySelector('.product-description').textContent;
            const category = document.querySelector('.category-button.active-category span')?.textContent || 'Без категории';

            const images = this.querySelectorAll('img');
            currentImages = Array.from(images).map(img => img.getAttribute('src'));
            currentImageIndex = 0;

            modalTitle.textContent = productName;
            modalPrice.textContent = productPrice;
            modalDescription.textContent = productDesc;
            modalCategory.textContent = category;
            modalImage.src = currentImages[currentImageIndex];
            modal.classList.add("show");
        });
    });

    closeModal.addEventListener('click', () => {
        modal.classList.remove("show");
    });

    document.getElementById("prev-image").addEventListener('click', () => {
        currentImageIndex = (currentImageIndex - 1 + currentImages.length) % currentImages.length;
        modalImage.src = currentImages[currentImageIndex];
    });

    document.getElementById("next-image").addEventListener('click', () => {
        currentImageIndex = (currentImageIndex + 1) % currentImages.length;
        modalImage.src = currentImages[currentImageIndex];
    });

    window.onclick = function (event) {
        if (event.target === modal) {
            modal.classList.remove("show");
        }
    };
});

