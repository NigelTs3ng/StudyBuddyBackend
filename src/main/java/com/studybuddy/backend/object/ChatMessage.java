package com.studybuddy.backend.object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private int questionId;
    private String sender;
    private String content;
}
