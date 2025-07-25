package shop.flowchat.notification.infrastructure.repository.channel;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.channel.ChannelReadModel;

public interface ChannelReadModelRepository extends JpaRepository<ChannelReadModel, Long> {
  Optional<ChannelReadModel> findByChatId(UUID chatId);

}