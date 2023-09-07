package dev.ronin_engineer.software_development.domain.auth.service;


import dev.ronin_engineer.software_development.domain.auth.dto.DecodedToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Primary
public class AccessTokenService {

//    @Value("${app.auth.access-token.ttl}")
//    Long accessTokenTTL;
//
//    @Value("${app.auth.email-token.ttl}")
//    Long emailTokenTTL;

//    @Autowired
//    AlgorithmService algorithmService;
//
//    @Autowired
//    RoleService roleService;
//
//    @Autowired
//    UserPermissionService userPermissionService;
//
//    protected static Algorithm alg;
//
//    protected JWTVerifier verifier;
//
//
//    @PostConstruct
//    public void init() {
//        alg = algorithmService.getAlgorithm(DefaultValue.GOOGLE);
//        verifier = JWT.require(alg)
//                .build();
//    }

//    public TokenDto generate(TokenRequest request) {
//        return generate(request, accessTokenTTL);
//    }
//
//    public TokenDto generateForMail(TokenRequest request) {
//        return generate(request, emailTokenTTL);
//    }

//    private TokenDto generate(TokenRequest request, long ttl) {
//        UserProfile userProfile = request.getUserProfile();
//        long issuedAt = System.currentTimeMillis();
//        long expiresAt = issuedAt + ttl;
//
//        try {
//            String accessToken = JWT.create()
//                    .withSubject(userProfile.getEmail())
//                    .withClaim(FieldName.NAME, userProfile.getName())
//                    .withClaim(FieldName.PICTURE, userProfile.getPicture())
//                    .withClaim(FieldName.SCOPE, userPermissionService.getPermissionByUserId(userProfile.getEmail()))
//                    .withExpiresAt(new Date(expiresAt))
//                    .sign(alg);
//
//            return TokenDto.builder()
//                    .token(accessToken)
//                    .expiresAt(expiresAt)
//                    .build();
//        }
//        catch (JWTCreationException e) {
//            log.error("Exception at generating token");
//            e.printStackTrace();
//            throw new BusinessException(ResponseCode.GENERATE_TOKEN_ERROR);
//        }
//    }

    public DecodedToken verify(String accessToken) {
//        try {
//            DecodedJWT decodedJWT = verifier.verify(accessToken);
//            Map<String, Claim> claims = decodedJWT.getClaims();
//            List<String> scope = claims.get(FieldName.SCOPE).asList(String.class);
//            return DecodedToken.builder()
//                    .userId(decodedJWT.getSubject())
//                    .scope(scope)
//                    .build();
//        } catch (Exception e) {
//            log.error("Invalid access token: {} - {}", e.getClass(), accessToken);
//            e.printStackTrace();
//            throw new AuthException(ResponseCode.INVALID_ACCESS_TOKEN);
//        }
        return null;
    }
}
