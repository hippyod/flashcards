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
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author Hippy
 */
class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    
    private SecurityConstants securityConstants;
    private JwtTokenProvider jwtTokenProvider;
    
    /**
     * @param authenticationManager
     * @param jwtTokenProvider
     * @param securityConstants
     */
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtTokenProvider jwtTokenProvider,
                                  SecurityConstants securityConstants)
    {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.securityConstants = securityConstants;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.web.authentication.www.BasicAuthenticationFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = getJwtFromRequest(request);
        String fingerPrint = (authToken != null) ? getFingerprint(request) : null;

        String userName = (authToken != null && fingerPrint != null) ? jwtTokenProvider.validateToken(authToken, fingerPrint) : null;
        if (StringUtils.isNotBlank(userName)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName, null, Collections.emptyList());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else if (authToken != null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * @param request
     * @return
     */
    String getFingerprint(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String fingerPrint = null;
        for (int i = 0; cookies != null && fingerPrint == null && i < cookies.length; i++) {
            fingerPrint = cookies[i].getName().equals(securityConstants.FINGERPRINT_COOKIE_NAME) ? cookies[i].getValue() : null;
        }
        return fingerPrint;
    }

    /**
     * @param request
     * @return
     */
    String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(securityConstants.JWT_HEADER_REQUEST_KEY);
        return StringUtils.substringAfter(bearerToken, securityConstants.JWT_HEADER_VALUE_PREFIX);
    }
}