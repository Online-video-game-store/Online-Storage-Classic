package mr.demonid.service.order.saga;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.payment.PaymentRequest;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.links.PaymentServiceClient;
import mr.demonid.service.order.utils.Converts;

/**
 * Шаг: Оплата заказа.
 */
@AllArgsConstructor
@Log4j2
public class PaymentTransferStep implements SagaStep<SagaContext> {

    private final PaymentServiceClient paymentServiceClient;


    @Override
    public void execute(SagaContext context) throws SagaStepException {
        try {
            PaymentRequest paymentRequest = Converts.sagaToPaymentRequest(context);
            paymentServiceClient.transfer(paymentRequest);
        } catch (FeignException e) {
            throw new SagaStepException(e.contentUTF8());
        }
    }

    @Override
    public void rollback(SagaContext context) {
        log.error("PaymentTransferStep.rollback()");
        try {
            paymentServiceClient.rollback(context.getOrderId());
        } catch (FeignException ignored) {}
    }

}
