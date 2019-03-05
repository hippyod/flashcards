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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hippy.flashcards.flashcard.Flashcard;
import org.hippy.flashcards.flashcard.FlashcardDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class FlashcardTypeControllerTest {

    private static final String FLASHCARD_ANSWER = "someAnswer";
    private static final String FLASHCARD_QUESTION = "someQuestion";

    private static final String FLASHCARD_TYPE_STR = "someType";
    private static final String FLASHCARD_TYPE_TITLE = "typeTitle";
    private static final String FLASHCARD_TYPE_DESCRIPTION = "someDescription";
    private static final String FLASHCARD_TYPE_QUESTION = "someTypeQuestion";
    private static final String FLASHCARD_TYPE_USER_ANSWER_FORMAT = "someUserAnswerFormat";
    
    @Mock private FlashcardTypeRepository mockFlashcardTypeRepository;
    @Mock private FlashcardType mockFlashcardType;
    @Mock private Flashcard mockFlashcard1;
    @Mock private Flashcard mockFlashcard2;

    private FlashcardTypeController flashcardTypeController;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        flashcardTypeController = new FlashcardTypeController(mockFlashcardTypeRepository);
    }
    
    @Test
    public void testGetFlashcards() {
        long startId = 1;
        int numFlashcards = 5;
        List<FlashcardType> flashcardType = createFlashcardTypeList(startId,  1,  startId,  numFlashcards);
        when(mockFlashcardTypeRepository.findFlashcardTypeByCardType(FLASHCARD_TYPE_STR)).thenReturn(flashcardType.get(0));
        
        FlashcardTypeDTO flashcardTypeDTO = flashcardTypeController.getFlashcards(FLASHCARD_TYPE_STR);

        assertEquals(FLASHCARD_TYPE_STR + startId, flashcardTypeDTO.getType());
        assertEquals(FLASHCARD_TYPE_TITLE + startId, flashcardTypeDTO.getTitle());
        assertEquals(FLASHCARD_TYPE_DESCRIPTION + startId, flashcardTypeDTO.getDescription());
        assertEquals(FLASHCARD_TYPE_USER_ANSWER_FORMAT + startId, flashcardTypeDTO.getUserAnswerFormat());
        
        for (int i = 0; i < numFlashcards; i++) {
            FlashcardDTO flashcardDTO = flashcardTypeDTO.getFlashcards().get(i);
            long id = i + startId;
            assertEquals(id, flashcardDTO.getId());
            assertEquals(FLASHCARD_TYPE_STR + startId, flashcardDTO.getType());
            assertEquals(FLASHCARD_QUESTION + id, flashcardDTO.getQuestion());
            assertEquals(FLASHCARD_ANSWER + id, flashcardDTO.getAnswer());
        }
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetFlashcards_InvalidType() {
        when(mockFlashcardTypeRepository.findFlashcardTypeByCardType(any())).thenReturn(null);
        
        flashcardTypeController.getFlashcards("foo");
    }
    
    @Test
    public void testGetFlashcardTypes() {
        long startId = 1;
        int numFlashcardTypes = 3;
        int numFlashcards = 0;
        List<FlashcardType> flashcardTypes = createFlashcardTypeList(startId,  numFlashcardTypes,  startId,  numFlashcards);
        when(mockFlashcardTypeRepository.findAllByOrderByCardType()).thenReturn(flashcardTypes);
        
        List<FlashcardTypeDTO> flashcardTypeDTOs = flashcardTypeController.getFlashcardTypes();
        
        for (int i = 0; i < numFlashcards; i++) {
            FlashcardTypeDTO flashcardTypeDTO = flashcardTypeDTOs.get(i);
            long id = i + startId;
            assertEquals(FLASHCARD_TYPE_STR + id, flashcardTypeDTO.getType());
            assertEquals(FLASHCARD_TYPE_TITLE + id, flashcardTypeDTO.getTitle());
            assertEquals(FLASHCARD_TYPE_DESCRIPTION + id, flashcardTypeDTO.getDescription());
            assertEquals(FLASHCARD_TYPE_USER_ANSWER_FORMAT + id, flashcardTypeDTO.getUserAnswerFormat());
        }
    }
    
    @Test
    @PrepareOnlyThisForTest({Collections.class})
    public void testGetShuffledFlashcards() {
        List<FlashcardDTO> flashcards = Collections.emptyList();
        FlashcardTypeDTO flashcardTypeDTO =
            new FlashcardTypeDTO(FLASHCARD_TYPE_STR, FLASHCARD_TYPE_TITLE, FLASHCARD_TYPE_DESCRIPTION, FLASHCARD_QUESTION, FLASHCARD_ANSWER, flashcards);
        
        mockStatic(Collections.class);
        Collections.shuffle(flashcards);
        
        FlashcardTypeController spyFlashcardTypeController = spy(flashcardTypeController);
        doReturn(flashcardTypeDTO).when(spyFlashcardTypeController).getFlashcards(FLASHCARD_TYPE_STR);
        spyFlashcardTypeController.getShuffledFlashcards(FLASHCARD_TYPE_STR);
        
        verify(spyFlashcardTypeController).getFlashcards(FLASHCARD_TYPE_STR);
        verifyStatic(Collections.class, times(1));
        Collections.shuffle(flashcards);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetShuffledFlashcards_InvalidType() {
        when(mockFlashcardTypeRepository.findFlashcardTypeByCardType(any())).thenReturn(null);
        
        flashcardTypeController.getShuffledFlashcards("foo");
    }
    
    private List<Flashcard> createFlashcardList(long startId, int count) {
        List<Flashcard> flashcardsList = new ArrayList<>();
        for (long i = 0; i < count; i++) {
            long id = i + startId;
            Flashcard flashcard = new Flashcard(id, mockFlashcardType, FLASHCARD_QUESTION + id, FLASHCARD_ANSWER + id);
            flashcardsList.add(flashcard);
        }
        
        return flashcardsList;
    }
    
    private List<FlashcardType> createFlashcardTypeList(long startId, int count, long flashCardStartId, int flashCardCount) {
        List<FlashcardType> flashcardTypesList = new ArrayList<>();
        for (long i = 0; i < count; i++) {
            List<Flashcard> flashcards = createFlashcardList(flashCardStartId, flashCardCount);
            long id = i + startId;
            FlashcardType flashcardType =
                new FlashcardType(id, FLASHCARD_TYPE_STR + id, FLASHCARD_TYPE_TITLE + id, FLASHCARD_TYPE_DESCRIPTION + id, FLASHCARD_TYPE_QUESTION + id, FLASHCARD_TYPE_USER_ANSWER_FORMAT + id, flashcards);
            flashcardTypesList.add(flashcardType);
        }
        
        return flashcardTypesList;
    }

}
