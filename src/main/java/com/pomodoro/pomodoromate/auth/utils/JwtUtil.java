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

import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    private final Algorithm algorithm;
    private final Long ACCESS_TOKEN_VALIDATION_SECOND = 1000L * 60 * 30;
    private final Long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 60 * 24 * 14;

    public JwtUtil(String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String encode(UserId userId) {
        return JWT.create()
                .withClaim("userId", userId.value())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDATION_SECOND))
                .sign(algorithm);
    }

    public String encode(UUID uuid) {
        return JWT.create()
                .withClaim("uuid", uuid.toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDATION_SECOND))
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
