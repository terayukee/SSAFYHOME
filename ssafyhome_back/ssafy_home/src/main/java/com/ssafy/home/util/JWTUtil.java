package com.ssafy.home.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {

	// 솔트
	@Value("${jwt.salt}")
	private String SALT;

	// 유효기간
	@Value("${jwt.access-token.expiretime}")
	private long accessTokenExpireTime;

	// 리프레시 유효기간
	@Value("${jwt.refresh-token.expiretime}")
	private long refreshTokenExpireTime;

	// createToken 만들기
	public String createAccessToken(int user_no) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userNo", user_no);
		claims.put("tokenType", "ACCESS");
		return generateToken(claims, "access-token", accessTokenExpireTime);
	}

	// AccessToken에 비해 유효기간을 길게 설정.
	public String createRefreshToken(int user_no) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userNo", user_no);
		claims.put("tokenType", "REFRESH");
		return generateToken(claims, "refresh-token", refreshTokenExpireTime);
	}

	// Token 발급
	// JWT 구성 : Header + Payload(Claim) + Signature
	private String generateToken(Map<String, Object> claims, String subject, long expireTime) {

		// Header 설정.
		Map<String, String> headers = new HashMap<>();
		headers.put("typ", "JWT");

		return Jwts.builder().header().add(headers).and().claims(claims).subject(subject)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expireTime)).signWith(getSigningKey()).compact();
	}

	// Signature 설정에 들어갈 key 생성.
	private SecretKey getSigningKey() {
		byte[] keyBytes = SALT.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

//	전달 받은 토큰이 제대로 생성된 것인지 확인 하고 문제가 있다면 UnauthorizedException 발생.
	public boolean checkToken(String token) {
		try {
//			Json Web Signature? 서버에서 인증을 근거로 인증 정보를 서버의 private key 서명 한것을 토큰화 한것
//			setSigningKey : JWS 서명 검증을 위한  secret key 세팅
//			parseClaimsJws : 파싱하여 원본 jws 만들기	
			Jws<Claims> claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
//			Claims 는 Map 구현체 형태
			log.debug("claims: {}", claims);

			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public String getUserId(String authorization) {
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authorization);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		Map<String, Object> value = claims.getPayload();
		log.info("value : {}", value);
		return (String) value.get("userId");
	}
}
