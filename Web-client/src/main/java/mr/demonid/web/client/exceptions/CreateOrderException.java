package mr.demonid.web.client.exceptions;


public class CreateOrderException extends WebClientException {

    private final String message;


    public CreateOrderException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Ошибка обновления данных: " + message;
    }
}
