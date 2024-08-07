package com.chototclone.Controller;

import com.chototclone.Entities.User;
import com.chototclone.JWT.JwtHelper;
import com.chototclone.Payload.Request.LoginRequest;
import com.chototclone.Payload.Request.RegisterRequest;
import com.chototclone.Payload.Response.LoginResponse;
import com.chototclone.Payload.Response.ReponseObject;
import com.chototclone.Services.AuthService;
import com.chototclone.Services.UserService;
import com.chototclone.Utils.Message;
import com.chototclone.Utils.Util;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    private UserService userService;

    @GetMapping("/test")
    String getAll() {
        return "Ok";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReponseObject> login(@Valid @RequestBody LoginRequest request) {
        User user = this.userService.findByEmail(request.getEmail());

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

    @PostMapping("/auth/register")
    public ResponseEntity<ReponseObject> register(@Valid @RequestBody RegisterRequest registerRequest) {
        // Create ReponseObject with default value
        ReponseObject responseObject = ReponseObject.builder()
                .statusCode(HttpStatus.OK.value())
                .build();

        try {
            // Construct User object from RegisterRequest
            User newUser = new User();
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(registerRequest.getPassword());
            newUser.setUserName(registerRequest.getName());

            // Handle user creation and update ReponseObject
            boolean isCreated = userService.createUser(newUser);
            if (isCreated) {
                responseObject.setMessage(Message.SUCCESS);
                return new ResponseEntity<>(responseObject, HttpStatus.OK);
            } else {
                responseObject.setMessage(Message.FAIL);
                responseObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
            }
        } catch (DataIntegrityViolationException exception) {
            responseObject = ReponseObject.builder()
                    .message(Message.VALIDATION_ERRORS)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .data(Map.of("email", Message.EMAIL_EXIST))
                    .build();
        }
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/auth/active")
    public ResponseEntity<ReponseObject> activeUser(@RequestParam String entryToken) {

        boolean isActive = authService.activeUser(entryToken);

        HttpStatus httpStatus = isActive ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = isActive ? Message.SUCCESS : Message.FAIL;

        ReponseObject responseObject = ReponseObject.builder()
                .data(null)
                .statusCode(httpStatus.value())
                .message(message)
                .build();

        return new ResponseEntity<>(responseObject, httpStatus);
    }
}
