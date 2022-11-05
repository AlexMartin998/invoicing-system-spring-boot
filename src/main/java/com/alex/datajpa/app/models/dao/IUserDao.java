package com.alex.datajpa.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.alex.datajpa.app.models.entity.User;


public interface IUserDao extends CrudRepository<User, Long> {
    
    // consulta a traves del method name
    public User findByUsername(String username);
    
}
