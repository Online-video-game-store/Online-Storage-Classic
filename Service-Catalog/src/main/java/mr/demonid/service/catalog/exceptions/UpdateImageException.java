package mr.demonid.service.catalog.exceptions;

public class UpdateImageException extends CatalogException {

    private final String message;

    public UpdateImageException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Ошибка обновления изображения: " + message;
    }

}
