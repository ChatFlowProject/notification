package shop.flowchat.notification.infrastructure.repository.channel;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.flowchat.notification.domain.channel.ChannelMemberReadModel;

public interface ChannelMemberReadModelRepository extends JpaRepository<ChannelMemberReadModel, Long> {
    List<ChannelMemberReadModel> findByChannelId(Long channelId);
}