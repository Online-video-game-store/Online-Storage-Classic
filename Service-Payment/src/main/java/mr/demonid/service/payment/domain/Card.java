package mr.demonid.service.payment.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import mr.demonid.service.payment.utils.CardUtil;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cardNumber;

    private String expiryDate;
    private String cvv;

    private boolean used;

    @ManyToOne
    @JoinColumn(name = "user_payment_id")
    private UserEntity userEntity;


    /*
        Возврат номера карты, частично скрытого маской
     */
    public String getSafeCardNumber() {
        if (CardUtil.isCardNumberValid(cardNumber)) {
            String[] s = cardNumber.split(" ");
            s[0] = s[1] = s[2] = "XXXX";
            return String.join(" ", s);
        }
        return "???? ???? ???? ????";
    }


    /*
        Обеспечиваем уникальность для каждого объекта.
        А поскольку номер карты уникален, то учитываем только его.
    */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;
        return cardNumber.equals(card.cardNumber);
    }

    @Override
    public int hashCode() {
        return cardNumber.hashCode();
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + cardNumber + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", cvv='" + cvv + '\'' +
                ", userPayment=" + (userEntity == null ? "[]" : userEntity.getUserId()) +
                '}';
    }
}
