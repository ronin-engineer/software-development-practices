package dev.ronin_engineer.software_development.application.dto.exception;

import dev.ronin_engineer.software_development.application.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthException extends RuntimeException {
    protected ResponseCode responseCode;
}
