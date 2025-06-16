package mr.demonid.service.payment.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.osc.commons.dto.payment.*;
import mr.demonid.service.payment.services.CardService;
import mr.demonid.service.payment.services.PaymentLogService;
import mr.demonid.service.payment.services.PaymentMethodService;
import mr.demonid.service.payment.services.PaymentService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/pk8000/api/payment")
@AllArgsConstructor
@Log4j2
public class ApiController {

    private CardService cardService;
    private PaymentMethodService paymentMethodService;
    private PaymentLogService paymentLogService;
    private PaymentService paymentService;



    /**
     * Проведение оплаты.
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @PostMapping("/transfer")
    public ResponseEntity<?> proceed(@RequestBody PaymentRequest request) {
        paymentService.doPay(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Отмена платежа.
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @PostMapping("/rollback")
    public ResponseEntity<?> rollback(@RequestBody UUID order) {
        // отменяем платеж
        // ...
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает список банковских карт пользователя.
     * В целях безопасности номера карт частично скрыты маской.
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @GetMapping("/get-cards")
    public ResponseEntity<List<CardResponse>> getCards(@RequestParam UUID userId) {
        return ResponseEntity.ok(cardService.getCards(userId));
    }

    /**
     * Добавление новой банковской карты пользователю.
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @PostMapping("/add-card")
    public ResponseEntity<?> addCard(@RequestBody NewCardRequest card) {
        cardService.addCard(card);
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает список доступных операций оплаты.
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @GetMapping("/get-payments")
    public ResponseEntity<List<PaymentMethodResponse>> getPayments() {
        List<PaymentMethodResponse> paymentMethods = paymentMethodService.getAllMethods();
        return ResponseEntity.ok(paymentMethods);
    }

    /**
     * Постраничный возврат истории операций пользователя.
     */
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'DEVELOPER')")
    @GetMapping("/get-history")
    public ResponseEntity<PageDTO<PaymentLogResponse>> getHistory(@RequestParam UUID userId, @RequestParam Pageable pageable) {
        return ResponseEntity.ok(paymentLogService.findById(userId, pageable));
    }

    /**
     * Постраничная выборка всех операций.
     * Только для администраторов и разработчика.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @GetMapping("/get-all-history")
    public ResponseEntity<PageDTO<PaymentLogResponse>> getAllHistory(@RequestParam Pageable pageable) {
        return ResponseEntity.ok(paymentLogService.findAll(pageable));
    }


}
