package mr.demonid.service.catalog.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.catalog.domain.ProductCategoryEntity;
import mr.demonid.service.catalog.domain.ProductEntity;
import mr.demonid.service.catalog.dto.ProduceFilter;
import mr.demonid.service.catalog.dto.ProductRequest;
import mr.demonid.service.catalog.dto.ProductResponse;
import mr.demonid.service.catalog.exceptions.*;
import mr.demonid.service.catalog.repositories.CategoryRepository;
import mr.demonid.service.catalog.repositories.ProductRepository;
import mr.demonid.service.catalog.services.filters.ProductSpecification;
import mr.demonid.service.catalog.utils.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
@Log4j2
public class ProductAdminService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Mappers mappers;

    @Value("${app.images-path}")
    private String imagesPath;
    @Value("${app.images-temp}")
    private String tempPath;

    /**
     * Возвращает постраничный список товаров для админки.
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(ProduceFilter produceFilter, Pageable pageable) {
        Page<ProductEntity> items = productRepository.findAll(ProductSpecification.filter(produceFilter, true), pageable);
        return items.map(mappers::entityToProductResponse);
    }

    /**
     * Добавление нового товара.
     */
    @Transactional
    public void createProduct(ProductRequest product) {
        try {
            if (product == null || product.getCategory() == null) {
                throw new Exception("Поступили некорректные данные");
            }
            product.setProductId(null);
            ProductCategoryEntity category = categoryRepository.findById(product.getCategory()).orElse(null);
            if (category == null) {
                throw new Exception("Неверная категория товара");
            }
            ProductEntity productEntity = mappers.requestProductToEntity(product, category);
            productRepository.save(productEntity);
        } catch (Exception e) {
            throw new CreateProductException(e.getMessage());
        }
    }

    /**
     * Обновление данных о товаре.
     */
    @Transactional
    public void updateProduct(ProductRequest product) {
        try {
            if (product == null || product.getProductId() == null || product.getCategory() == null) {
                throw new Exception("Поступили некорректные данные");
            }
            ProductCategoryEntity category = categoryRepository.findById(product.getCategory()).orElse(null);
            ProductEntity productEntity = productRepository.findById(product.getProductId()).orElse(null);
            if (productEntity == null || category == null) {
                throw new Exception("Данные о продукте или категории не найдены в БД");
            }
            productEntity.setName(product.getName());
            productEntity.setPrice(product.getPrice());
            productEntity.setStock(product.getStock());
            productEntity.setDescription(product.getDescription());
            productEntity.setCategory(category);
            productRepository.save(productEntity);
        } catch (Exception e) {
            throw new UpdateProductException(e.getMessage());
        }
    }

    /**
     * Удаление товара из БД.
     * @param productId Продукт.
     */
    @Transactional
    public void deleteProduct(Long productId) {
        try {
            productRepository.deleteById(productId);
            deleteDirectory(Paths.get(imagesPath, productId.toString()));
        } catch (Exception e) {
            throw new DeleteProductException(e.getMessage());
        }
    }

    /**
     * Обновление существующего, или добавление нового изображения.
     * @param productId       Продукт.
     * @param file            Файл от клиента.
     * @param replaceFileName Имя существующего файла, или null.
     */
    @Transactional
    public void updateImage(Long productId, MultipartFile file, String replaceFileName) {
        try {
            if (file.isEmpty()) {
                throw new Exception("Поступили некорректные данные");
            }
            ProductEntity productEntity = productRepository.findById(productId).orElse(null);
            if (productEntity == null) {
                throw new UpdateImageException("Товар не найден");
            }
            // проверяем, существует ли заменяемый файл
            if (replaceFileName != null && !replaceFileName.isEmpty()) {
                if (!productEntity.getImageFiles().contains(replaceFileName)) {
                    throw new UpdateImageException("Файл '" + replaceFileName + "' не найден");
                }
            }
            // сохраняем во временную папку
            Path tmpFile = loadToTempDirectory(file);
            // переносим в pics
            String finalFileName = replaceFileName == null ? file.getOriginalFilename() : replaceFileName.isBlank() ? file.getOriginalFilename() : replaceFileName;
            moveToImageDirectory(tmpFile, Paths.get(imagesPath, productId.toString()).toString(), finalFileName);

            // корректируем БД
            if (replaceFileName == null || replaceFileName.isEmpty()) {
                productEntity.getImageFiles().add(finalFileName);
                productRepository.save(productEntity);
            }
        } catch (Exception e) {
            throw new UpdateImageException(e.getMessage());
        }
    }

    /**
     * Удаление изображения.
     * @param productId Продукт.
     * @param fileName  Имя удаляемого файла. Удаляет как с БД, так и с диска.
     */
    @Transactional
    public void deleteImage(Long productId, String fileName) {
        try {
            if (productId == null || fileName == null || fileName.isEmpty()) {
                throw new Exception("Некорректные данные");
            }
            ProductEntity productEntity = productRepository.findById(productId).orElse(null);
            if (productEntity == null) {
                throw new Exception("Товар не найден");
            }
            if (!productEntity.getImageFiles().contains(fileName)) {
                throw new Exception("Товар не содержит такого изображения");
            }
            // удаляем из БД
            productEntity.getImageFiles().remove(fileName);
            productRepository.save(productEntity);

            // удаляем файл с носителя
            Path imgFile = Paths.get(imagesPath, productId.toString(), fileName).toAbsolutePath().normalize();
            Files.deleteIfExists(imgFile);

        } catch (Exception e) {
            throw new DeleteImageException(e.getMessage());
        }
    }


    /*
    Перемещает файл с временной папки в каталог изображений
     */
    private void moveToImageDirectory(Path src, String destPath, String destFileName) {
        try {
            Path picsDir = Paths.get(destPath).toAbsolutePath().normalize();
            Files.createDirectories(picsDir);
            Path finalFile = picsDir.resolve(Objects.requireNonNull(destFileName));
            Files.move(src, finalFile, StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            throw new UpdateImageException(e.getMessage());
        }
    }

    /*
    Сохраняет пришедший файл во временную папку
     */
    private Path loadToTempDirectory(MultipartFile file) {
        // сохраняем во временную папку
        try {
            Path tmpDir = Paths.get(tempPath).toAbsolutePath().normalize();
            Files.createDirectories(tmpDir);
            Path tmpFile = tmpDir.resolve(UUID.randomUUID() + "_" + file.getOriginalFilename());
            file.transferTo(tmpFile.toFile());
            // проверяем MIME-тип
            String contentType = Files.probeContentType(tmpFile);
            if (contentType == null || !contentType.startsWith("image/")) {
                // удаляем и возвращаем ошибку
                Files.deleteIfExists(tmpFile);
                throw new Exception("Файл '" + tmpFile.toFile() + "' не является изображением");
            }
            return tmpFile;
        } catch (Exception e) {
            throw new UpdateImageException(e.getMessage());
        }
    }

    /*
     * Удаление каталога со всем содержимым.
     */
    private void deleteDirectory(Path path) {
        try (Stream<Path> stream = Files.walk(path)) {
            stream.sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            log.error("Не удалось удалить '{}': {}", p, e);
                        }
                    });
        } catch (IOException e) {
            log.error("Ошибка удаления: {}", e.getMessage());
        }
    }


}
