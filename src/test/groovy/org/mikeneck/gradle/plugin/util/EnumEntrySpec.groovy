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

import org.mikeneck.gradle.plugin.model.EnumEntry
import spock.lang.Specification
import spock.lang.Unroll

import static org.mikeneck.gradle.plugin.test.support.BooleanExpected.INVALID
import static org.mikeneck.gradle.plugin.test.support.BooleanExpected.VALID
import static org.mikeneck.gradle.plugin.util.EnumEntrySpec.Entry.enumEntry
import static org.mikeneck.gradle.plugin.util.EnumEntryUtil.wellDefined

class EnumEntrySpec extends Specification {

    @Unroll
    def '#entry is #validity' () {
        expect:
        assert wellDefined(entry) == validity.asBoolean

        where:
        entry                       | validity
        enumEntry()                 | INVALID
        enumEntry(value: '')        | INVALID
        enumEntry(value: 'A')       | VALID
        enumEntry(value: 'a')       | INVALID
    }

    static class Entry implements EnumEntry {
        String value
        @Override
        String toString() {
            "EnumEntry(value[${value}])"
        }
        private static EnumEntry enumEntry() {
            new Entry()
        }
        private static EnumEntry enumEntry(Map<String, Object> arg) {
            new Entry(arg)
        }
    }
}
