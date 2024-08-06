package com.chototclone.Services;

import com.chototclone.Entities.User;
import com.chototclone.Repository.UserRepository;
import com.chototclone.Utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        long currentTime = System.currentTimeMillis();
        user.setCreatedAt(new Date(currentTime));
        user.setUpdatedAt(new Date(currentTime));
        User result = userRepository.save(user);
        return Util.notNull(result.getUserId());
    }
}
