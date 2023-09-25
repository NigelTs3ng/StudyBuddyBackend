package com.studybuddy.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Entity
@Table(name = "answer")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "tutor_id")
    private int tutorId;

    @Column(name = "content")
    private String content;

    @Column(name = "created_date")
    @Builder.Default
    private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_date")
    @Builder.Default
    private Timestamp updatedDate = new Timestamp(System.currentTimeMillis());
}
