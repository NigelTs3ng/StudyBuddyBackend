package com.studybuddy.backend.response;

import com.studybuddy.backend.entity.Question;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class QuestionResponse extends BaseResponse {
	private Question card;
}
