package com.api.common_chat;

import java.util.UUID;

public class ChatMessage {
    private UUID gameId;
    private UUID senderId;
    private String content;
    private ChatChannel channel;
}
