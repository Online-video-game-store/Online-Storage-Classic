package mr.demonid.service.order.exceptions;

public class SagaStepException extends BaseOrderException {


    public SagaStepException(String message) {
        super("Ошибка создания заказа", message);
    }

}
