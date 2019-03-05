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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class FlashcardSecurityConfigurationTest {
    
    public static final String PSECRET = "?)652-aHS sa hcus( AHS esoohc I dluohs yhW";
    
    @Mock private JwtTokenProvider mockJwtTokenProvider;
    @Mock private JwtAuthenticationEntryPoint mockAuthenticationEntryPoint;
    @Mock private UserDetailsService mockUserDetailsService;
    @Mock private SecurityConstants mockSecurityConstants;
    
    @Mock private AuthenticationManagerBuilder mockAuthenticationManagerBuilder;
    
    @Mock BCryptPasswordEncoder mockBcryptPasswordEncoder;
    
    @Mock DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsService> mockDaoAuthenticationConfigurer;
    
    @Spy @InjectMocks private FlashcardSecurityConfiguration spyFlashcardSecurityConfiguration;
    
    @Test
    public void testConfigure_AuthenticationManagerBuilder() throws Exception {
        doReturn(mockBcryptPasswordEncoder).when(spyFlashcardSecurityConfiguration).passwordEncoder();
        when(mockAuthenticationManagerBuilder.userDetailsService(mockUserDetailsService)).thenReturn(mockDaoAuthenticationConfigurer);
        when(mockDaoAuthenticationConfigurer.passwordEncoder(mockBcryptPasswordEncoder)).thenReturn(mockDaoAuthenticationConfigurer);
        
        spyFlashcardSecurityConfiguration.configure(mockAuthenticationManagerBuilder);
        
        verify(mockAuthenticationManagerBuilder).userDetailsService(mockUserDetailsService);
        verify(mockDaoAuthenticationConfigurer).passwordEncoder(mockBcryptPasswordEncoder);
    }

}
