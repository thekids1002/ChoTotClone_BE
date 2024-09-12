package com.chototclone.Payload.Request;

import com.chototclone.Rules.MailRFCConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotNull(message = "Email is required")
    @Size(max = 100, message = "Email must be at most 100 characters long")
    @MailRFCConstraint
    private String email;

    @NotNull(message = "Password is required")
    @Size(max = 20, message = "Password must be at most 20 characters long")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
