package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mr.demonid.web.client.dto.*;
import mr.demonid.web.client.dto.payment.CardResponse;
import mr.demonid.web.client.dto.payment.PaymentMethod;
import mr.demonid.web.client.dto.payment.PaymentRequest;
import mr.demonid.web.client.services.OrderService;
import mr.demonid.web.client.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/pk8000/catalog/payment")
public class PaymentController {

    private PaymentService paymentService;
    private OrderService orderService;


    /**
     * Отображает страницу выбора платежной системы
     */
    @GetMapping
    public String showPaymentPage(Model model) {
        List<PaymentMethod> paymentMethods = paymentService.getPaymentMethods();
        List<CardResponse> userCards = paymentService.getCards();
        model.addAttribute("paymentMethods", paymentMethods);
        model.addAttribute("userCards", userCards);
        return "/payment";
    }


    /**
     * Добавление новой банковской карты.
     */
    @PostMapping("/add-card")
    @ResponseBody
    public ResponseEntity<?> addCard(@RequestBody NewCardRequest request) {
        log.info("== addCard");
        return paymentService.addNewCard(request);
    }


    /**
     * Формирование заказа.
     */
    @PostMapping("/process")
    @ResponseBody
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest request) {
        log.info("== processPayment: {}", request);
        return orderService.createOrder(request);
    }


}
