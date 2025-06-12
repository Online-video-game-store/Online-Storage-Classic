package mr.demonid.service.catalog.exceptions;

public class DeleteProductException extends CatalogException {

    private final String message;

    public DeleteProductException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Ошибка удаления товара: " + message;
    }

}
