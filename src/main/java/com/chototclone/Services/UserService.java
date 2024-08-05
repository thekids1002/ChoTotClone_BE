package com.chototclone.Services;

import com.chototclone.Entities.User;

public interface UserService {

     User findByEmail(String email);

     boolean createUser(User user);
}
