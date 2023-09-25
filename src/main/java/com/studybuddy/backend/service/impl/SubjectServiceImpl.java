package com.studybuddy.backend.service.impl;

import com.studybuddy.backend.entity.Subject;
import com.studybuddy.backend.repository.SubjectRepository;
import com.studybuddy.backend.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    @Transactional
    public Subject create(String title) {
        try {
            int currentUserid = authenticationService.extractUserIdLogin();
            Subject subject = Subject.builder()
                    .title(title)
                    .userId(currentUserid)
                    .build();
            Subject addSubject = subjectRepository.save(subject);
            return addSubject;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Transactional
    public Subject edit(int id, String title) {
        try {
            Subject subject = subjectRepository.findById(id).orElse(null);
            if (subject == null) {
                return null;
            }
            subject.setTitle(title);
            Subject addSubject = subjectRepository.save(subject);
            return addSubject;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Transactional
    public void remove(int subjectId) {
        try {
            subjectRepository.deleteById(subjectId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkExistSubject(String title) {
        try {
            return subjectRepository.findByTitleContainsIgnoreCase(title).isPresent();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public Subject findById(int id) {
        try {
            Subject subject = subjectRepository.findById(id).orElse(null);
            return subject;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> findAll() {
        List<Subject> subjects = new ArrayList<>();
        try {
            subjects = subjectRepository.findAllByOrderByCreatedDateDesc();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return subjects;
    }
}
