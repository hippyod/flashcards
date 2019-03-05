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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenProviderTest {

    private SecurityConstants securityConstants =
        new SecurityConstants("fingerPrintCookieName", "fingerPrintCookieConfig", 86400000l);
    
    private JwtTokenProvider jwtTokenProvider;
    
    @Before
    public void setup() {
        jwtTokenProvider = new JwtTokenProvider(securityConstants); 
    }
    
    @Test
    public void testGenerateFingerprint() {
        String fingerPrint = jwtTokenProvider.generateFingerprint();
        
        byte[] bytes = DatatypeConverter.parseHexBinary(fingerPrint);
        assertEquals(50, bytes.length);
    }
    
    @Test
    public void testGenerateToken() {
        Date now = new Date();
        
        String token = jwtTokenProvider.generateToken("1234", "fingerPrint");
        
        DecodedJWT decodedJWT = JWT.decode(token);
        assertEquals("1234", decodedJWT.getSubject());

        String fingerPrint = DigestUtils.sha256Hex("fingerPrint");
        assertEquals(fingerPrint, decodedJWT.getClaim(JwtTokenProvider.FINGERPRINT_KEY).asString());

        long expiryDateBefore = now.getTime() + securityConstants.MAX_AGE_MILLISECONDS - 1000;
        long expiryDateAfter = now.getTime() + securityConstants.MAX_AGE_MILLISECONDS + 1000;
        long jwtExpiryDate = decodedJWT.getExpiresAt().getTime();
        assertTrue(jwtExpiryDate > expiryDateBefore);
        assertTrue(jwtExpiryDate < expiryDateAfter);

        assertEquals(Algorithm.HMAC512("").getName(), decodedJWT.getAlgorithm());
    }
    
    @Test(expected = NullPointerException.class)
    public void testGenerateToken_NullSubject() {
        jwtTokenProvider.generateToken(null, "fingerPrint");
    }

    @Test(expected = NullPointerException.class)
    public void testGenerateToken_NullFingerPrint() {
        jwtTokenProvider.generateToken("1234", null);
    }
}
