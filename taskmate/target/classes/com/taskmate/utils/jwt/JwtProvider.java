package com.taskmate.utils.jwt;

import java.time.Instant;
import java.util.Date;

import com.taskmate.domain.User;
import com.taskmate.utils.CommonUtils;
import com.taskmate.utils.Constants;
import com.taskmate.utils.EnumUtils.UserType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtProvider {

	private static long jwtExpirationInMillis = 1200L;
	private static final String GROUP = "group";

	public JwtProvider() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	public static String generateToken(User user) {
		return Jwts.builder().setSubject(user.getEmail()).setIssuedAt(Date.from(Instant.now()))
				.setExpiration(Date.from(Instant.now().plusSeconds(jwtExpirationInMillis)))
				.claim(GROUP, user.getUserType())
				.signWith(SignatureAlgorithm.HS512, CommonUtils.getConfigByKey(Constants.JWT_B64_KEY)).compact();
	}

	public static boolean validateToken(String jwt, String email) {
		Jws<Claims> claims = Jwts.parser().setSigningKey(CommonUtils.getConfigByKey(Constants.JWT_B64_KEY))
				.parseClaimsJws(jwt);
		if (email.equals(claims.getBody().getSubject())) {
			return true;
		}
		return false;
	}

	public static UserType getUserType(String jwt) {
		Jws<Claims> claims = Jwts.parser().setSigningKey(CommonUtils.getConfigByKey(Constants.JWT_B64_KEY))
				.parseClaimsJws(jwt);
		return UserType.getByName(((String) claims.getBody().get(GROUP)).toLowerCase());
	}

}
