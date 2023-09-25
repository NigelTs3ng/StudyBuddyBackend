package com.studybuddy.backend.response;

import com.studybuddy.backend.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class FindTutorResponse extends BaseResponse {
    private List<User> tutors;
}
