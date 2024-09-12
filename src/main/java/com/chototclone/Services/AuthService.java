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

    /**
     * Loads a user by their email (username) for authentication.
     *
     * @param username the email of the user to be loaded
     * @return a `UserDetails` object containing the user's information
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.chototclone.Entities.User user = repository.findByEmail(username);

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    /**
     * Activates a user account
     *
     * @param user
     * @return true if the user was successfully activated, false otherwise
     */
    @Transactional
    public boolean activeUser(com.chototclone.Entities.User user) {
        try {

            if (user.getMemberStatus() != DefaultConst.USER_ACTIVE) {
                user.setMemberStatus(DefaultConst.USER_ACTIVE);
                repository.save(user);
            }
            return true;
        } catch (Exception e) {
            logger.error("Error activating user with token: {}", user.getEntryToken(), e);
            return false;
        }
    }

    /**
     * Finds a user by the given entry token.
     *
     * @param entryToken The user's entry token.
     * @return An Optional containing the user if found, otherwise an empty Optional.
     */
    public Optional<com.chototclone.Entities.User> findUserByToken(String entryToken) {
        return Optional.ofNullable(repository.findByEntryToken(entryToken));
    }

}
