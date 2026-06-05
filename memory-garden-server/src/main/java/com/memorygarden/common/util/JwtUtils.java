package com.memorygarden.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.memorygarden.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * JWT Token 工具类
 *
 * <p>提供 Token 生成、验证和解析功能，基于 HMAC-SHA256 签名算法。
 * 密钥通过 Spring 配置属性 jwt.secret 注入，环境变量 JWT_SECRET 优先。</p>
 *
 * @author jLU
 * @date 2026-06-03
 */
@Slf4j
@Component
public class JwtUtils {

    /**
     * JWT 签名密钥（通过 Spring 配置注入，环境变量优先）
     */
    @Value("${JWT_SECRET:${jwt.secret:}}")
    private String secretValue;

    private static String SECRET;

    /**
     * JWT 签发者
     */
    private static final String ISSUER = "memory-garden";

    /**
     * JWT 中用户 ID 的 Claim 名称
     */
    private static final String CLAIM_USER_ID = "userId";

    /**
     * 初始化密钥，Spring 容器启动后校验密钥是否已配置
     */
    @PostConstruct
    public void init() {
        if (secretValue == null || secretValue.trim().isEmpty()) {
            throw new IllegalStateException("JWT_SECRET 环境变量或 jwt.secret 配置未设置，请配置后启动应用");
        }
        SECRET = secretValue;
        log.info("JWT 密钥初始化完成");
    }

    /**
     * 生成 JWT Token
     *
     * @param userId 用户 ID
     * @return JWT Token 字符串
     */
    public static String generateToken(Long userId) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim(CLAIM_USER_ID, userId)
                .withExpiresAt(new java.util.Date(System.currentTimeMillis() + (long) Constant.TOKEN_EXPIRE_SECONDS * 1000))
                .sign(algorithm);
    }

    /**
     * 验证并解析 JWT Token
     *
     * @param token JWT Token 字符串
     * @return 用户 ID，验证失败返回 null
     */
    public static Long verifyAndGetUserId(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim(CLAIM_USER_ID).asLong();
        } catch (JWTVerificationException e) {
            log.warn("JWT 验证失败: {}", e.getMessage());
            return null;
        }
    }
}
