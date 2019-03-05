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
package org.hippy.flashcards.flashcardtype;

import java.util.List;

import org.hippy.flashcards.flashcard.FlashcardDTO;

/**
 * @author Hippy
 */
public class FlashcardTypeDTO {
    private String type;
    private String title;
    private String description;
    private String question;
    private String userAnswerFormat;
    private List<FlashcardDTO> flashcards;
    
    /**
     * @param type
     * @param title
     * @param description
     * @param question
     * @param userAnswerFormat
     * @param flashcards
     */
    public FlashcardTypeDTO(String type, String title, String description, String question, String userAnswerFormat, List<FlashcardDTO> flashcards) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.question = question;
        this.userAnswerFormat = userAnswerFormat;
        
        this.flashcards = flashcards;
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @return the flashcards
     */
    public List<FlashcardDTO> getFlashcards() {
        return flashcards;
    }
    
    /**
     * @return the description
     */
    public String getQuestion() {
        return question;
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * @return the userAnswerFormat
     */
    public String getUserAnswerFormat() {
        return userAnswerFormat;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @param flashcards the flashcards to set
     */
    public void setFlashcards(List<FlashcardDTO> flashcards) {
        this.flashcards = flashcards;
    }

    /**
     * @param description the description to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param userAnswerFormat the userAnswerFormat to set
     */
    public void setUserAnswerFormat(String userAnswerFormat) {
        this.userAnswerFormat = userAnswerFormat;
    }
    
}
