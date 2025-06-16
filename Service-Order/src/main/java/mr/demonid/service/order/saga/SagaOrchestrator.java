package mr.demonid.service.order.saga;

import mr.demonid.service.order.exceptions.SagaStepException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Оркестратор.
 */
public class SagaOrchestrator<T> {

    private final List<SagaStep<T>> steps = new ArrayList<>();

    public void addStep(SagaStep<T> step) {
        steps.add(step);
    }

    /**
     * Выполнение заданной последовательности и в случае ошибки на каком-то
     * этапе - автоматический откат всех проделанных действий.
     * @param context Контекст данных по операции.
     */
    public void execute(T context) throws SagaStepException {
        Deque<SagaStep<T>> executedSteps = new ArrayDeque<>();
        try {
            for (SagaStep<T> step : steps) {
                step.execute(context);
                executedSteps.push(step);       // помещаем в стек, на случай отката
            }
        } catch (SagaStepException e) {
            while (!executedSteps.isEmpty()) {
                executedSteps.pop().rollback(context);
            }
            throw e;
        }
    }


}
