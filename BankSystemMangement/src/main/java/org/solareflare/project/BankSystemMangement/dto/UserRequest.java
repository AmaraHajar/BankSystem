package org.solareflare.project.BankSystemMangement.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    String firstName;
    String lastName;
    @NonNull
    String email;
    @NonNull
    String password;
}
