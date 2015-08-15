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
package org.mikeneck.gradle.plugin.util

import org.mikeneck.gradle.plugin.model.PropertyEntry
import org.mikeneck.gradle.plugin.model.Type
import spock.lang.Specification
import spock.lang.Unroll

import static org.mikeneck.gradle.plugin.test.support.BooleanExpected.INVALID
import static org.mikeneck.gradle.plugin.test.support.BooleanExpected.VALID
import static org.mikeneck.gradle.plugin.util.PropertyEntrySpec.Entry.entry
import static org.mikeneck.gradle.plugin.util.PropertyEntryUtil.wellDefined

class PropertyEntrySpec extends Specification {

    @Unroll
    def '#prop is #validity'() {
        expect:
        assert wellDefined(prop) == validity.asBoolean

        where:
        prop                                                            | validity
        entry()                                                         | INVALID
        entry(name: '')                                                 | INVALID
        entry(name: 'A1')                                               | INVALID
        entry(name: 'anItem')                                           | INVALID
        entry(name: 'asIf', type: Type.INTEGER)                         | VALID
        entry(name: 'when', type: Type.MODEL_SET)                       | INVALID
        entry(name: 'asIf', type: Type.MODEL_SET, refType: '')          | INVALID
        entry(name: 'asIf', type: Type.OTHER, refType: 'String')        | INVALID
        entry(name: 'asIf', type: Type.MODEL_MAP, refType: 'aType')     | INVALID
        entry(name: 'asIf', type: Type.ENUM, refType: 'Easy')           | VALID
    }

    static class Entry implements PropertyEntry {
        String name
        Type type
        String refType
        @Override
        String toString() {
            "PropertyEntry(name[${name}], type[$type], refType[${refType}])"
        }
        static PropertyEntry entry() {
            new Entry()
        }
        static PropertyEntry entry(Map<String, Object> args) {
            new Entry(args)
        }
    }
}
