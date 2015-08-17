/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mikeneck.gradle.plugin.domain

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.mikeneck.gradle.plugin.domain.DomainModelSetup.getModelWithEnum

class ModelDefinitionSpec extends Specification {

    @Rule
    TemporaryFolder tmpDir = new TemporaryFolder()

    def findSpec() {
        when:
        def md = new ModelDefinition(tmpDir.root, model)
        def objects = md.findClasses {ClassObject co ->
            co.enumType == isEnum
        }

        then:
        objects.collect {
            it.javaName
        } == expectedJavaNames

        where:
        model               | isEnum    | expectedJavaNames
        getModelWithEnum()  | true      | ['Favorite.java']
        getModelWithEnum()  | false     | ['Person.java']
    }

    def eachClassSpec() {
        when:
        def md = new ModelDefinition(tmpDir.root, model)

        then:
        md.eachClasses {ClassObject co ->
            assert map[co.javaName] == co.entries.size()
        }

        where:
        model               | map
        getModelWithEnum()  | ['Favorite.java': (2 + 1), 'Person.java': (2 + 1) + (2 + 1) + 2]
    }
}
