package mr.demonid.service.catalog.exceptions;

public class UpdateProductException extends CatalogException {

    private final String message;


    public UpdateProductException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Ошибка обновления данных: " + message;
    }
}
