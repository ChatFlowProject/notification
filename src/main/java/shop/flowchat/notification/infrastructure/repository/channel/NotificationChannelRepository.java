package shop.flowchat.notification.infrastructure.repository.channel;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.channel.NotificationChannel;

public interface NotificationChannelRepository extends JpaRepository<NotificationChannel, Long> {
  Optional<NotificationChannel> findByChatId(UUID chatId);

}