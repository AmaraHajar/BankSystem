package org.solareflare.project.BankSystemMangement.dto;

import lombok.*;



@Data
public class SignUpRequest {
    String firstName;
    String lastName;
    @NonNull
    String email;
    @NonNull
    String password;
}
