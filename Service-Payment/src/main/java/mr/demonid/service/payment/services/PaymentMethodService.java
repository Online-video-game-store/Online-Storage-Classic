package mr.demonid.service.payment.services;


import lombok.AllArgsConstructor;
import mr.demonid.osc.commons.dto.payment.PaymentMethodResponse;
import mr.demonid.service.payment.domain.PaymentMethod;
import mr.demonid.service.payment.repository.PaymentMethodRepository;
import mr.demonid.service.payment.utils.Converts;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentMethodService {

    private PaymentMethodRepository paymentMethodRepository;
    private Converts converts;


    /**
     * Возвращает список доступных способов оплаты.
     */
    public List<PaymentMethodResponse> getAllMethods() {
        List<PaymentMethod> list = paymentMethodRepository.findAll();
        if (!list.isEmpty()) {
            return list.stream().map(converts::paymentMethodToResponse).toList();
        }
        return List.of();
    }

    /**
     * Возвращает способ оплаты по его Id.
     */
    public PaymentMethodResponse getPaymentMethodById(Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        if (paymentMethod.isPresent()) {
            PaymentMethod method = paymentMethod.get();
            return converts.paymentMethodToResponse(method);
        }
        return null;
    }

    /**
     * Добавляет новый метод оплаты в БД
     */
    public PaymentMethodResponse createPaymentMethod(PaymentMethodResponse paymentMethod) {
        PaymentMethod method = new PaymentMethod(null, paymentMethod.getName(), paymentMethod.isSupportsCards());
        PaymentMethod res = paymentMethodRepository.save(method);
        return converts.paymentMethodToResponse(res);
    }


}
