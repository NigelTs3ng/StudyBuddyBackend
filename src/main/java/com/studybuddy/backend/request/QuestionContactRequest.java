package com.studybuddy.backend.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionContactRequest {
	private int questionId;
	private int status;
	private int isAllowChat;
	private int isAllowVideoCall;
	private int isAllowVoiceCall;
	private int tutorId;
	private int isRegistChat;
	private int isRegistVideoCall;
	private int isRegistVoiceCall;
}
