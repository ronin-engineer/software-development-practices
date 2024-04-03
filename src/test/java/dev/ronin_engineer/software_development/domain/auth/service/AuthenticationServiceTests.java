package dev.ronin_engineer.software_development.domain.auth.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import dev.ronin_engineer.software_development.application.constant.ResponseCode;
import dev.ronin_engineer.software_development.application.dto.exception.BusinessException;
import dev.ronin_engineer.software_development.domain.auth.dto.TokenResponse;
import dev.ronin_engineer.software_development.domain.auth.dto.request.LoginRequest;
import dev.ronin_engineer.software_development.domain.auth.entity.User;
import dev.ronin_engineer.software_development.domain.auth.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @InjectMocks
    AuthenticationService service;

    @Mock
    UserRepository userRepository;

    public static User defaultUser;

    @BeforeAll
    public void init() {
        defaultUser = new User("ronin_engineer", "pass34", "Ronin");
    }


    @Test
    @DisplayName("Happy Case: login successfully")
    public void authenticate_RightUser_ReturnToken() {
        // 1. Arrange (Given)
        LoginRequest request = new LoginRequest("ronin_engineer", "pass34");
        when(userRepository.findByUsername(request.getUsername()))
                .thenReturn(defaultUser);

        // 2. Action (When)
        TokenResponse response = service.authenticate(request);


        // 3. Assert (Then)
        verify(userRepository, times(1)).findByUsername(anyString());
        assertNotNull(response);
        assertNotEquals("", response.getAccessToken());
    }

    @Test
    @DisplayName("Edge Case: user not found")
    public void authenticate_UserNotFound_ReturnException() {
        // 1. Arrange
        LoginRequest request = new LoginRequest("ict_engineer", "pass34");
        when(userRepository.findByUsername(request.getUsername()))
                .thenReturn(null);

        // 2. Action
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.authenticate(request)
        );

        // 3. Assert
        assertEquals(ResponseCode.USERNAME_OR_PASSWORD_INVALID, exception.getResponseCode());
    }


//    @Test
//    @ValueSource(ints = {0, 2, 9999})
//    public void testNonNegative(Integer x) {
//        sut.methodX(x);
//    }


    // negative, 0, positive
    // method()
    // test 2
    // test 0
    // testNonNegative
    // int[] a = [0, 2, 999999]
    // for (int test: a)
    // {...}

}
