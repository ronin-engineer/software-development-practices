package dev.ronin_engineer.software_development.domain.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {

    private String token;
    private String sessionId;

    private Long expiresAt;
}
