package mr.demonid.service.order.links;

import mr.demonid.osc.commons.dto.payment.PaymentRequest;
import mr.demonid.service.order.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;


@FeignClient(name = "PAYMENT-SERVICE", configuration = FeignClientConfig.class)
public interface PaymentServiceClient {

    @PostMapping("/pk8000/api/payment/transfer")
    ResponseEntity<?> transfer(@RequestBody PaymentRequest request);

    @PostMapping("/pk8000/api/payment/rollback")
    ResponseEntity<?> rollback(@RequestBody UUID order);

}
