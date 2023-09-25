package com.studybuddy.backend.service;

import com.studybuddy.backend.entity.User;

import java.util.List;

public interface UserService {
    User login(String username, String password);

    boolean register(User user);

    boolean checkExistByEmail(String mail);

    User findByEmail(String email);

    List<User> findTutors();

    String findTutorNameById(int tutorId);
}
