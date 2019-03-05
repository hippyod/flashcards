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
// Generated Aug 20, 2018 7:04:52 PM by Hibernate Tools 3.5.0.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hippy.flashcards.flashcard.Flashcard;
import org.hippy.flashcards.flashcarduser.FlashcardUser;

/**
 * Results generated by hbm2java
 */
@Entity
@Table(name = "flashcard_result", schema = "public")
public class FlashcardResult implements java.io.Serializable {

	private long id;
	private Flashcard flashcard;
	private FlashcardUser flashcardUser;
	private String userAnswer;
	private Date dateCreated;

	/**
	 * 
	 */
	public FlashcardResult() {
	}

    /**
     * @param flashcard
     * @param flashcardUser
     * @param userAnswer
     */
    public FlashcardResult(Flashcard flashcard, FlashcardUser flashcardUser, String userAnswer) {
        this.flashcard = flashcard;
        this.flashcardUser = flashcardUser;
        this.userAnswer = userAnswer;
    }

	/**
	 * @param id
	 * @param flashcard
	 * @param flashcardUser
	 * @param userAnswer
	 * @param dateCreated
	 */
	public FlashcardResult(long id, Flashcard flashcard, FlashcardUser flashcardUser, String userAnswer, Date dateCreated) {
		this.id = id;
		this.flashcard = flashcard;
		this.flashcardUser = flashcardUser;
		this.userAnswer = userAnswer;
		this.dateCreated = dateCreated;
	}

	/**
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 29, insertable = false, updatable = false)
	public Date getDateCreated() {
		return this.dateCreated;
	}

	/**
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "flashcard", nullable = false)
	public Flashcard getFlashcard() {
		return this.flashcard;
	}

	/**
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "flashcard_user", nullable = false)
	public FlashcardUser getFlashcardUser() {
		return this.flashcardUser;
	}

	/**
	 * @return
	 */
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId() {
		return this.id;
	}

	/**
	 * @return
	 */
	@Column(name = "user_answer", length = 64)
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
	public void setFlashcard(Flashcard flashcard) {
		this.flashcard = flashcard;
	}

	/**
	 * @param flashcardUser
	 */
	public void setFlashcardUser(FlashcardUser flashcardUser) {
		this.flashcardUser = flashcardUser;
	}

	/**
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param userAnswer
	 */
	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

}
