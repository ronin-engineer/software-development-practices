package dev.ronin_engineer.software_development.domain.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecodedToken {
    private String userId;

    private String name;

    private String picture;

    private List<String> roles;

    private String sessionId;

    private List<String> scope;
}
