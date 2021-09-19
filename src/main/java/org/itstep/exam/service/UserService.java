package org.itstep.exam.service;

import org.itstep.exam.entity.Role;
import org.itstep.exam.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User getUser(String email);

    User registerUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    List<User> getUsers();

    List<User> getUsersPaged(Integer currentPage, Integer length, Role role);
}
