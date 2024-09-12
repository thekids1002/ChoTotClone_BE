package com.chototclone.Payload.Request;

import com.chototclone.Rules.MailRFCConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be at most 50 characters long")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(max = 20, message = "Password must be at most 20 characters long")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Email is required")
    @MailRFCConstraint
    @Size(max = 100, message = "Email must be at most 100 characters long")
    private String email;
}
