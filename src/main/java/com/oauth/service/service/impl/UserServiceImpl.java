package com.oauth.service.service.impl;

import com.oauth.service.bean.User;
import com.oauth.service.mapper.UserMapper;
import com.oauth.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Override
    public User save(User user) {
        if(user.getId()!=null){
            mapper.update(user);
        } else {
            user.setId(System.currentTimeMillis());
            mapper.insert(user);
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        return mapper.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return mapper.findByUsername(username);
    }

    @Override
    public User findByTel(String tel) {
        return mapper.findByTel(tel);
    }

    @Override
    public List<User> findAll() {
        return mapper.findAll();
    }

    @Override
    public void delete(Long id) {
        mapper.delete(id);
    }

    @Override
    public List<User> findByName(String name) {
        return mapper.findByName("%" + name + "%");
    }

}
