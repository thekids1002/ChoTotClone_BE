package com.chototclone.Controller;

import com.chototclone.Entities.User;
import com.chototclone.JWT.JwtHelper;
import com.chototclone.Payload.Request.LoginRequest;
import com.chototclone.Payload.Response.LoginResponse;
import com.chototclone.Repository.UserRepository;
import com.chototclone.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JwtHelper helper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository UserRepository;

    @GetMapping("/test")
    String getAll() {
        return "Ok";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        User user = this.UserRepository.findByEmail(request.getEmail());

        if(user != null){
            this.doAuthenticate(request.getEmail(), request.getPassword());
            UserDetails userDetails = authService.loadUserByUsername(request.getEmail());
            String token = this.helper.generateToken(userDetails);

            LoginResponse response = LoginResponse.builder().jwt(token).build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>("Không ổn", HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

}
