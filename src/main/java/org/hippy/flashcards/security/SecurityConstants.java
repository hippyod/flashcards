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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Hippy
 */
@Component
public class SecurityConstants {

    public final String FINGERPRINT_COOKIE_CONFIG;
    public final String FINGERPRINT_COOKIE_NAME;
    public final String JWT_HEADER_RESPONSE_KEY = "x-token";
    public final String JWT_HEADER_REQUEST_KEY = "Authorization";
    public final String JWT_HEADER_VALUE_PREFIX = "Bearer ";
    public final long MAX_AGE_MILLISECONDS;
    
    /**
     * @param fingerPrintCookieName
     * @param fingerPrintCookieConfig
     * @param jwtSecret
     * @param maxAgeMilliseconds
     */
    @Autowired
    public SecurityConstants(@Value("${flashcards.security.constants.fingerPrintCookieName}") String fingerPrintCookieName,
                             @Value("${flashcards.security.constants.fingerPrintCookieConfig}") String fingerPrintCookieConfig,
                             @Value("${flashcards.security.constants.maxAgeMilliseconds}") long maxAgeMilliseconds)
    {
        FINGERPRINT_COOKIE_CONFIG = fingerPrintCookieConfig;
        FINGERPRINT_COOKIE_NAME = fingerPrintCookieName;
        MAX_AGE_MILLISECONDS = maxAgeMilliseconds;
	}
}
