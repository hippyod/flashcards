/*
   Copyright 2019 Evan "Hippy" Slatis

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.hippy.flashcards.security;


import java.security.SecureRandom;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

/**
 * @author Hippy
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	
	static final String FINGERPRINT_KEY = "Secret-FingerPrint-Key";

    private SecureRandom secureRandom = new SecureRandom();
	
	private SecurityConstants securityConstants;
	
    private final Algorithm SIGNATURE_ALGORITHM;
	
	/**
	 * @param securityConstants
	 */
	public JwtTokenProvider(SecurityConstants securityConstants) {
        this.securityConstants = securityConstants;

        SIGNATURE_ALGORITHM = Algorithm.HMAC512("The brown fox jumps over the poorly hidden secret.");
    }
	
	/**
	 * Generate a random string that will constitute the fingerprint for this user
	 * 
	 * @return
	 */
	public String generateFingerprint() {
        byte[] randomFgp = new byte[50];
        this.secureRandom.nextBytes(randomFgp);
        
        return DatatypeConverter.printHexBinary(randomFgp);
	}

	/**
	 * @param subject
	 * @param fingerPrint
	 * @return
	 */
	public String generateToken(String subject, String fingerPrint) {
	    if (StringUtils.isBlank(subject) || StringUtils.isBlank(fingerPrint)) {
	        throw new NullPointerException("Subject and/or fingerprint cannot be null or empty");
	    }
	    
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + securityConstants.MAX_AGE_MILLISECONDS);

		fingerPrint = DigestUtils.sha256Hex(fingerPrint);
		return JWT.create()
		          .withSubject(subject.toString())
		          .withIssuedAt(now)
		          .withNotBefore(now)
		          .withExpiresAt(expiryDate)
		          .withClaim(FINGERPRINT_KEY, fingerPrint.toString())
		          .sign(SIGNATURE_ALGORITHM);
	}

    /**
     * @param authToken a JWT token, null is acceptable
     * @param fingerPrint a fingerprint to match with the token, null is acceptable
     * @return the userName associated with the token; null if the token is invalid or either parameter is null
     */
    public String validateToken(String authToken, String fingerPrint) {
        String result = null;
        if (authToken != null && fingerPrint != null) {
            try {
                fingerPrint = DigestUtils.sha256Hex(fingerPrint);
                JWTVerifier verifier = JWT.require(SIGNATURE_ALGORITHM)
                                          .withClaim(FINGERPRINT_KEY, fingerPrint)
                                          .build();
    
                result = verifier.verify(authToken).getSubject();
            }
            catch (JWTVerificationException  jwtExc) {
                logger.info("Invalid token sent: {}", jwtExc.getMessage());
            }
        }
        
        return result;
    }
}