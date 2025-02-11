package com.example.notification.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestAcceptReq {
    private UUID requesterId;
    private UUID accepterId;
}
