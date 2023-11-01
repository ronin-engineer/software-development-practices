package dev.ronin_engineer.software_development.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ronin_engineer.software_development.application.constant.ResponseCode;
import dev.ronin_engineer.software_development.application.dto.exception.AuthException;
import dev.ronin_engineer.software_development.application.dto.request.MutableHttpServletRequest;
import dev.ronin_engineer.software_development.application.dto.request.RequestWrapper;
import dev.ronin_engineer.software_development.application.service.ResponseFactory;
import dev.ronin_engineer.software_development.domain.auth.dto.DecodedToken;
import dev.ronin_engineer.software_development.domain.auth.service.AccessTokenService;
import dev.ronin_engineer.software_development.infrastructure.constant.DefaultValue;
import dev.ronin_engineer.software_development.infrastructure.constant.FieldName;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private static final JSONParser JSON_PARSER = new JSONParser();

    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ResponseFactory responseFactory;


    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(FieldName.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(DefaultValue.BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorizationHeader.replace(DefaultValue.BEARER, "");
        try {
            DecodedToken decodedToken = accessTokenService.verify(accessToken); // 1. verify token
            UsernamePasswordAuthenticationToken authentication = loadAuthentication(decodedToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String userId = decodedToken.getUserId();

            if (StringUtils.startsWithIgnoreCase(request.getContentType(), DefaultValue.MULTIPART_PREFIX)) {
                MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
                mutableRequest.addHeader(FieldName.REQUESTER, userId);  //
                mutableRequest.setAttribute(FieldName.SCOPE, decodedToken.getScope());  // 2. set scope to attribute
                filterChain.doFilter(mutableRequest, response); // 3. move on
            } else {
                RequestWrapper requestWrapper = new RequestWrapper(request);
                JSONObject body = StringUtils.isEmpty(requestWrapper.getBody()) ?
                        new JSONObject() :
                        (JSONObject) JSON_PARSER.parse(requestWrapper.getBody());
                body.put(FieldName.REQUESTER, userId);
                requestWrapper.setAttribute(FieldName.SCOPE, decodedToken.getScope());
                requestWrapper.setBody(body.toString());
                filterChain.doFilter(requestWrapper, response);
            }
        } catch (AuthException e) {
            log.error("Exception at verifying token");
            e.printStackTrace();
            buildErrorResponse(response, ResponseCode.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Filter exception: {}", e.getMessage());
            e.printStackTrace();
            buildErrorResponse(response, ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @SneakyThrows
    private void buildErrorResponse(HttpServletResponse response, ResponseCode responseCode) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(responseCode.getStatus());
        String serialized = objectMapper.writeValueAsString(responseFactory.response(responseCode));
        response.getOutputStream().write(serialized.getBytes());
    }

    private UsernamePasswordAuthenticationToken loadAuthentication(DecodedToken decodedToken) {
        String userId = decodedToken.getUserId();
        UserDetails userDetails = loadByUserId(userId);
        Collection<? extends GrantedAuthority> authorities = decodedToken.getScope().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    private UserDetails loadByUserId(String userId) {
        return new User(userId, "", new HashSet<>());
    }
}
