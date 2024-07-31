package com.chototclone.Controller;

import Utils.Util;
import com.chototclone.Entities.User;
import com.chototclone.JWT.JwtHelper;
import com.chototclone.Payload.Request.LoginRequest;
import com.chototclone.Payload.Response.LoginResponse;
import com.chototclone.Payload.Response.ReponseObject;
import com.chototclone.Repository.UserRepository;
import com.chototclone.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
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
    public ResponseEntity<ReponseObject> login(@RequestBody LoginRequest request) {
        User user = this.UserRepository.findByEmail(request.getEmail());

        ReponseObject reponseObject;
        reponseObject = ReponseObject.builder()
                .message("Fail")
                .statusCode(HttpStatus.FORBIDDEN.value())
                .data(null)
                .build();

        if (Util.notNull(user)) {
            this.doAuthenticate(request.getEmail(), request.getPassword());
            UserDetails userDetails = authService.loadUserByUsername(request.getEmail());
            String accessToken = this.helper.generateAccessToken(userDetails);
            String refreshToken = this.helper.generateRefreshToken(userDetails);
            LoginResponse response = LoginResponse
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(System.currentTimeMillis() + helper.getJWT_TOKEN_VALIDITY_ACCESSTOKEN() * 1000)
                    .build();
            reponseObject = ReponseObject.builder()
                    .message("Success")
                    .statusCode(HttpStatus.OK.value())
                    .data(response)
                    .build();
            return new ResponseEntity<>(reponseObject, HttpStatus.OK);
        }

        return new ResponseEntity<>(reponseObject, HttpStatus.FORBIDDEN);
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
