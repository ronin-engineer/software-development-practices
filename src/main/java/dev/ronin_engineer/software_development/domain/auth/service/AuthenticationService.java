package dev.ronin_engineer.software_development.domain.auth.service;


import dev.ronin_engineer.software_development.application.constant.ResponseCode;
import dev.ronin_engineer.software_development.application.dto.exception.BusinessException;
import dev.ronin_engineer.software_development.domain.auth.dto.TokenResponse;
import dev.ronin_engineer.software_development.domain.auth.dto.request.LoginRequest;
import dev.ronin_engineer.software_development.domain.auth.entity.User;
import dev.ronin_engineer.software_development.domain.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    UserRepository userRepository;


    public TokenResponse authenticate(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException(ResponseCode.USERNAME_OR_PASSWORD_INVALID);
        }

        if (!user.getPassword().equals(request.getPassword())) {
            throw new BusinessException(ResponseCode.USERNAME_OR_PASSWORD_INVALID);
        }

        TokenResponse response = TokenService.generateToken(user);

        return response;
    }
}
