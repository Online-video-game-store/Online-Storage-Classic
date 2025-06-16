package mr.demonid.service.payment.exceptions;

import lombok.Getter;


/**
 * Базовый класс эксепшенов.
 */
@Getter
public abstract class BasePaymentException extends RuntimeException {

    public BasePaymentException() {
    }

    public BasePaymentException(String message) {
        super(message);
    }
}
