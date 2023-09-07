package dev.ronin_engineer.software_development.application.aop;


import dev.ronin_engineer.software_development.domain.auth.constant.Action;
import dev.ronin_engineer.software_development.domain.auth.constant.Resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {

    public Action action();

    public Resource resource();
}
