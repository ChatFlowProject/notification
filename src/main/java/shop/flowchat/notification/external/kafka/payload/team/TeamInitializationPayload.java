package shop.flowchat.notification.external.kafka.payload.team;


import shop.flowchat.notification.external.kafka.payload.category.CategoryEventPayload;
import shop.flowchat.notification.external.kafka.payload.channel.ChannelEventPayload;

public record TeamInitializationPayload(
        TeamEventPayload teamPayload,
        TeamMemberEventPayload memberPayload,
        CategoryEventPayload categoryPayload,
        ChannelEventPayload channelPayload
) {}