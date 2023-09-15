package dev.ronin_engineer.software_development.application.aop;


import dev.ronin_engineer.software_development.application.constant.ResponseCode;
import dev.ronin_engineer.software_development.application.dto.exception.BusinessException;
import dev.ronin_engineer.software_development.domain.auth.constant.Action;
import dev.ronin_engineer.software_development.domain.auth.constant.Resource;
import dev.ronin_engineer.software_development.infrastructure.constant.FieldName;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.List;


@Aspect
@Component
@Slf4j
public class AuthorizationAspect {


    @SneakyThrows
    @Around("@annotation(Authorize)")
    public Object preAuthorize(ProceedingJoinPoint jp) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        List<String> scope = (List<String>) attributes.getAttribute(FieldName.SCOPE, 0);

        if (scope == null || scope.isEmpty()) {
            throw new BusinessException(ResponseCode.FORBIDDEN);
        }

        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Authorize authorize = method.getAnnotation(Authorize.class);
        Action action = authorize.action();
        Resource resource = authorize.resource();
        String permission = action.getAction() + ":" + resource.getResource();

        if (!isAllowed(scope, permission))
            throw new BusinessException(ResponseCode.FORBIDDEN);

        return jp.proceed();
    }

    private static boolean isAllowed(List<String> scopes, String permission) {
        for (String s : scopes) {
            if (s.equals(permission)) {
                return true;
            }
        }

        return false;
    }
}
