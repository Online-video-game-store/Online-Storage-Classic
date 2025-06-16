package mr.demonid.service.payment.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.domain.PaymentStatus;
import mr.demonid.osc.commons.dto.payment.PaymentRequest;
import mr.demonid.service.payment.domain.PaymentLog;
import mr.demonid.service.payment.exceptions.ProceedPaymentException;
import mr.demonid.service.payment.utils.Converts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Math.random;


/**
 * Сервис непосредственно по оплате заказов.
 */
@Service
@AllArgsConstructor
@Log4j2
public class PaymentService {

    private Converts converts;
    private PaymentLogService paymentLogService;


    /**
     * Проведение оплаты заказа.
     */
    @Transactional
    public void doPay(PaymentRequest request) {
        log.info("-- Payment started: {}", request);
        PaymentLog log = converts.orderToPaymentLog(request);
        log.setStatus(PaymentStatus.REQUESTED);

        try {
            log = paymentLogService.save(log);
            // проводим оплату
            // ...

        } catch (Exception e) {
            throw new ProceedPaymentException("Ошибка оплаты. Нет доступа к БД.");
        }
        // TODO: добавь оплату
        if (random() < 0.3) {
            log.setStatus(PaymentStatus.REJECTED);
            paymentLogService.save(log);
            throw new ProceedPaymentException("Недостаточно средств на счете.");
        }
        // если всё успешно, то меняем статус
        log.setStatus(PaymentStatus.APPROVED);
        paymentLogService.save(log);
    }


}
