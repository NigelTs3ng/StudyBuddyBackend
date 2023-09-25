package com.studybuddy.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "subject")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    private String title;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    @OrderBy(value = "updatedDate DESC")
    private List<Question> questions;

    @Column(name = "created_date")
    @Builder.Default
    private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

}
