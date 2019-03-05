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

import static java.util.Collections.emptyList;

import org.hippy.flashcards.flashcarduser.FlashcardUser;
import org.hippy.flashcards.flashcarduser.FlashcardUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Hippy
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private FlashcardUserRepository userRepository;

    /**
     * @param userRepository
     */
    public UserDetailsServiceImpl(FlashcardUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        FlashcardUser flashcardUser = userRepository.findByUsername(username);

        if (flashcardUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new SecuredFlashcardUser(flashcardUser.getId(), flashcardUser.getUsername(), flashcardUser.getPassword(), emptyList());
    }
}
