package SOT.Squad.code.generation.JWT;

import SOT.Squad.code.generation.Models.User;
import SOT.Squad.code.generation.Services.UserService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.security.KeyStore;

@Component
public class JWTKeyProvider {

    @Value("${jwt.key-store}")
    private String keystore;
    @Value("${jwt.key-store-password}")
    private String password;
    @Value("${jwt.key-alias}")
    private String alias;
    private Key privateKey;

    @PostConstruct
    protected void init() {
        try {
            Resource resource = new ClassPathResource(keystore);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(resource.getInputStream(), password.toCharArray());
            privateKey = keyStore.getKey(alias, password.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Key getPrivateKey() {
        return privateKey;
    }


    public String decodeJWT() {
        String jwtToken = extractJwtTokenFromRequest();
        return getUsernameFromJwtToken(jwtToken);
    }

    private String extractJwtTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new JwtException("Authorization header missing or invalid");
        }
        return authorizationHeader.replace("Bearer ", "");
    }

    private String getUsernameFromJwtToken(String jwtToken) {
        try {
            return Jwts.parser().setSigningKey(getPrivateKey()).parseClaimsJws(jwtToken).getBody().getSubject();
        } catch (JwtException e) {
            throw new JwtException("Invalid token");
        }
    }

}
