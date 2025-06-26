package mr.demonid.notification.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


/**
 * Сущность для хранения мыла пользователей.
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class NotifyTargetEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // триггеры уведомления
    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String email;


    public NotifyTargetEmail(UUID userId, String email, NotifyType notificationType) {
        this.userId = userId;
        this.email = email;
    }

}
