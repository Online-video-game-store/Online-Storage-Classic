package mr.demonid.service.order.exceptions;

import lombok.Getter;


@Getter
public abstract class BaseOrderException extends RuntimeException {

    private final String title;

    public BaseOrderException(String title, String message) {
        super(message);
        this.title = title;
    }
}
