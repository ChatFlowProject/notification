package shop.flowchat.notification.event.payload;


public record TeamInitializationPayload(
        TeamEventPayload teamPayload,
        TeamMemberEventPayload memberPayload,
        CategoryEventPayload categoryPayload,
        ChannelEventPayload channelPayload
) {}