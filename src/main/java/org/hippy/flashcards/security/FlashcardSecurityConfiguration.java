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

import org.hippy.flashcards.FlashcardsApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Hippy
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class FlashcardSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(FlashcardSecurityConfiguration.class);
    
    public static final String PSECRET = "?)652-aHS sa hcus( AHS esoohc I dluohs yhW";
    
    private static final String[] PERMIT_ALL_ANT_MATCHERS = {
        FlashcardsApplication.APPLICATION_HOME,
        FlashcardsApplication.APPLICATION_CONTEXT_PATH,
        "/sign-up",
        "/error",
        "/favicon.ico",
        "/**/*.png",
        "/**/*.gif", 
        "/**/*.svg",
        "/**/*.jpg",
        "/**/*.css",
        "/**/*.js"
    };

    private JwtTokenProvider jwtTokenProvider;
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private UserDetailsService userDetailsService;
    private SecurityConstants securityConstants;

    /**
     * @param jwtTokenProvider
     * @param authenticationEntryPoint
     * @param userDetailsService
     * @param securityConstants
     */
    public FlashcardSecurityConfiguration(JwtTokenProvider jwtTokenProvider,
                                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                                          UserDetailsService userDetailsService,
                                          SecurityConstants securityConstants)
    {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.securityConstants = securityConstants;
    }
    
    /* (non-Javadoc)
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /* (non-Javadoc)
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
            .csrf()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers(PERMIT_ALL_ANT_MATCHERS)
                    .permitAll()
                .anyRequest()
                    .authenticated()
                    .and()
                .formLogin()
                    .loginPage(FlashcardsApplication.APPLICATION_HOME)
                    .permitAll()
                    .and()
                .logout()
                    .deleteCookies(securityConstants.FINGERPRINT_COOKIE_NAME)
                    .permitAll();
        

        http.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider, securityConstants));
        http.addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtTokenProvider, securityConstants));
    }
    
    /**
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}