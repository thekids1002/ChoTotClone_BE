package com.chototclone.Controller;

import com.chototclone.Entities.User;
import com.chototclone.JWT.JwtHelper;
import com.chototclone.Payload.Request.LoginRequest;
import com.chototclone.Payload.Request.RegisterRequest;
import com.chototclone.Payload.Response.LoginResponse;
import com.chototclone.Payload.Response.ResponseBuilder;
import com.chototclone.Payload.Response.ResponseObject;
import com.chototclone.Services.AuthService;
import com.chototclone.Services.UserService;
import com.chototclone.Utils.DateUtil;
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

import java.util.Date;
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
    public ResponseEntity<ResponseObject> login(@Valid @RequestBody LoginRequest request) {
        User user = this.userService.findByEmail(request.getEmail());

        ResponseObject responseObject;
        responseObject = ResponseObject.builder()
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
            responseObject = ResponseObject.builder()
                    .message("Success")
                    .statusCode(HttpStatus.OK.value())
                    .data(response)
                    .build();
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        }

        return new ResponseEntity<>(responseObject, HttpStatus.FORBIDDEN);
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
    public ResponseEntity<ResponseObject> register(@Valid @RequestBody RegisterRequest registerRequest) {
        // Create ReposeObject with default value
        ResponseObject responseObject = ResponseObject.builder()
                .statusCode(HttpStatus.OK.value())
                .build();

        try {
            // Construct User object from RegisterRequest
            User newUser = new User();
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(registerRequest.getPassword());
            newUser.setUserName(registerRequest.getName());

            // Handle user creation and update ResponseObject
            boolean isCreated = userService.createUser(newUser);
            if (isCreated) {
                responseObject.setMessage(Message.SUCCESS);
                return new ResponseEntity<>(responseObject, HttpStatus.OK);
            } else {
                responseObject.setMessage(Message.FAIL);
                responseObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
            }
        } catch (DataIntegrityViolationException exception) {
            responseObject = ResponseObject.builder()
                    .message(Message.VALIDATION_ERRORS)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .data(Map.of("email", Message.EMAIL_EXIST))
                    .build();
        }
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    /**
     * Activates a user based on the provided entry token.
     *
     * @param entryToken The token used to identify the user.
     * @return A ResponseEntity containing the result of the activation process.
     */
    @GetMapping("/auth/active")
    public ResponseEntity<ResponseObject> activeUser(@Valid @RequestParam String entryToken) {
        if (entryToken == null || entryToken.trim().isEmpty()) {
            return ResponseBuilder.createBadRequestResponse("Missing entry token");
        }

        User user = authService.findUserByToken(entryToken)
                .orElse(null);

        if (user == null) {
            return ResponseBuilder.createNotFoundResponse("User not found");
        }

        Date currentDate = new Date();

        if (DateUtil.compareDates(user.getEntryTokenExpire(), currentDate) < 0) {
            return ResponseBuilder.createBadRequestResponse("Expired entry token");
        }

        boolean isActive = authService.activeUser(user);

        return ResponseBuilder.createEmptyResponse(
                isActive ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR,
                isActive ? Message.SUCCESS : Message.FAIL
        );
    }

}
