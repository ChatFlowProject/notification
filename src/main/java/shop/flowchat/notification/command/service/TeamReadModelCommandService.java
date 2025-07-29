package shop.flowchat.notification.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.flowchat.notification.domain.category.CategoryReadModel;
import shop.flowchat.notification.domain.channel.ChannelReadModel;
import shop.flowchat.notification.domain.team.TeamMemberReadModel;
import shop.flowchat.notification.domain.team.TeamReadModel;
import shop.flowchat.notification.event.payload.CategoryEventPayload;
import shop.flowchat.notification.event.payload.ChannelEventPayload;
import shop.flowchat.notification.event.payload.TeamEventPayload;
import shop.flowchat.notification.event.payload.TeamInitializationPayload;
import shop.flowchat.notification.event.payload.TeamMemberEventPayload;
import shop.flowchat.notification.infrastructure.repository.category.CategoryReadModelRepository;
import shop.flowchat.notification.infrastructure.repository.channel.ChannelReadModelRepository;
import shop.flowchat.notification.infrastructure.repository.team.TeamMemberReadModelRepository;
import shop.flowchat.notification.infrastructure.repository.team.TeamReadModelRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamReadModelCommandService {
    private final TeamReadModelRepository teamRepository;
    private final TeamMemberReadModelRepository teamMemberRepository;
    private final CategoryReadModelRepository categoryRepository;
    private final ChannelReadModelRepository channelRepository;
    private final MessageReadModelCommandService messageCommandService;
    private final MentionCommandService mentionCommandService;

    // Team
    public void createTeam(TeamInitializationPayload payload) {
        if (!teamRepository.existsById(payload.teamPayload().id())) {
            teamRepository.save(TeamReadModel.create(payload.teamPayload()));
            createTeamMember(payload.memberPayload());
            createCategory(payload.categoryPayload());
            createChannel(payload.channelPayload());
        }
    }

    public void updateTeam(TeamEventPayload payload) {
        teamRepository.findById(payload.id())
                .ifPresentOrElse(
                        existingTeam -> {
                            if (existingTeam.isUpdated(payload.timestamp())) {
                                existingTeam.update(payload);
                            }
                        },
                        () -> {
                            TeamReadModel team = TeamReadModel.create(payload);
                            team.update(payload);
                            teamRepository.save(team);
                        }
                );
    }

    public void deleteTeam(TeamEventPayload payload) {
        teamRepository.findById(payload.id()).ifPresent(team -> {
            teamRepository.delete(team);
            teamMemberRepository.deleteByTeamId(team.getId());

            List<CategoryReadModel> categories = categoryRepository.findAllByTeamId(team.getId());
            List<Long> categoryIds = categories.stream().map(CategoryReadModel::getId).toList();

            List<ChannelReadModel> channels = channelRepository.findAllByCategoryIdIn(categoryIds);
            List<UUID> chatIds = channels.stream().map(ChannelReadModel::getChatId).toList();

            chatIds.forEach(this::deleteMessage);
            channelRepository.deleteByCategoryIds(categoryIds);
            categoryRepository.deleteAll(categories);
        });
    }


    // Team Member
    public void createTeamMember(TeamMemberEventPayload payload) {
        if (!teamMemberRepository.existsByTeamIdAndMemberId(payload.teamId(), payload.memberId())) {
            teamMemberRepository.save(TeamMemberReadModel.create(payload));
        }
    }

    public void deleteTeamMember(TeamMemberEventPayload payload) {
        TeamMemberReadModel member = teamMemberRepository.findByTeamIdAndMemberId(payload.teamId(), payload.memberId());
        if (member != null) {
            teamMemberRepository.delete(member);
        }
    }

    // Category
    public void createCategory(CategoryEventPayload payload) {
        CategoryReadModel category = CategoryReadModel.create(payload);
        categoryRepository.save(category);
    }

    public void deleteCategory(CategoryEventPayload payload) {
        categoryRepository.findById(payload.id()).ifPresent(category -> {
            channelRepository.findAllByCategoryId(category.getId())
                    .forEach(channel -> deleteMessage(channel.getChatId()));
            categoryRepository.delete(category);
        });
    }

    // Channel
    public void createChannel(ChannelEventPayload payload) {
        if (!channelRepository.existsById(payload.id())) {
            channelRepository.save(ChannelReadModel.create(payload));
        }
    }

    public void updateChannel(ChannelEventPayload payload) {
        channelRepository.findById(payload.id())
                .ifPresentOrElse(
                        existingChannel -> {
                            if (existingChannel.needsUpdate(payload.timestamp())) {
                                existingChannel.update(payload);
                            }
                        },
                        () -> {
                            ChannelReadModel channel = ChannelReadModel.create(payload);
                            channel.update(payload);
                            channelRepository.save(channel);
                        }
                );
    }

    public void deleteChannel(ChannelEventPayload payload) {
        channelRepository.findById(payload.id()).ifPresent(channelRepository::delete);
        deleteMessage(payload.chatId());
    }

    private void deleteMessage(UUID chatId) {
        List<Long> messageIds = messageCommandService.deleteMessages(chatId);
        if (messageIds.isEmpty()) return;
        mentionCommandService.deleteMentions(messageIds);
    }
}
