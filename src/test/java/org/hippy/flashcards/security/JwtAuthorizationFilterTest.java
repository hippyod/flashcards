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
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthorizationFilterTest {

    private static final String AUTH_TOKEN = "authToken";
    private static final String FINGERPRINT = "fingerprint";
    private static final String USERNAME = "username";

    @Mock HttpServletRequest mockHttpServletRequest;
    @Mock HttpServletResponse mockHttpServletResponse;
    @Mock FilterChain mockFilterChain;
    @Mock AuthenticationManager mockAuthenticationManager;
    @Mock JwtTokenProvider mockJwtTokenProvider;
    @Mock SecurityConstants mockSecurityConstants;

    @Mock SecurityContext mockSecurityContext;
    
    @Spy @InjectMocks JwtAuthorizationFilter spyJwtAuthorizationFilter;

    private SecurityConstants securityConstants =
        new SecurityConstants("fingerPrintCookieName", "fingerPrintCookieConfig", 1234l);
    
    @Before
    public void setupSecurityContext() {
        SecurityContextHolder.setContext(mockSecurityContext);
    }

    @Test
    public void testDoFilterInternal_NullAuthToken() throws Exception {
        doReturn(null).when(spyJwtAuthorizationFilter).getJwtFromRequest(mockHttpServletRequest);
        
        spyJwtAuthorizationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        verify(mockFilterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockSecurityContext, never()).setAuthentication(any());
    }

    @Test
    public void testDoFilterInternal_NullFingerprint() throws Exception {
        doReturn(AUTH_TOKEN).when(spyJwtAuthorizationFilter).getJwtFromRequest(mockHttpServletRequest);
        doReturn(null).when(spyJwtAuthorizationFilter).getFingerprint(mockHttpServletRequest);
        
        spyJwtAuthorizationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
        
        verify(mockHttpServletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        verify(mockFilterChain, never()).doFilter(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockSecurityContext, never()).setAuthentication(any());
    }

    @Test
    public void testDoFilterInternal_InvalidOrExpiredToken() throws Exception {
        doReturn(AUTH_TOKEN).when(spyJwtAuthorizationFilter).getJwtFromRequest(mockHttpServletRequest);
        doReturn(FINGERPRINT).when(spyJwtAuthorizationFilter).getFingerprint(mockHttpServletRequest);
        
        when(mockJwtTokenProvider.validateToken(AUTH_TOKEN, FINGERPRINT)).thenReturn(null);
        
        spyJwtAuthorizationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
        
        verify(mockHttpServletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        verify(mockFilterChain, never()).doFilter(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockSecurityContext, never()).setAuthentication(any());
    }

    @Test
    public void testDoFilterInternal_ValidOrExpiredToken() throws Exception {
        doReturn(AUTH_TOKEN).when(spyJwtAuthorizationFilter).getJwtFromRequest(mockHttpServletRequest);
        doReturn(FINGERPRINT).when(spyJwtAuthorizationFilter).getFingerprint(mockHttpServletRequest);
        
        when(mockJwtTokenProvider.validateToken(AUTH_TOKEN, FINGERPRINT)).thenReturn(USERNAME);
        
        spyJwtAuthorizationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        verify(mockSecurityContext).setAuthentication(any());
        
        verify(mockFilterChain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
        
        verify(mockHttpServletResponse, never()).setStatus(anyInt());
    }

    @Test
    public void testGetFingerprint() throws Exception {
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(mockAuthenticationManager, mockJwtTokenProvider, securityConstants);
            
        Cookie[] cookies = { new Cookie("foo1", "bar"),
                             new Cookie(securityConstants.FINGERPRINT_COOKIE_NAME, FINGERPRINT),
                             new Cookie("foo2", "bar") };

        when(mockHttpServletRequest.getCookies()).thenReturn(cookies);
        
        assertEquals(FINGERPRINT, jwtAuthorizationFilter.getFingerprint(mockHttpServletRequest));

        cookies[1] =  new Cookie("foo3", "bar");
        
        assertNull(jwtAuthorizationFilter.getFingerprint(mockHttpServletRequest));
    }
    
    @Test
    public void testGetJwtFromRequest() {
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(mockAuthenticationManager, mockJwtTokenProvider, securityConstants);
        when(mockHttpServletRequest.getHeader(securityConstants.JWT_HEADER_REQUEST_KEY)).thenReturn("Bearer " + FINGERPRINT);
        
        assertEquals(FINGERPRINT, jwtAuthorizationFilter.getJwtFromRequest(mockHttpServletRequest));
    }

}
