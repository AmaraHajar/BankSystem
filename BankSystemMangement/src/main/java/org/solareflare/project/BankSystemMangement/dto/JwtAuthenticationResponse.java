package org.solareflare.project.BankSystemMangement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class JwtAuthenticationResponse {
    String token;
}