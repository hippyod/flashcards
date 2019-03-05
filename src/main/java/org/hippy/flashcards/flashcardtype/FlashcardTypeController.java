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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hippy.flashcards.flashcard.FlashcardDTO;
import org.hippy.flashcards.flashcardtype.FlashcardType;
import org.hippy.flashcards.flashcardtype.FlashcardTypeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hippy
 */
@RestController
@RequestMapping("/flashcardTypes")
public class FlashcardTypeController {
	
	private static final String TYPE_PATH_VARIABLE = "type";
    private FlashcardTypeRepository flashcardTypeRepository;
	
	/**
	 * @param flashcardTypeRepository
	 */
	public FlashcardTypeController(FlashcardTypeRepository flashcardTypeRepository) {
	    this.flashcardTypeRepository = flashcardTypeRepository;
    }
	
	/**
     * @param type
     * @return
     */
    @GetMapping("/{" + TYPE_PATH_VARIABLE + "}")
    public FlashcardTypeDTO getFlashcards(@PathVariable(TYPE_PATH_VARIABLE) String type) {
        FlashcardType fcType = flashcardTypeRepository.findFlashcardTypeByCardType(type);
        
        List<FlashcardDTO> flashcardDtos =
            fcType.getFlashcards().stream().map(flashcard -> {
                return new FlashcardDTO(flashcard.getId(), fcType.getCardType(), flashcard.getQuestion(), flashcard.getAnswer());
            }).collect(Collectors.toList());
        FlashcardTypeDTO flashcardTypeDto = 
            new FlashcardTypeDTO(fcType.getCardType(), fcType.getTitle(), fcType.getDescription(), fcType.getQuestion(), fcType.getUserAnswerFormat(), flashcardDtos);
        
        return flashcardTypeDto;
    }
    
    /**
	 * @return
	 */
	@GetMapping
	public List<FlashcardTypeDTO> getFlashcardTypes() {
		List<FlashcardType> flashcardTypes = flashcardTypeRepository.findAllByOrderByCardType();
		
		ArrayList<FlashcardTypeDTO> fcTypes = new ArrayList<>();
		for (FlashcardType fcType : flashcardTypes) {
	        FlashcardTypeDTO flashcardTypeDto = 
	            new FlashcardTypeDTO(fcType.getCardType(), fcType.getTitle(), fcType.getDescription(), fcType.getQuestion(), fcType.getUserAnswerFormat(), null);
		    fcTypes.add(flashcardTypeDto);
		}
		
		return fcTypes;
	}
    
    /**
     * @param type
     * @return
     */
    @GetMapping("/shuffled/{" + TYPE_PATH_VARIABLE + "}")
    public FlashcardTypeDTO getShuffledFlashcards(@PathVariable(TYPE_PATH_VARIABLE) String type) {
        FlashcardTypeDTO flashcardTypeDto = getFlashcards(type);
        Collections.shuffle(flashcardTypeDto.getFlashcards());
        
        return flashcardTypeDto;
    }

}
