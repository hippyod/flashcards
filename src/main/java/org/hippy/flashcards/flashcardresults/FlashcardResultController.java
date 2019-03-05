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

import java.security.Principal;
import java.util.regex.Pattern;

import org.hippy.flashcards.flashcard.Flashcard;
import org.hippy.flashcards.flashcard.FlashcardDTO;
import org.hippy.flashcards.flashcard.FlashcardRepository;
import org.hippy.flashcards.flashcarduser.FlashcardUser;
import org.hippy.flashcards.flashcarduser.FlashcardUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Hippy
 */
@RestController
public class FlashcardResultController {

    private static final String FLASHCARD_ID_URL_PARAM = "flashcardId";
    private static final String USER_ANSWER_URL_PARAM = "userAnswer";
    private static final String TYPE_URL_PARAM = "type";

    private FlashcardResultRepository flashcardResultRepository;
    private FlashcardRepository flashcardRepository;
    private FlashcardUserRepository flashcardUserRepository;
	
	/**
	 * @param flashcardResultRepository
	 * @param flashcardRepository
	 * @param flashcardUserRepository
	 */
	public FlashcardResultController(FlashcardResultRepository flashcardResultRepository, 
	                                 FlashcardRepository flashcardRepository,
	                                 FlashcardUserRepository flashcardUserRepository)
	{
        this.flashcardResultRepository = flashcardResultRepository;
        this.flashcardRepository = flashcardRepository;
        this.flashcardUserRepository = flashcardUserRepository;
    }
	
	/**
	 * @param principal
	 * @param flashcardId
	 * @param userAnswer
	 * @return
	 */
	@PostMapping("/flashcards/result/{" + FLASHCARD_ID_URL_PARAM + "}/{" + USER_ANSWER_URL_PARAM + "}")
	@ResponseStatus(HttpStatus.OK)
	public void submitResult(Principal principal,
                             @PathVariable(FLASHCARD_ID_URL_PARAM) long flashcardId,
                             @PathVariable(USER_ANSWER_URL_PARAM) String userAnswer)
	{
        FlashcardUser flashcardUser = flashcardUserRepository.getOne(Long.valueOf(principal.getName()));
        Flashcard flashcard = flashcardRepository.getOne(flashcardId);
        
        if (Pattern.matches(flashcard.getFlashcardType().getUserAnswerFormat(), userAnswer)) {
    		FlashcardResult result = new FlashcardResult(flashcard, flashcardUser, userAnswer);
    		
    		flashcardResultRepository.save(result);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User answer has invalid format: " + userAnswer);
        }
	}

    /**
     * @param principal
     * @param type
     * @param resultsPageable
     * @return
     */
    @GetMapping("/user/results/{" + TYPE_URL_PARAM + "}")
    public Page<FlashcardResultDTO> getResults(Principal principal,
                                               @PathVariable(TYPE_URL_PARAM) String type,
                                               @PageableDefault(10) Pageable resultsPageable)
    {
        long userId = Long.valueOf(principal.getName());
        Page<FlashcardResult> resultPage =
            flashcardResultRepository.findByFlashcardUserIdAndFlashcardFlashcardTypeCardTypeOrderByDateCreatedDesc(userId, type, resultsPageable);

        return resultPage.map(fcResult -> flashcardResultToFlashcardResultDTO(fcResult, type));
    }
    
    FlashcardResultDTO flashcardResultToFlashcardResultDTO(FlashcardResult fcResult, String type) {
        Flashcard flashcard = fcResult.getFlashcard();
        FlashcardDTO fcDTO = new FlashcardDTO(flashcard.getId(), type.toString(), flashcard.getQuestion(), flashcard.getAnswer());
        return new FlashcardResultDTO(fcDTO, fcResult.getUserAnswer(), fcResult.getDateCreated());
    }
}
