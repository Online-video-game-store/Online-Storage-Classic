package mr.demonid.service.order.exceptions;

public class CreateOrderException extends BaseOrderException {

    public CreateOrderException(String message) {
        super("Ошибка создания заказа", message);
    }
}
