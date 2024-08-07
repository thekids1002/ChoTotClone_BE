package com.chototclone.Services;

import com.chototclone.Constant.DefaultConst;
import com.chototclone.Entities.User;
import com.chototclone.Repository.UserRepository;
import com.chototclone.Utils.DateUtil;
import com.chototclone.Utils.StringUtil;
import com.chototclone.Utils.Util;
import jakarta.transaction.Transactional;
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

    @Autowired
    private EmailService emailService;

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null || user.getMemberStatus() == DefaultConst.USER_NOT_ACTIVE) {
            return null;
        }
        return user;
    }

    @Transactional
    @Override
    public boolean createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        long currentTime = System.currentTimeMillis();
        user.setCreatedAt(new Date(currentTime));
        user.setUpdatedAt(new Date(currentTime));

        String entryToken = StringUtil.generateRandomString(DefaultConst.DEFAULT_NUMBER_CHARACTER_TOKEN);
        boolean isSentMail = emailService.sendActivationEmail(user.getEmail(), entryToken);

        if (isSentMail) {
            user.setEntryToken(entryToken);
            user.setEntryTokenExpire(DateUtil.addDays(DefaultConst.DEFAULT_ENTRY_TOKEN_EXPIRE));
            User result = userRepository.save(user);
            return Util.notNull(result.getUserId());
        }

        return false;
    }
}
