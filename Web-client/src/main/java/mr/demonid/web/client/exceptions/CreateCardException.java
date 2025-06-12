package mr.demonid.web.client.exceptions;

public class CreateCardException extends WebClientException {

    private final String message;


    public CreateCardException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Ошибка добавления карты: " + message;
    }
}
