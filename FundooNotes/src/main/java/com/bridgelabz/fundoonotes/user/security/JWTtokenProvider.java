package com.bridgelabz.fundoonotes.user.security;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.user.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JWTtokenProvider {

final static String key = "Sweta" ;
	
	/**To generate the token
	 * @param user
	 * @return
	 */
	public String generator(User user) 
	{
		String userName = user.getUserName();
		
		String userEmail = user.getEmailId();
		
		//long time = System.currentTimeMillis();
		
		long nowMillis = System.currentTimeMillis();
		
		Date date = new Date(nowMillis);
		
		JwtBuilder builder = Jwts.builder().setId(userEmail).setIssuedAt(date).setSubject(userName)
				.signWith(SignatureAlgorithm.HS256, key);
		
		return builder.compact();
	}
	
	/**To claim token
	 * @param jwt
	 */
	public void parseJWT(String jwt)
	{

		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(key)).parseClaimsJws(jwt)
				.getBody();
		
	    claims.getId();
		claims.getSubject();
	}
}
