package dev.ronin_engineer.software_development.domain.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRequest {

    UserProfile userProfile;

    Map<String, Object> claims;

    List<String> roles;
    List<String> scope;
}
