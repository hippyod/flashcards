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

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hippy.flashcards.flashcarduser.FlashcardUserDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Hippy
 */
class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private SecurityConstants securityConstants;

    /**
     * @param authenticationManager
     * @param jwtTokenProvider
     * @param securityConstants
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, SecurityConstants securityConstants) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.securityConstants = securityConstants;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter#attemptAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FlashcardUserDTO creds = objectMapper.readValue(request.getInputStream(), FlashcardUserDTO.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUserName(), creds.getPassword(), new ArrayList<>()));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#successfulAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain, org.springframework.security.core.Authentication)
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException
    {
        String fingerprint = jwtTokenProvider.generateFingerprint();
        String id = Long.toString(((SecuredFlashcardUser) auth.getPrincipal()).getId());
        String jwtToken = jwtTokenProvider.generateToken(id, fingerprint);
        String cookie =  securityConstants.FINGERPRINT_COOKIE_NAME + "=" + fingerprint + securityConstants.FINGERPRINT_COOKIE_CONFIG;

        response.addHeader(HttpHeaders.SET_COOKIE, cookie);
        response.addHeader(securityConstants.JWT_HEADER_RESPONSE_KEY, jwtToken);
    }
}
