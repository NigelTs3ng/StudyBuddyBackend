package com.studybuddy.backend.response;

import com.studybuddy.backend.entity.Answer;
import com.studybuddy.backend.object.AnswerViewObj;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class AnswerResponse extends BaseResponse {
	private AnswerViewObj answer;
}
