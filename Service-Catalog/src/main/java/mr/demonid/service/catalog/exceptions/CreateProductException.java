package mr.demonid.service.catalog.exceptions;

public class CreateProductException extends CatalogException {
    private final String message;

    public CreateProductException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Ошибка добавления нового товара: " + message;
    }

}
