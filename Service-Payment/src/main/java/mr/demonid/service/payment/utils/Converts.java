package mr.demonid.service.payment.utils;

import lombok.AllArgsConstructor;
import mr.demonid.osc.commons.domain.PaymentStatus;
import mr.demonid.osc.commons.dto.payment.*;
import mr.demonid.service.payment.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class Converts {


    public CardResponse cardToCardResponse(Card card) {
        return new CardResponse(card.getId(), card.getSafeCardNumber(), card.getExpiryDate());
    }

    public Card newCardRequestToCard(NewCardRequest cardRequest, UserEntity userEntity) {
        return new Card(null, cardRequest.getCardNumber(), cardRequest.getExpiryDate(), cardRequest.getCvv(), true, userEntity);
    }

    public PaymentLogResponse logToPaymentLogResponse(PaymentLog paymentLog, String cardNumber) {
        return new PaymentLogResponse(
                paymentLog.getOrderId(),
                paymentLog.getUserId(),
                paymentLog.getPaymentMethodId(),
                cardNumber,
                paymentLog.getAmount(),
                paymentLog.getStatus(),
                paymentLog.getCreatedAt()
        );
    }

    public PaymentMethodResponse paymentMethodToResponse(PaymentMethod method) {
        return new PaymentMethodResponse(method.getId(), method.getName(), method.isSupportsCards());
    }

    /*
    OrderPayment -> PaymentLog
     */
    public PaymentLog orderToPaymentLog(PaymentRequest order) {
        return new PaymentLog(
                null,
                order.getOrderId(),
                order.getUserId(),
                order.getPaymentId(),
                order.getCardId(),
                order.getTotalAmount(),
                PaymentStatus.REQUESTED,
                LocalDateTime.now()
        );
    }

}
