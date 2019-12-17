package com.oauth.service.service;

import com.oauth.service.bean.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User findById(Long id);

    User findByUsername(String username);

    User findByTel(String tel);

    List<User> findAll();

    void delete(Long id);

    List<User> findByName(String name);
}
