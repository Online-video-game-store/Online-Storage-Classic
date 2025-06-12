package mr.demonid.service.catalog.services;

import lombok.AllArgsConstructor;
import mr.demonid.service.catalog.domain.ProductCategoryEntity;
import mr.demonid.service.catalog.repositories.CategoryRepository;
import mr.demonid.store.commons.dto.CategoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Слой сервиса по работе с категориями товаров.
 */
@Service
@Transactional
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    /**
     * Добавляет в БД новую категорию.
     */
    @Transactional
    public void createCategory(String name, String description) {
        if (categoryRepository.findByName(name) == null) {
            categoryRepository.save(new ProductCategoryEntity(name, description));
        }
    }

    /**
     * Возвращает список всех доступных категорий.
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        List<ProductCategoryEntity> entities = categoryRepository.findAll();
        if (entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities.stream().map(e -> new CategoryResponse(e.getId(), e.getName(), e.getDescription())).toList();
    }


}