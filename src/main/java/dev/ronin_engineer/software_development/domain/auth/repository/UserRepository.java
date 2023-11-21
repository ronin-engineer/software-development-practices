package dev.ronin_engineer.software_development.domain.auth.repository;

import dev.ronin_engineer.software_development.domain.auth.entity.User;

public interface UserRepository {

    User findByUsername(String username);

}
