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
package org.hippy.flashcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Hippy
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class FlashcardsApplication implements WebMvcConfigurer {

    public static final String APPLICATION_CONTEXT_PATH = "/";
    public static final String APPLICATION_HOME = "/index.html";

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(FlashcardsApplication.class, args);
    }
}
