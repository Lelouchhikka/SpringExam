package org.itstep.exam.service;

import lombok.SneakyThrows;
import org.itstep.exam.entity.Role;
import org.itstep.exam.entity.User;
import org.itstep.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@EnableWebSecurity
public class UserServiceImpl implements UserService {

    private BCryptPasswordEncoder bcryptPasswordEncoder;

    private UserRepository userRepository;

    @Override
    public User getUser(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User registerUser(User User) {
        User checkUser = userRepository.findUserByEmail(User.getEmail());
        if (Objects.isNull(checkUser)) {
            User.setPassword(bcryptPasswordEncoder.encode(User.getPassword()));
            return userRepository.save(User);
        }
        return null;
    }

    @Override
    public User updateUser(User User) {
        return userRepository.save(User);
    }

    @Override
    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersPaged(Integer currentPage, Integer length, Role role) {
        return null;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(final String email)  {
        User User = userRepository.findUserByEmail(email);
        if (Objects.nonNull(User)) {
            User securityUser = new User(User.getEmail(), User.getPassword(), User.getRoles());
            return (UserDetails) securityUser;
        } else {
            return null;
        }
    }

    @Autowired
    public void setBcryptPasswordEncoder(BCryptPasswordEncoder bcryptPasswordEncoder) {
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
