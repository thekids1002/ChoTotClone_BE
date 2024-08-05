package com.chototclone.Payload.Request;

import com.chototclone.Rules.MailRFCConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Email is required")
    @MailRFCConstraint
    private String email;
}
