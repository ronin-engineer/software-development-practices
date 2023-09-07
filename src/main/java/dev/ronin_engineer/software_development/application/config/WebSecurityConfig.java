package dev.ronin_engineer.software_development.application.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class WebSecurityConfig {

    @Autowired
    AuthorizationFilter authorizationFilter;

    @Value("${application-name}")
    private String applicationName;

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .cors().and().csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/payslip/v1/token/google").permitAll()
//                .antMatchers("/payslip/v1/token").permitAll()
//                .antMatchers("/payslip/v1/statement_batches/**/approve").permitAll()
//                .antMatchers("/payslip/v1/payment_requests/**/approve").permitAll()
//                .antMatchers("/actuator/health/readiness").permitAll()
//                .anyRequest().authenticated()
//        ;
//        http.addFilter(authorizationFilter);
//
//        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/" + applicationName + "/public/**",
                                "/" + applicationName + "/login")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterAfter(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
