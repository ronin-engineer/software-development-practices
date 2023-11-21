package dev.ronin_engineer.software_development.domain.auth.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String username;

    private String password;

    private String name;
}
