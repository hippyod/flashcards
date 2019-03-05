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
package org.hippy.flashcards.flashcarduser;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.hippy.flashcards.security.JwtTokenProvider;
import org.hippy.flashcards.security.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hippy
 */
@RestController
public class FlashcardUserController {
    
    private static final Logger logger = LoggerFactory.getLogger(FlashcardUserController.class);

    private static final String COOKIE_HEADER_KEY = "Set-Cookie";
    
    public static final String SIGN_UP_URL = "/sign-up";

    private FlashcardUserRepository flashcardUserRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private SecurityConstants securityConstants;

    /**
     * @param flashcardUserRepository
     * @param passwordEncoder
     * @param jwtTokenProvider
     * @param securityConstants
     */
    public FlashcardUserController(FlashcardUserRepository flashcardUserRepository,
                                   BCryptPasswordEncoder passwordEncoder,
                                   JwtTokenProvider jwtTokenProvider,
                                   SecurityConstants securityConstants)
    {
        this.flashcardUserRepository = flashcardUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.securityConstants = securityConstants;
    }

    /**
     * @param userName
     * @return
     */
    @GetMapping(value = "/user/{userName}")
    public FlashcardUser getUser(@PathVariable String userName) {
        return flashcardUserRepository.findByUsername(userName);
    }


    /**
     * @param newUser
     * @return
     * @throws URISyntaxException
     * @throws JSONException
     */
    @PostMapping(SIGN_UP_URL)
    public ResponseEntity<JSONObject> signUp(@Valid @RequestBody FlashcardUserDTO newUser) throws URISyntaxException, JSONException {
        logger.info("Registering:\n{}", newUser);
        FlashcardUser flashcardUser = null;
        ResponseEntity<JSONObject> responseEntity = null;
        if (newUser.getPassword().equals(newUser.getConfirmPassword()) && !flashcardUserRepository.existsByUsername(newUser.getUserName())) {
            flashcardUser = new FlashcardUser();
            flashcardUser.setUsername(newUser.getUserName());
            flashcardUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            
            flashcardUserRepository.save(flashcardUser);

            responseEntity = createResponseEntity(flashcardUser.getId());
        }
        else {
            responseEntity = ResponseEntity.badRequest().build();
        }
        
        return responseEntity;
    }
    
    
    /**
     * @param id
     * @return
     */
    ResponseEntity<JSONObject> createResponseEntity(Long id) {
        String fingerprint = jwtTokenProvider.generateFingerprint();
        String jwtToken = jwtTokenProvider.generateToken(id.toString(), fingerprint);
        String cookie =  securityConstants.FINGERPRINT_COOKIE_NAME + "=" + fingerprint + securityConstants.FINGERPRINT_COOKIE_CONFIG;
        
        return ResponseEntity.ok()
                             .header(COOKIE_HEADER_KEY, cookie)
                             .header(securityConstants.JWT_HEADER_RESPONSE_KEY, jwtToken)
                             .build();
    }
    
    
}
