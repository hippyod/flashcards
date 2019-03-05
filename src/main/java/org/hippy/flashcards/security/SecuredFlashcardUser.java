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

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Custom Spring security User that contains the FlashcardUser user id.
 * 
 * @author Hippy
 */
public class SecuredFlashcardUser extends User {
    
    private long id;

    /**
     * @param id
     * @param username
     * @param password
     * @param authorities
     */
    public SecuredFlashcardUser(long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        
        this.id = id;
    }

    /**
     * @param id
     * @param username
     * @param password
     * @param enabled
     * @param accountNonExpired
     * @param credentialsNonExpired
     * @param accountNonLocked
     * @param authorities
     */
    public SecuredFlashcardUser(long id,
                                String username,
                                String password,
                                boolean enabled,
                                boolean accountNonExpired,
                                boolean credentialsNonExpired,
                                boolean accountNonLocked,
                                Collection<? extends GrantedAuthority> authorities)
    {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        
        this.id = id;
    }
    
    /**
     * @return
     */
    public long getId() {
        return id;
    }

}
