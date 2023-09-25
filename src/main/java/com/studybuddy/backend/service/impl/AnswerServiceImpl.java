package com.studybuddy.backend.service.impl;

import com.studybuddy.backend.entity.Answer;
import com.studybuddy.backend.repository.AnswerRepository;
import com.studybuddy.backend.service.AnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Override
    @Transactional
    public Answer create(int questionId, int tutorId, String content) {
        try {
            Answer answer = Answer.builder()
                    .questionId(questionId)
                    .tutorId(tutorId)
                    .content(content)
                    .build();
            Answer addAnswer = answerRepository.save(answer);
            return addAnswer;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
