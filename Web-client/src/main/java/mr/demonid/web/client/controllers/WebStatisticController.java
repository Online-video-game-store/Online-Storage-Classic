package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.osc.commons.dto.catalog.CategoryResponse;
import mr.demonid.web.client.dto.filters.OrderFilter;
import mr.demonid.web.client.dto.logs.OrderStatistic;
import mr.demonid.web.client.dto.orders.OrderResponse;
import mr.demonid.web.client.dto.payment.PaymentMethod;
import mr.demonid.web.client.services.*;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@AllArgsConstructor
@Log4j2
@RequestMapping("/pk8000/catalog")
public class WebStatisticController {

    private ProductServices productServices;
    private OrderService orderService;
    private PaymentService paymentService;
//    private KeycloakUserService keycloakUserService;


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DEVELOPER')")
    @GetMapping("/index/statistics")
    public String statistics(
            @RequestParam(name = "elemsOfPage", defaultValue = "32") int pageSize,
            @RequestParam(name = "pageNo", defaultValue = "0") int currentPage,
            @RequestParam(name = "categoryId", defaultValue = "0") Long categoryId,
            @RequestParam(name = "productName", defaultValue = "") String productName,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            Model model) {

        productName = normalizeProductName(productName);

        model.addAttribute("username", IdnUtil.isAuthenticated() ? IdnUtil.getUserName() : "Хьюстон");
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("productName", productName);

        setCategories(model, categoryId);

        List<PaymentMethod> paymentMethods = paymentService.getPaymentMethods();
        Map<Long, String> payments = new HashMap<>();
        paymentMethods.forEach(e -> payments.put(e.getId(), e.getName()));

        // Создаем выборку очередной страницы и корректируем данные о страницах
        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("createdAt").descending());
        PageDTO<OrderResponse> page = orderService.getAllOrders(new OrderFilter(from, to), pageable);

        List<OrderStatistic> items = page.getContent().stream().map(e -> orderResponseToStatistic(e, payments)).toList();
        model.addAttribute("orderStatistics", items);

        // корректируем данные о страницах
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("elemsOfPage", pageSize);

        return "statistics";
    }

    private String getUserName(UUID userId) {
        String user = IdnUtil.getUserName();
        return user == null ? "Unknown" : user;
    }

    private OrderStatistic orderResponseToStatistic(OrderResponse orderResponse, Map<Long, String> payments) {
        return new OrderStatistic(
                orderResponse.getOrderId(),
                orderResponse.getUserId(),
                getUserName(orderResponse.getUserId()),
                payments.getOrDefault(orderResponse.getPaymentId(), "Unknown"),
                orderResponse.getTotalAmount(),
                orderResponse.getCreatedAt(),
                orderResponse.getStatus());
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
    Нормализация названий.
     */
    private String normalizeProductName(String name) {
        if (name == null || name.isEmpty() || name.trim().isEmpty()) {
            return null;
        }
        return name;
    }


}
