package mr.demonid.service.payment.services;


import lombok.AllArgsConstructor;
import mr.demonid.osc.commons.dto.PageDTO;
import mr.demonid.osc.commons.dto.payment.PaymentLogResponse;
import mr.demonid.service.payment.domain.PaymentLog;
import mr.demonid.service.payment.repository.PaymentLogRepository;
import mr.demonid.service.payment.services.filters.LogSpecification;
import mr.demonid.service.payment.utils.Converts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class PaymentLogService {

    private PaymentLogRepository paymentLogRepository;
    private CardService cardService;
    private Converts converts;


    public PaymentLog save(PaymentLog paymentLog) {
        return paymentLogRepository.save(paymentLog);
    }


    /**
     * Постраничная выборка всех данных.
     */
    public PageDTO<PaymentLogResponse> findAll(Pageable pageable) {
        Page<PaymentLog> items = paymentLogRepository.findAll(pageable);
        return new PageDTO<>(items.map(e -> converts.logToPaymentLogResponse(e, cardService.getCardNumber(e.getCardNumber()))));
    }


    /**
     * Постраничная выборка всех данных для конкретного пользователя.
     */
    public PageDTO<PaymentLogResponse> findById(UUID userId, Pageable pageable) {
        Page<PaymentLog> items = paymentLogRepository.findAll(LogSpecification.filter(userId), pageable);
        return new PageDTO<>(items.map(e -> converts.logToPaymentLogResponse(e, cardService.getCardNumber(e.getCardNumber()))));
    }


    /**
     * Выборка конкретного заказа.
     */
    public PaymentLogResponse findByOrderId(UUID orderId) {
        PaymentLog item = paymentLogRepository.findByOrderId(orderId);
        return converts.logToPaymentLogResponse(item, cardService.getCardNumber(item.getCardNumber()));
    }

}
