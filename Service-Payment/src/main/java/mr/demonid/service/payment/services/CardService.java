package mr.demonid.service.payment.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.osc.commons.dto.payment.CardResponse;
import mr.demonid.osc.commons.dto.payment.NewCardRequest;
import mr.demonid.service.payment.domain.Card;
import mr.demonid.service.payment.domain.UserEntity;
import mr.demonid.service.payment.exceptions.AddCardException;
import mr.demonid.service.payment.repository.CardRepository;
import mr.demonid.service.payment.repository.UserEntityRepository;
import mr.demonid.service.payment.utils.CardUtil;
import mr.demonid.service.payment.utils.Converts;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Log4j2
public class CardService {

    private UserEntityRepository userEntityRepository;
    private CardRepository cardRepository;
    private Converts converts;


    /**
     * Возвращает список банковских карт пользователя.
     * Номера карт частично скрыты.
     *
     * Прим. Поскольку карт у пользователя не может быть много, выбираем из БД все
     * существующие и затем просто отфильтровываем недействительные.
     */
    public List<CardResponse> getCards(UUID userId) {
        List<CardResponse> cards = new ArrayList<>();

        UserEntity userEntity = userEntityRepository.findById(userId).orElse(null);
        if (userEntity != null) {
            Set<Card> c = userEntity.getCards();
            cards = c.stream().filter(Card::isUsed).map(converts::cardToCardResponse).toList();
        }
        return cards;
    }


    /**
     * Возвращает номер карты по её ID.
     * Номер частично скрыт маской.
     */
    public String getCardNumber(Long cardId) {
        Card c = null;
        if (cardId != null) {
            c = cardRepository.findById(cardId).orElse(null);
        }
        return c == null ? "" : c.getSafeCardNumber();
    }


    /**
     * Привязка банковской карты к пользователю.
     * В случае ошибки кидает исключение для глобального обработчика.
     */
    public void addCard(NewCardRequest cardRequest) {
        UserEntity userEntity;

        if (!CardUtil.isCardNumberValid(cardRequest.getCardNumber())
                || !CardUtil.isExpiryDateValid(cardRequest.getExpiryDate())
                || !CardUtil.isCvvValid(cardRequest.getCvv())) {
            throw new AddCardException("Неверный формат данных.");
        }

        if (cardRepository.existsByCardNumber(cardRequest.getCardNumber())) {
            throw new AddCardException("Карта уже существует.");
        }

        try {
            Optional<UserEntity> userPayment = userEntityRepository.findById(cardRequest.getUserId());
            userEntity = userPayment.orElseGet(() -> new UserEntity(cardRequest.getUserId(), new HashSet<>()));
            userEntity.addCard(converts.newCardRequestToCard(cardRequest, userEntity));
            userEntityRepository.save(userEntity);
        } catch (Exception e) {
            throw new AddCardException(e.getMessage());
        }
    }


    /**
     * Переводит карту в разряд недействительных.
     */
    public void blockCard(UUID userId, Long cardId) {
        Optional<Card> opt = cardRepository.findById(cardId);
        if (opt.isPresent()) {
            Card card = opt.get();
            if (card.getUserEntity().getUserId().equals(userId)) {
                card.setUsed(false);
                cardRepository.save(card);
                log.info("Card {} blocked", cardId);
            } else {
                log.error("The card {} has a different owner", cardId);
            }
        } else {
            log.error("Card {} not found", cardId);
        }
    }
}
