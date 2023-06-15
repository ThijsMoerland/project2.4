package SOT.Squad.code.generation.JWT;

import SOT.Squad.code.generation.Models.Role;
import SOT.Squad.code.generation.Services.UserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Date;
import java.util.List;

@Component
public class JWTTokenProvider {

    @Autowired
    JWTKeyProvider keyProvider;

    @Autowired
    UserDetailService myUserDetailsService;

    public String createToken(String username, List<Role> roles) throws JwtException {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().map(Role::name).toList());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(keyProvider.getPrivateKey()) // <- this is important, we need a key to sign the jwt
                .compact();
    }

    public Authentication getAuthentication(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(keyProvider.getPrivateKey()).build().parseClaimsJws(token);
            String username = claims.getBody().getSubject();
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails,
                    ""
                    , userDetails.getAuthorities());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Bearer token not valid");
        }
    }

}
