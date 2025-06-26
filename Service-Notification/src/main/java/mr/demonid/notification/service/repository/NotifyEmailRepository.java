package mr.demonid.notification.service.repository;

import mr.demonid.notification.service.domain.NotifyTargetEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Интерфейс работы с базой данных уведомлений.
 */
@Repository
public interface NotifyEmailRepository extends JpaRepository<NotifyTargetEmail, Long> {

    List<NotifyTargetEmail> findNotifyTargetEmailByUserId(UUID userId);

}
