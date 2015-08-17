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
import org.mikeneck.gradle.plugin.model.ClassType
import spock.lang.Specification

import static org.mikeneck.gradle.plugin.domain.DomainModelSetup.getModelWithEnum

class ClassObjectSpec extends Specification {

    @Rule
    TemporaryFolder tmpDir = new TemporaryFolder()

    def basicValuesSpec() {
        when:
        def mdl = new ModelDefinition(tmpDir.root, domain)
        def klass = domain.classes.find {it.type == type}
        def obj = new ClassObject(klass, mdl)

        then:
        obj.enumType == isEnum
        obj.javaName == javaName
        obj.javaFilePath == "${tmpDir.root}/src/main/groovy/org/mikeneck/${javaName}" as String

        where:
        domain              | type                  | isEnum| javaName
        getModelWithEnum()  | ClassType.ENUM        | true  | 'Favorite.java'
        getModelWithEnum()  | ClassType.INTERFACE   | false | 'Person.java'
    }

    def entriesSpec() {
        when:
        def mdl = new ModelDefinition(tmpDir.root, domain)
        def klass = domain.classes.find {it.type == type}
        def obj = new ClassObject(klass, mdl)

        then:
        obj.entries as Set == entries as Set

        where:
        domain              | type                  | entries
        getModelWithEnum()  | ClassType.ENUM        | ['GAME', 'READING', '']
        getModelWithEnum()  | ClassType.INTERFACE   | ['Favorite GAME = Favorite.GAME;',
                                                       'Favorite READING = Favorite.READING;',
                                                       'String getName();', 'void setName(String name);',
                                                       'Favorite getFavorite();',
                                                       'void setFavorite(Favorite favorite);', '']
    }
}
