package com.studybuddy.backend.object;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class AnswerViewObj {
	private int id;
	private String tutorName;
	private String content;
	private String updatedDate;
}
