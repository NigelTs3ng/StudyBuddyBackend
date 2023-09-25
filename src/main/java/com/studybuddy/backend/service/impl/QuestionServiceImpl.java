package com.studybuddy.backend.service.impl;

import com.studybuddy.backend.entity.Question;
import com.studybuddy.backend.repository.QuestionRepository;
import com.studybuddy.backend.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    @Transactional
    public Question create(int subjectId, String filePath, String title, String inputDetail, int regisChat, int regisVoiceCall, int regisVideoCall) {
        try {
            Question question = Question.builder()
                    .title(title)
                    .subjectId(subjectId)
                    .inputDetail(inputDetail)
                    .imageDetailUrl(filePath)
                    .isVoiceCall(0)
                    .isChatMessage(0)
                    .isVideoCall(0)
                    .isRegisterChatMessage(regisChat)
                    .isRegisterVoiceCall(regisVoiceCall)
                    .isRegisterVideoCall(regisVideoCall)
                    .build();
            return questionRepository.save(question);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Question findById(int questionId) {
        try {
            return questionRepository.findById(questionId).orElse(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean update(int questionId, String filePath, String inputText) {
        try {
            Question question = findById(questionId);
            if (question != null) {
                question.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                question.setInputDetail(inputText);
                question.setImageDetailUrl(filePath);
                int update = questionRepository.save(question).getId();
                return update > 0;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    @Transactional
    public void delete(int questionId) {
        try {
            questionRepository.deleteById(questionId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void update(Question question) {
        try {
            question.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            questionRepository.save(question);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
