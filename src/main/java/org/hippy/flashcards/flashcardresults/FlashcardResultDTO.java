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
package org.hippy.flashcards.flashcardresults;

import java.util.Date;

import org.hippy.flashcards.flashcard.FlashcardDTO;

/**
 * @author Hippy
 */
public class FlashcardResultDTO implements java.io.Serializable {

	private FlashcardDTO flashcard;
	private String userAnswer;
	private Date dateCreated;

	/**
	 * 
	 */
	public FlashcardResultDTO() {
	}

    /**
     * @param flashcard
     * @param userAnswer
     * @param dateCreated
     */
    public FlashcardResultDTO(FlashcardDTO flashcard, String userAnswer, Date dateCreated) {
		this.flashcard = flashcard;
		this.userAnswer = userAnswer;
		this.dateCreated = dateCreated;
	}

	/**
	 * @return
	 */
	public Date getDateCreated() {
		return this.dateCreated;
	}

	/**
	 * @return
	 */
	public FlashcardDTO getFlashcard() {
		return this.flashcard;
	}

	/**
	 * @return
	 */
	public String getUserAnswer() {
		return this.userAnswer;
	}

	/**
	 * @param dateCreated
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @param flashcard
	 */
	public void setFlashcard(FlashcardDTO flashcard) {
		this.flashcard = flashcard;
	}

	/**
	 * @param userAnswer
	 */
	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

}
