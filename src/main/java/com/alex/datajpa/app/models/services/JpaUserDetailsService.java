package com.alex.datajpa.app.models.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alex.datajpa.app.models.dao.IUserDao;
import com.alex.datajpa.app.models.entity.Role;
import com.alex.datajpa.app.models.entity.User;



// No requiere Interface ya que la interface la provee Spring Security
@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserDao userDao;


    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);

        if (user == null) {
            logger.error("User not found!".concat(username));
            throw new UsernameNotFoundException("User: ".concat(username).concat(" not found!"));
        }


        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : user.getAuthorities()) {
            logger.info("ROLE: ".concat(role.getAuthority()));
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        if (authorities.isEmpty()) {
            logger.error("User ".concat(username).concat(" does not have any role asigned!"));
            throw new UsernameNotFoundException("User ".concat(username).concat(" does not have any role asigned!"));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, authorities);
    }

}
