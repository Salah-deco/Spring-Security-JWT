package org.sid.authjwt.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sid.authjwt.security.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("------------ attemptAuthentication ------------");
        // recuperer username et password
        String username = request.getParameter("username"); // si parameter passe format x-www-url-form-encoded
        String password = request.getParameter("password");
        System.out.println(username);
        System.out.println(password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("------------ successfulAuthentication ------------");
        User user = (User) authResult.getPrincipal();
        // apres ajouter de dependance auth0 jwt
        // algorithme pour genere signature HMAC256 = symetrique
        Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
        // create JWT token with claims (Subject, Expiration(20min), )
        // =======================================================================
        //  The "iss" (issuer) claim identifies the principal that issued the
        //   JWT.  The processing of this claim is generally application specific.
        //   The "iss" value is a case-sensitive string containing a StringOrURI
        //   value.  Use of this claim is OPTIONAL.
        // =======================================================================
        // .withClaim(name, values) == to private claims
        String jwtAccessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_ACCESS_TOKEN))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))
                .sign(algorithm);

        response.setHeader(JWTUtil.AUTH_HEADER, jwtAccessToken);

        // JWT il presente un probleme concernant la revocation de token
        // ex: si je vous donnee un jwt qui valable pendant 1 mois simplement je peux pas demain vous empecher d'acceder a l'application
        // meme que vous changez le mot de passe ou votre compte a ete supprime
        // solution utilise 2 token accessToken, refreshToken
        String jwtRefreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_REFRESH_TOKEN))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String, String> idToken = new HashMap<>();
        idToken.put("access-token", jwtAccessToken);
        idToken.put("refresh-token", jwtRefreshToken);

        // utiliser Jackson new ObjectMapper pour serialise un object format JSON
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), idToken);
    }
}
