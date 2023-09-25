package com.studybuddy.backend.response;

import com.studybuddy.backend.entity.Subject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class SubjectResponse extends BaseResponse {
	private Subject subject;
}
