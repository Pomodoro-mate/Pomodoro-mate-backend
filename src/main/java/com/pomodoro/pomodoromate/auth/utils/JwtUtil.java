package com.pomodoro.pomodoromate.auth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pomodoro.pomodoromate.auth.exceptions.AccessTokenExpiredException;
import com.pomodoro.pomodoromate.auth.exceptions.RefreshTokenExpiredException;
import com.pomodoro.pomodoromate.auth.exceptions.TokenDecodingFailedException;
import com.pomodoro.pomodoromate.user.models.UserId;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    private final Algorithm algorithm;

    @Value("${access-token.validation-second}")
    private Long accessTokenValidationSecond;

    @Value("${refresh-token.validation-second}")
    private Long refreshTokenValidationSecond;

    public JwtUtil(String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String encode(UserId userId) {
        return JWT.create()
                .withClaim("userId", userId.value())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidationSecond))
                .sign(algorithm);
    }

    public String encode(UUID uuid) {
        return JWT.create()
                .withClaim("uuid", uuid.toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidationSecond))
                .sign(algorithm);
    }

    public UserId decode(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT verify = verifier.verify(token);

            Long userId = verify.getClaim("userId").asLong();
            return UserId.of(userId);
        } catch (TokenExpiredException exception) {
            throw new AccessTokenExpiredException();
        } catch (JWTDecodeException exception) {
            throw new TokenDecodingFailedException();
        }
    }

    public String decodeRefreshToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT verify = verifier.verify(token);

            return verify.getClaim("uuid").asString();
        } catch (TokenExpiredException exception) {
            throw new RefreshTokenExpiredException();
        } catch (JWTDecodeException exception) {
            throw new TokenDecodingFailedException();
        }
    }
}
