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

import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;
    
/**
 * @author Hippy
 */
@Validated
public class FlashcardUserDTO {
	private String userName;
	private String password;
	private String confirmPassword;
	
	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(@Size(min=6, max=20)String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(@Size(min=6, max=20)String password) {
		this.password = password;
	}
	
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(@Size(min=6, max=20) String userName) {
		this.userName = userName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NewUser [userName=");
		builder.append(userName);
		builder.append(", password=");
		builder.append(password);
		builder.append(", confirmPassword=");
		builder.append(confirmPassword);
		builder.append("]");
		return builder.toString();
	}
}
