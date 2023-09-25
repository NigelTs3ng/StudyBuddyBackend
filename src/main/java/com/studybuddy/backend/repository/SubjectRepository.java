package com.studybuddy.backend.repository;

import com.studybuddy.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Optional<Subject> findByTitleContainsIgnoreCase(String title);

    List<Subject> findAllByOrderByCreatedDateDesc();
}