package mr.demonid.web.client.exceptions;

/**
 * Базовый класс исключений для модуля.
 * Пришлось сделать как RuntimeException, иначе требуется
 * делать проброс вплоть до методов контроллера.
 */
public abstract class WebClientException extends RuntimeException {

}
