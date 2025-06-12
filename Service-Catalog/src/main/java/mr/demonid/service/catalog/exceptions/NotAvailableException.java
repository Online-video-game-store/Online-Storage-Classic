package mr.demonid.service.catalog.exceptions;

import mr.demonid.service.catalog.dto.CartNeededResponse;

import java.util.List;

/**
 * Исключение на случай отсутствия товара в наличии.
 */
public class NotAvailableException extends CatalogException {

    private String message;


    public NotAvailableException(List<CartNeededResponse> products) {
        format(products);
    }

    @Override
    public String getMessage() {
        return message;
    }

    private void format(List<CartNeededResponse> products) {
        StringBuilder res = new StringBuilder();
        for (CartNeededResponse product : products) {
            res.append("&")
                    .append(product.getName())
                    .append(" [требуется: ").append(product.getRequest())
                    .append(", в наличии: ").append(product.getStock())
                    .append("]");
        }
        message = res.toString();
    }

}
