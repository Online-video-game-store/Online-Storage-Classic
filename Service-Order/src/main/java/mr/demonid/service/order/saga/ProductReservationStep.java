package mr.demonid.service.order.saga;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.catalog.CatalogReserveRequest;
import mr.demonid.service.order.exceptions.SagaStepException;
import mr.demonid.service.order.links.CatalogServiceClient;
import mr.demonid.service.order.utils.Converts;


/**
 * Шаг: резервирование товара на складе.
 */
@AllArgsConstructor
@Log4j2
public class ProductReservationStep implements SagaStep<SagaContext> {

    CatalogServiceClient catalogServiceClient;

    @Override
    public void execute(SagaContext context) throws SagaStepException {
        try {
            CatalogReserveRequest request = Converts.sagaToCatalogReserveRequest(context);
            catalogServiceClient.reserveCatalog(request);
        } catch (FeignException e) {
            throw new SagaStepException(e.contentUTF8());
        }
    }

    @Override
    public void rollback(SagaContext context) {
        log.error("ProductReservationStep.rollback()");
        try {
            catalogServiceClient.unblock(context.getOrderId());
        } catch (FeignException ignored) {}
    }


}
