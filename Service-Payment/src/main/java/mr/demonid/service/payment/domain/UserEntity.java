package mr.demonid.service.payment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    private UUID userId;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "userEntity", fetch = FetchType.EAGER)
    Set<Card> cards = new HashSet<>();

    public void addCard(Card card) {
        cards.add(card);
        card.setUserEntity(this);
    }
}
