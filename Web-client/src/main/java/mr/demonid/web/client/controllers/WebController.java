package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.osc.commons.dto.catalog.CategoryResponse;
import mr.demonid.osc.commons.dto.catalog.ProductResponse;
import mr.demonid.web.client.configs.AppConfiguration;
import mr.demonid.web.client.dto.filters.ProductFilter;
import mr.demonid.web.client.services.CartServices;
import mr.demonid.web.client.services.ProductServices;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


@Controller
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/catalog")
public class WebController {

    private AppConfiguration appConfiguration;
    private ProductServices productServices;
    private CartServices cartServices;
    private IdnUtil idnUtil;


    @GetMapping("/index")
    public String index(
        @RequestParam(name = "elemsOfPage", defaultValue = "8") int pageSize,
        @RequestParam(name = "pageNo", defaultValue = "0") int currentPage,
        @RequestParam(name = "categoryId", defaultValue = "0") Long categoryId,
        @RequestParam(name = "productName", defaultValue = "") String productName,
        @RequestParam(name = "minPrice", defaultValue = "0") BigDecimal minPrice,
        @RequestParam(name = "maxPrice", defaultValue = "0") BigDecimal maxPrice,
        Model model) {

        minPrice = normalizePrice(minPrice);
        maxPrice = normalizePrice(maxPrice);
        productName = normalizeProductName(productName);

        log.info("-- index start");
        List<String> scopes = idnUtil.getCurrentUserAuthorities();
        boolean isAuthenticated = idnUtil.isAuthenticated();

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("username", isAuthenticated ? idnUtil.getUserName() : null);
        model.addAttribute("isAdmin", scopes.contains("ROLE_ADMIN") || scopes.contains("ROLE_DEVELOPER"));
        model.addAttribute("cartItemCount", cartServices.getCountItems());

        setCategories(model, categoryId);

        // Создаем выборку очередной страницы и корректируем данные о страницах
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id").ascending());
        PageDTO<ProductResponse> page = productServices.getProductsWithoutEmpty(new ProductFilter(categoryId, productName, minPrice, maxPrice), pageable);
        log.info("Продукты: {}", page.getContent());
        setPageModel(model, page, pageSize, productName, minPrice, maxPrice);

        return "home";
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @GetMapping("/index/setup")
    public String setup(
            @RequestParam(name = "elemsOfPage", defaultValue = "8") int pageSize,
            @RequestParam(name = "pageNo", defaultValue = "0") int currentPage,
            @RequestParam(name = "categoryId", defaultValue = "0") Long categoryId,
            @RequestParam(name = "productName", defaultValue = "") String productName,
            @RequestParam(name = "minPrice", defaultValue = "0") BigDecimal minPrice,
            @RequestParam(name = "maxPrice", defaultValue = "0") BigDecimal maxPrice,
            Model model) {

        minPrice = normalizePrice(minPrice);
        maxPrice = normalizePrice(maxPrice);
        productName = normalizeProductName(productName);

        model.addAttribute("username", idnUtil.isAuthenticated() ? idnUtil.getUserName() : "Хьюстон");

        setCategories(model, categoryId);

        // Создаем выборку очередной страницы и корректируем данные о страницах
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("id").ascending());
        PageDTO<ProductResponse> page = productServices.getAllProducts(new ProductFilter(categoryId, productName, minPrice, maxPrice), pageable);

        setPageModel(model, page, pageSize, productName, minPrice, maxPrice);

        return "manager";
    }


    /*
    Установка категории в модели.
     */
    private void setCategories(Model model, Long categoryId) {
        List<CategoryResponse> categories = productServices.getAllCategories();
        categories.add(0, new CategoryResponse(0L, "All", "Все товары"));
        if (categoryId != null) {
            CategoryResponse curCat = categories.stream().filter(e -> Objects.equals(e.getId(), categoryId)).findFirst().orElse(null);
            if (curCat != null) {
                model.addAttribute("currentCategory", curCat.getName());
            } else {
                model.addAttribute("currentCategory", "All");
            }
        } else {
            model.addAttribute("currentCategory", "All");
        }
        model.addAttribute("categories", categories);
        model.addAttribute("categoryId", categoryId);
    }

    /*
    Установка очередной выборки данных страницы
     */
    private void setPageModel(Model model, PageDTO<ProductResponse> page, int pageSize, String productName, BigDecimal minPrice, BigDecimal maxPrice) {
        // Задаем выборку очередной страницы
        List<ProductResponse> products = page.getContent();
        String gateway = appConfiguration.getGatewayUrl();
        List<ProductResponse> normalized = products.stream()
                .peek(p -> {
                    List<String> fixedUrls = p.getImageUrls().stream()
                            .map(url -> url.replace("\\", "/"))
                            .map(url -> gateway + url)
                            .toList();

                    p.setImageUrls(fixedUrls);
                })
                .toList();
        model.addAttribute("products", normalized);

        // корректируем данные о страницах
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("elemsOfPage", pageSize);
        model.addAttribute("productName", productName);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
    }


    /*
    Нормализация цены.
     */
    private BigDecimal normalizePrice(BigDecimal n) {
        if (n == null || n.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        return n;
    }

    /*
    Нормализация названий.
     */
    private String normalizeProductName(String name) {
        if (name == null || name.isEmpty() || name.trim().isEmpty()) {
            return null;
        }
        return name;
    }

}
