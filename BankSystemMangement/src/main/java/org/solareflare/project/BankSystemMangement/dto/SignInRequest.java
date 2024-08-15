package org.solareflare.project.BankSystemMangement.dto;




import lombok.*;

@Data
public class SignInRequest {
    @NonNull
    String email;
    @NonNull
    String password;
}
