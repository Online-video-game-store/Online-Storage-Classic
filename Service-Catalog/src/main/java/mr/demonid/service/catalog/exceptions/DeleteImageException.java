package mr.demonid.service.catalog.exceptions;

public class DeleteImageException extends CatalogException {

    private final String message;

    public DeleteImageException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Ошибка удаления файла: " + message;
    }
}
