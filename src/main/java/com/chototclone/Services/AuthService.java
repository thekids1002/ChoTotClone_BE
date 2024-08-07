package com.chototclone.Services;

import com.chototclone.Constant.DefaultConst;
import com.chototclone.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.chototclone.Entities.User user = repository.findByEmail(username);

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    @Transactional
    public boolean activeUser(String entryToken) {
        try {
            Optional<com.chototclone.Entities.User> optionalUser = Optional.ofNullable(repository.findByEntryToken(entryToken));

            if (optionalUser.isEmpty()) {
                return false;
            }

            com.chototclone.Entities.User user = optionalUser.get();

            if (user.getMemberStatus() != DefaultConst.USER_ACTIVE) {
                user.setMemberStatus(DefaultConst.USER_ACTIVE);
                repository.save(user);
            }

            return true;
        } catch (Exception e) {
            logger.error("Error activating user with token: {}", entryToken, e);
            return false;
        }
    }
}
