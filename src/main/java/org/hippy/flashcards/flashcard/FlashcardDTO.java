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
package org.hippy.flashcards.flashcard;

/**
 * @author Hippy
 */
public class FlashcardDTO {

    private long id;
    private String question;
    private String answer;
    private String type;

    /**
     * 
     */
    public FlashcardDTO() {
    }

    /**
     * @param id
     * @param flashcardType
     * @param question
     * @param answer
     */
    public FlashcardDTO(long id, String flashcardType, String question, String answer) {
        this.type = flashcardType;
        this.question = question;
        this.answer = answer;
        this.id = id;
    }

    /**
     * @return
     */
    public String getAnswer() {
        return this.answer;
    }

    /**
     * @return
     */
    public long getId() {
        return this.id;
    }

    /**
     * @return
     */
    public String getQuestion() {
        return this.question;
    }

    /**
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @param question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    
}
