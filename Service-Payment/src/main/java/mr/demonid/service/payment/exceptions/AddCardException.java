package mr.demonid.service.payment.exceptions;

public class AddCardException extends BasePaymentException {

    private final String message;


    public AddCardException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Ошибка добавления карты: " + message;
    }
}
