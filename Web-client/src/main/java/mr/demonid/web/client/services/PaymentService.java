package mr.demonid.web.client.services;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.NewCardRequest;
import mr.demonid.web.client.dto.payment.PaymentMethod;
import mr.demonid.web.client.dto.payment.CardResponse;
import mr.demonid.web.client.dto.payment.CreateCardRequest;
import mr.demonid.web.client.exceptions.CreateCardException;
import mr.demonid.web.client.links.PaymentServiceClient;
import mr.demonid.web.client.utils.FeignErrorUtils;
import mr.demonid.web.client.utils.IdnUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class PaymentService {

    private PaymentServiceClient paymentServiceClient;
    private IdnUtil idnUtil;


    public List<PaymentMethod> getPaymentMethods() {
        try {
            return paymentServiceClient.getPayments().getBody();
        } catch (FeignException e) {
            log.error("PaymentService.getPaymentMethods(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return List.of();
    }

    public List<CardResponse> getCards() {
        try {
            UUID userId = idnUtil.getUserId();
            if (userId != null) {
                return paymentServiceClient.getCards(userId).getBody();
            }
        } catch (FeignException e) {
            log.error("PaymentService.getCards(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return List.of();
    }

    public ResponseEntity<?> addNewCard(NewCardRequest cardRequest) {
        if (cardRequest.getCardNumber().isEmpty()) {
            throw new CreateCardException("Некорректный номер карты");
        }
        UUID userId = idnUtil.getUserId();
        if (userId == null) {
            throw new CreateCardException("Пользователь не авторизирован");
        }
        try {
            log.info("-- Adding new card user: {}", userId);
            return paymentServiceClient.addCard(new CreateCardRequest(userId, cardRequest.getCardNumber(), cardRequest.getExpiryDate(), cardRequest.getCvv()));
        } catch (FeignException e) {
            return FeignErrorUtils.toResponse(e, "Ошибка микросервиса Payment-Service");
        }
    }


}
