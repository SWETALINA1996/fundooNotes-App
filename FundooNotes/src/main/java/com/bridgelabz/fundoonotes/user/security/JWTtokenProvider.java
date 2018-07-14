package com.bridgelabz.fundoonotes.user.security;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import com.bridgelabz.fundoonotes.user.models.User;

import io.jsonwebtoken.Claims;
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
		
		return Jwts.builder().setId(userEmail).setIssuedAt(date).setSubject(userName)
				.signWith(SignatureAlgorithm.HS256, key).compact();
		
	}
	
	/**To claim token
	 * @param jwt
	 */
	public String parseJWT(String jwt)
	{

		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(key)).parseClaimsJws(jwt)
				.getBody();
	    String claimId = claims.getId();
		//claims.getSubject();
		return claimId;
	}
}
