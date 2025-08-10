package shop.flowchat.notification.infrastructure.repository.channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.flowchat.notification.common.dto.ChannelContextDto;
import shop.flowchat.notification.domain.channel.ChannelReadModel;

public interface ChannelReadModelRepository extends JpaRepository<ChannelReadModel, Long> {
    @Query("""
        SELECT new shop.flowchat.notification.common.dto.ChannelContextDto(c, ca, t)
        FROM ChannelReadModel c
        JOIN CategoryReadModel ca ON c.categoryId = ca.id
        JOIN TeamReadModel t ON ca.teamId = t.id
        WHERE c.chatId = :chatId
    """)
    ChannelContextDto findChannelCategoryTeamByChatId(@Param("chatId") UUID chatId);

    @Query("""
        SELECT new shop.flowchat.notification.common.dto.ChannelContextDto(c, ca, t)
        FROM ChannelReadModel c
        JOIN CategoryReadModel ca ON c.categoryId = ca.id
        JOIN TeamReadModel t ON ca.teamId = t.id
        WHERE c.chatId IN :chatIds
    """)
    List<ChannelContextDto> findAllChannelCategoryTeamByChatIds(@Param("chatIds") List<UUID> chatIds);

    Optional<ChannelReadModel> findByChatId(UUID chatId);

    List<ChannelReadModel> findAllByCategoryIdIn(List<Long> categoryIds);

    List<ChannelReadModel> findAllByCategoryId(Long categoryId);

    @Modifying
    @Query("DELETE FROM ChannelReadModel c WHERE c.categoryId IN :categoryIds")
    void deleteByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
}