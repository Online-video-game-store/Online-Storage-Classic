package mr.demonid.service.order.saga;

import mr.demonid.service.order.exceptions.SagaStepException;


/**
 * Один шаг операции, или её отката.
 * @param <T> Обобщенный тип.
 */
public interface SagaStep<T> {
    void execute(T context) throws SagaStepException;   // Выполнение операции
    void rollback(T context);                           // Откат операции
}
