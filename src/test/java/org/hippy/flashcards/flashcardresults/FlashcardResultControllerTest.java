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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.function.Function;

import org.hippy.flashcards.flashcard.Flashcard;
import org.hippy.flashcards.flashcard.FlashcardRepository;
import org.hippy.flashcards.flashcardtype.FlashcardType;
import org.hippy.flashcards.flashcarduser.FlashcardUser;
import org.hippy.flashcards.flashcarduser.FlashcardUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

@RunWith(MockitoJUnitRunner.class)
public class FlashcardResultControllerTest {

    private static final String USER_ID_STR = "1";
    private static final long USER_ID = 1l;
    private static final long FLASHCARD_ID = 1l;
    private static final String FLASHCARD_QUESTION = "question";
    private static final String USER_ANSWER = "userAnswer";
    private static final String FLASHCARD_ANSWER = "flashcardAnswer";
    private static final String FLASHCARD_TYPE_STR = "FLASHCARD_TYPE";
    
    @Mock private FlashcardResultRepository mockFlashcardResultRepository;
    @Mock private FlashcardRepository mockFlashcardRepository;
    @Mock private FlashcardUserRepository mockFlashcardUserRepository;
    @Mock private Principal mockPrincipal;
    
    @Mock private FlashcardUser mockFlashcardUser;
    @Mock private Flashcard mockFlashcard;
    @Mock private FlashcardType mockFlashcardType;
    @Mock private FlashcardResult mockFlashcardResult;
    @Mock private FlashcardResultDTO mockFlashcardResultDto;

    @Mock private Page<FlashcardResult> mockFlashcardResultPage;
    @Mock private Page<FlashcardResultDTO> mockFlashcardResultDtoPage;
    @Mock private Pageable mockFlashcardResultPageable;
    
    private FlashcardResultController flashcardResultController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        flashcardResultController =
            new FlashcardResultController(mockFlashcardResultRepository, mockFlashcardRepository, mockFlashcardUserRepository);

        when(mockPrincipal.getName()).thenReturn(USER_ID_STR);
        when(mockFlashcardUserRepository.getOne(USER_ID)).thenReturn(mockFlashcardUser);
    }

    @Test
    public void testSubmitResult() {
        when(mockFlashcardRepository.getOne(FLASHCARD_ID)).thenReturn(mockFlashcard);
        
        when(mockFlashcard.getFlashcardType()).thenReturn(mockFlashcardType);
        when(mockFlashcardType.getUserAnswerFormat()).thenReturn(".*");
        
        flashcardResultController.submitResult(mockPrincipal, FLASHCARD_ID, USER_ANSWER);
        
        ArgumentCaptor<FlashcardResult> argument = ArgumentCaptor.forClass(FlashcardResult.class);
        verify(mockFlashcardResultRepository).save(argument.capture());
        assertEquals(argument.getValue().getFlashcard(), mockFlashcard);
        assertEquals(argument.getValue().getFlashcardUser(), mockFlashcardUser);
        assertEquals(argument.getValue().getUserAnswer(), USER_ANSWER);
    }

    @Test(expected = NullPointerException.class)
    public void testSubmitResult_flashcardIdNotExist() {
        when(mockPrincipal.getName()).thenReturn("1");
        when(mockFlashcardUserRepository.getOne(1l)).thenReturn(null);
        
        flashcardResultController.submitResult(mockPrincipal, FLASHCARD_ID, USER_ANSWER);
    }

    @Test(expected = ResponseStatusException.class)
    public void testSubmitResult_userAnswerInvalidFormat() {
        when(mockFlashcardRepository.getOne(FLASHCARD_ID)).thenReturn(mockFlashcard);
        
        when(mockFlashcard.getFlashcardType()).thenReturn(mockFlashcardType);
        when(mockFlashcardType.getUserAnswerFormat()).thenReturn("\\d+");
        
        flashcardResultController.submitResult(mockPrincipal, FLASHCARD_ID, USER_ANSWER);
    }
    
    @Test
    public void testGetResult() {
        when(mockPrincipal.getName()).thenReturn(USER_ID_STR);
        when(mockFlashcardResultRepository.findByFlashcardUserIdAndFlashcardFlashcardTypeCardTypeOrderByDateCreatedDesc(USER_ID, FLASHCARD_TYPE_STR, mockFlashcardResultPageable))
            .thenReturn(mockFlashcardResultPage);
        
        when(mockFlashcardResultPage.map(Mockito.<Function<FlashcardResult, FlashcardResultDTO>>any())).thenReturn(mockFlashcardResultDtoPage);
        
        Page<FlashcardResultDTO> page = flashcardResultController.getResults(mockPrincipal, FLASHCARD_TYPE_STR, mockFlashcardResultPageable);
        
        assertEquals(mockFlashcardResultDtoPage, page);
    }
    
    @Test
    public void testFlashcardResultToFlashcardResultDTO() {
        when(mockFlashcardResult.getFlashcard()).thenReturn(mockFlashcard);
        when(mockFlashcardResult.getUserAnswer()).thenReturn(USER_ANSWER);
        when(mockFlashcard.getId()).thenReturn(FLASHCARD_ID);
        when(mockFlashcard.getQuestion()).thenReturn(FLASHCARD_QUESTION);
        when(mockFlashcard.getAnswer()).thenReturn(FLASHCARD_ANSWER);
        
        FlashcardResultDTO fcDTO = flashcardResultController.flashcardResultToFlashcardResultDTO(mockFlashcardResult, FLASHCARD_TYPE_STR);

        assertEquals(FLASHCARD_ID, fcDTO.getFlashcard().getId());
        assertEquals(FLASHCARD_TYPE_STR, fcDTO.getFlashcard().getType());
        assertEquals(FLASHCARD_QUESTION, fcDTO.getFlashcard().getQuestion());
        assertEquals(FLASHCARD_ANSWER, fcDTO.getFlashcard().getAnswer());
        assertEquals(USER_ANSWER, fcDTO.getUserAnswer());
    }
    
    @Test(expected = NullPointerException.class)
    public void testFlashcardResultToFlashcardResultDTO_nullFlashcardResult() {
        flashcardResultController.flashcardResultToFlashcardResultDTO(null, FLASHCARD_TYPE_STR);
    }
    
    @Test(expected = NullPointerException.class)
    public void testFlashcardResultToFlashcardResultDTO_nullFlashcardType() {
        when(mockFlashcardResult.getFlashcard()).thenReturn(mockFlashcard);
        when(mockFlashcard.getId()).thenReturn(FLASHCARD_ID);
        
        flashcardResultController.flashcardResultToFlashcardResultDTO(mockFlashcardResult, null);
    }

}
