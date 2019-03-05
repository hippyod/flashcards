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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.hippy.flashcards.flashcarduser.FlashcardUser;
import org.hippy.flashcards.flashcarduser.FlashcardUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {
    
    @Mock FlashcardUserRepository userRepository;
    
    private UserDetailsServiceImpl userDetailsServiceImpl;
    
    @Before
    public void setup() {
        userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    public void testLoadUserByName() {
        String username = "someName";
        String pwd = "asdlfhalifu";
        FlashcardUser flashcardUser = new FlashcardUser(57l, username, "asdlfhalifu", new Date(), new Date());
        
        when(userRepository.findByUsername(username)).thenReturn(flashcardUser);
        
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        assertTrue(((SecuredFlashcardUser)userDetails).getId() == 57);
        assertTrue(((SecuredFlashcardUser)userDetails).getUsername() == username);
        assertTrue(((SecuredFlashcardUser)userDetails).getPassword() == pwd);
    }
    
    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserName_userNameNotFound() {
        String username = "someName";
        when(userRepository.findByUsername(username)).thenReturn(null);
        
        userDetailsServiceImpl.loadUserByUsername(username);
    }
    
    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserName_nullUserName() {
        when(userRepository.findByUsername(null)).thenReturn(null);
        
        userDetailsServiceImpl.loadUserByUsername(null);
    }

}
