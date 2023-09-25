package com.studybuddy.backend.service;

import com.studybuddy.backend.entity.Subject;

import java.util.List;

public interface SubjectService {
	Subject create(String title);

	Subject edit(int id, String title);

	void remove(int subjectId);

	boolean checkExistSubject(String title);

	Subject findById(int id);

	List<Subject> findAll();
}
