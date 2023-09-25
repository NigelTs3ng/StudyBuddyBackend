package com.studybuddy.backend.controller;

import com.google.common.base.Strings;
import com.studybuddy.backend.entity.Subject;
import com.studybuddy.backend.request.SubjectRequest;
import com.studybuddy.backend.response.SubjectResponse;
import com.studybuddy.backend.service.SubjectService;
import com.studybuddy.backend.utils.ValidtionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/app/subjects")
@Slf4j
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<SubjectResponse> add(@RequestBody SubjectRequest rq) {
        SubjectResponse res = SubjectResponse.builder()
                .code("00")
                .desc("Success")
                .build();
        try {
            if (ValidtionUtils.checkEmptyOrNull(rq.getTitle())) {
                res.setCode("01");
                res.setDesc("Please enter your subject title");
                return ResponseEntity.badRequest().body(res);
            }
            if (subjectService.checkExistSubject(rq.getTitle())) {
                res.setCode("01");
                res.setDesc("This subject has existed");
                return ResponseEntity.badRequest().body(res);
            }
            Subject subject = subjectService.create(rq.getTitle());
            subject.setQuestions(new ArrayList<>());
            res.setSubject(subject);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setCode("99");
            res.setDesc("System error. Please try again");
            return ResponseEntity.internalServerError().body(res);
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntity<SubjectResponse> edit(@RequestBody SubjectRequest rq) {
        SubjectResponse res = SubjectResponse.builder()
                .code("00")
                .desc("Success")
                .build();
        try {
            if (rq == null || rq.getId() == null || Strings.isNullOrEmpty(rq.getTitle())) {
                res.setCode("01");
                res.setDesc("Invalid subject data");
                return ResponseEntity.badRequest().body(res);
            }
            if (subjectService.checkExistSubject(rq.getTitle())) {
                res.setCode("01");
                res.setDesc("This subject has existed");
                return ResponseEntity.badRequest().body(res);
            }
            Subject subject = subjectService.edit(rq.getId(), rq.getTitle());
            res.setSubject(subject);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setCode("99");
            res.setDesc("System error. Please try again");
            return ResponseEntity.internalServerError().body(res);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity<SubjectResponse> delete(@PathVariable(name = "id") Integer id) {
        SubjectResponse res = SubjectResponse.builder()
                .code("00")
                .desc("Success")
                .build();
        try {
            if (id == null || id <= 0) {
                res.setCode("01");
                res.setDesc("Invalid subject data");
                return ResponseEntity.badRequest().body(res);
            }
            Subject subject = subjectService.findById(id);
            if (subject == null) {
                res.setCode("01");
                res.setDesc("This subject has not existed");
                return ResponseEntity.badRequest().body(res);
            }
            subjectService.remove(subject.getId());
            res.setSubject(subject);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setCode("99");
            res.setDesc("System error. Please try again");
            return ResponseEntity.internalServerError().body(res);
        }
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ResponseEntity<SubjectResponse> view(@PathVariable(name = "id") Integer id) {
        SubjectResponse res = SubjectResponse.builder()
                .code("00")
                .desc("Success")
                .build();
        try {
            if (id == null || id <= 0) {
                res.setCode("01");
                res.setDesc("Invalid subject data");
                return ResponseEntity.badRequest().body(res);
            }
            Subject subject = subjectService.findById(id);
            subject.setQuestions(new ArrayList<>());
            res.setSubject(subject);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setCode("99");
            res.setDesc("System error. Please try again");
            return ResponseEntity.internalServerError().body(res);
        }
    }
}
