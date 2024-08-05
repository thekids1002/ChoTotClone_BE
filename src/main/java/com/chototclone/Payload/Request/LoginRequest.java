package com.chototclone.Payload.Request;

import com.chototclone.Rules.MailRFCConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotNull(message =  "Email is required")
    @MailRFCConstraint
    private String email;

    @NotNull(message =  "Password is required")
    private String password;
}
