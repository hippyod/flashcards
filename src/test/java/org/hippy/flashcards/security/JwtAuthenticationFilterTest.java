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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hippy.flashcards.flashcarduser.FlashcardUserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JwtAuthenticationFilter.class)
@PowerMockIgnore("javax.security.auth.Subject")
public class JwtAuthenticationFilterTest {

    @Mock ObjectMapper mockObjectMapper;
    @Mock ServletInputStream mockServletInputStream;
    @Mock HttpServletRequest mockHttpServletRequest;
    @Mock HttpServletResponse mockHttpServletResponse;
    @Mock UsernamePasswordAuthenticationToken mockUsernamePasswordAuthenticationToken;
    @Mock AuthenticationManager mockAuthenticationManager;
    @Mock JwtTokenProvider mockJwtTokenProvider;
    @Mock SecuredFlashcardUser mockSecuredFlashcardUser;
    
    private SecurityConstants securityConstants =
        new SecurityConstants("fingerPrintCookieName", "fingerPrintCookieConfig", 1234l);
    
    private JwtAuthenticationFilter testJwtAuthenticationFilter;
    
    @Before
    public void setup() {
        testJwtAuthenticationFilter = new JwtAuthenticationFilter(mockAuthenticationManager, mockJwtTokenProvider, securityConstants);
    }
    
    @Test
    public void testAttemptAuthentication() throws Exception {
        FlashcardUserDTO flashcardUserDTO = new FlashcardUserDTO();
        flashcardUserDTO.setUserName("testingName");
        flashcardUserDTO.setUserName("testingPassword");
        
        whenNew(ObjectMapper.class).withNoArguments().thenReturn(mockObjectMapper);
        when(mockHttpServletRequest.getInputStream()).thenReturn(mockServletInputStream);
        when(mockObjectMapper.readValue(mockServletInputStream, FlashcardUserDTO.class)).thenReturn(flashcardUserDTO);
        
        whenNew(UsernamePasswordAuthenticationToken.class).withArguments(eq(flashcardUserDTO.getUserName()), eq(flashcardUserDTO.getPassword()), any())
                                                          .thenReturn(mockUsernamePasswordAuthenticationToken);
        
        when(mockAuthenticationManager.authenticate(mockUsernamePasswordAuthenticationToken)).thenReturn(mockUsernamePasswordAuthenticationToken);
         
        
        testJwtAuthenticationFilter.attemptAuthentication(mockHttpServletRequest, mockHttpServletResponse);
        
        verifyNew(UsernamePasswordAuthenticationToken.class).withArguments(eq(flashcardUserDTO.getUserName()), eq(flashcardUserDTO.getPassword()), any());
        verify(mockAuthenticationManager).authenticate(mockUsernamePasswordAuthenticationToken);
    }
    
    @Test
    public void testSuccessfulAuthentication() throws Exception {
        when(mockJwtTokenProvider.generateFingerprint()).thenReturn("fingerPrint");
        when(mockUsernamePasswordAuthenticationToken.getPrincipal()).thenReturn(mockSecuredFlashcardUser);
        when(mockSecuredFlashcardUser.getId()).thenReturn(4l);
        when(mockJwtTokenProvider.generateToken("4", "fingerPrint")).thenReturn("jwtToken");
        
        String cookie =  securityConstants.FINGERPRINT_COOKIE_NAME + "=fingerPrint" + securityConstants.FINGERPRINT_COOKIE_CONFIG;
        
        testJwtAuthenticationFilter.successfulAuthentication(mockHttpServletRequest, mockHttpServletResponse, null, mockUsernamePasswordAuthenticationToken);
        
        verify(mockHttpServletResponse).addHeader(HttpHeaders.SET_COOKIE, cookie);
        verify(mockHttpServletResponse).addHeader(securityConstants.JWT_HEADER_RESPONSE_KEY, "jwtToken");
    }

}
