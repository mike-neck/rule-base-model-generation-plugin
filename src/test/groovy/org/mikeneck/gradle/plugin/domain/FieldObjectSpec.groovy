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

import org.mikeneck.gradle.plugin.model.PropertyEntryPojo
import org.mikeneck.gradle.plugin.model.Type
import spock.lang.Specification

class FieldObjectSpec extends Specification {

    static final Set<String> TYPES = ['Person', 'Favorite']

    def unableToGenerateAccessors() {
        when:
        def fo = new FieldObject(entry, TYPES)

        then:
        fo.unableToGenerateAccessors == cannotGenerate

        where:
        entry                                                   | cannotGenerate
        new PropertyEntryPojo('name', Type.STRING)              | false
        new PropertyEntryPojo('fav', Type.ENUM, 'Favorite')     | false
        new PropertyEntryPojo('address', Type.OTHER, 'City')    | true
        new PropertyEntryPojo('fav', Type.MODEL_SET, 'Fav')     | true
        new PropertyEntryPojo('fav', Type.MODEL_MAP, 'Favorite')| false
    }

    def getterSpec() {
        when:
        def fo = new FieldObject(entry, TYPES)

        then:
        fo.getter == getter

        where:
        entry                                                   | getter
        new PropertyEntryPojo('name', Type.STRING)              | 'String getName();'
        new PropertyEntryPojo('fav', Type.ENUM, 'Favorite')     | 'Favorite getFav();'
        new PropertyEntryPojo('address', Type.OTHER, 'City')    | 'City getAddress();'
        new PropertyEntryPojo('fav', Type.MODEL_SET, 'Favorite')| 'ModelSet<Favorite> getFav();'
    }

    def setterSpec() {
        when:
        def fo = new FieldObject(entry, TYPES)

        then:
        fo.setterRequired == need
        fo.setter == setter

        where:
        entry                                                   | need  | setter
        new PropertyEntryPojo('name', Type.STRING)              | true  | 'void setName(String name);'
        new PropertyEntryPojo('fav', Type.ENUM, 'Favorite')     | true  | 'void setFav(Favorite fav);'
        new PropertyEntryPojo('address', Type.OTHER, 'City')    | false | 'void setAddress(City address);'
        new PropertyEntryPojo('fav', Type.MODEL_SET, 'Favorite')| false | 'void setFav(ModelSet<Favorite> fav);'
    }
}
