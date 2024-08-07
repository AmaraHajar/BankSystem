package org.solareflare.project.BankSystemMangement.dto;




import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @NonNull
    String email;
    @NonNull
    String password;
}
