package com.studybuddy.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "question")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "subject_id")
    private int subjectId;

    @Column(name = "tutor_id")
    private int tutorId;

    @Transient
    private String tutorName;

    private String title;

    private int status;

    @Column(name = "input_detail")
    private String inputDetail;

    @Column(name = "image_detail_url")
    private String imageDetailUrl;

    @Column(name = "is_voice_call")
    private Integer isVoiceCall;

    @Column(name = "is_chat_message")
    private Integer isChatMessage;

    @Column(name = "is_video_call")
    private Integer isVideoCall;

    @Column(name = "created_date")
    @Builder.Default
    private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_date")
    @Builder.Default
    private Timestamp updatedDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "answer_date")
    private Timestamp answerDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @OrderBy(value = "updatedDate DESC")
    private List<Answer> answers;

    @Column(name = "answer_solution_path")
    private String answerSolutionPath;

    @Column(name = "is_register_chat_message")
    private Integer isRegisterChatMessage;

    @Column(name = "is_register_video_call")
    private Integer isRegisterVideoCall;

    @Column(name = "is_register_voice_call")
    private Integer isRegisterVoiceCall;
}
